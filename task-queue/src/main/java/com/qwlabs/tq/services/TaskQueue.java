package com.qwlabs.tq.services;

import com.google.common.base.Throwables;
import com.qwlabs.tq.models.ProcessStatus;
import com.qwlabs.tq.models.TaskQueueRecord;
import com.qwlabs.tq.repositories.TaskQueueRecordRepository;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class TaskQueue<R extends TaskQueueRecord> {
    public static final Integer PROCESSING_TIMEOUT_PRIORITY = 60;
    public static final Integer POSTPONED_TO_IDLE_PRIORITY = 50;
    public static final Integer FAILED_TO_IDLE_PRIORITY = 0;
    public static final Integer MAX_PRIORITY = 100;
    public static final Integer NEW_PRIORITY = 80;

    private final TaskQueueRecordRepository<R> repository;

    public TaskQueue(TaskQueueRecordRepository<R> repository) {
        this.repository = repository;
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void enqueue(R record) {
        record.setPriority(NEW_PRIORITY);
        record.setProcessStatus(ProcessStatus.IDLE);
        record.setProcessStartAt(null);
        record.setProcessEndAt(null);
        repository.persist(record);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void onBefore(TaskQueueProcessContext context) {
        repository.resetByTimeout(context.getTopic(), Instant.now().minus(context.getTimeout()), PROCESSING_TIMEOUT_PRIORITY);
        repository.resetByStatus(context.getTopic(), ProcessStatus.FAILED, POSTPONED_TO_IDLE_PRIORITY);
        repository.resetByStatus(context.getTopic(), ProcessStatus.POSTPONED, FAILED_TO_IDLE_PRIORITY);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public String poll(TaskQueueProcessContext context) {
        boolean findCompleted;
        Optional<R> mayRecord;
        do {
            mayRecord = repository.peekId(context.getTopic())
                    .map(repository::lock);
            findCompleted = mayRecord.map(record -> record.getProcessStatus() == ProcessStatus.IDLE).orElse(true);
            LOGGER.info("task queue poll");
        } while (!findCompleted);
        mayRecord.ifPresent(record -> {
            record.setProcessStatus(ProcessStatus.PROCESSING);
            record.setProcessStartAt(Instant.now());
            record.setProcessEndAt(null);
            repository.persist(record);
        });
        return mayRecord
                .map(TaskQueueRecord::getId)
                .orElse(null);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void onWork(String recordId, Function<R, Boolean> processor) {
        R record = repository.lock(recordId);
        boolean succeed = processor.apply(record);
        record.setProcessStatus(succeed ? ProcessStatus.SUCCEED : ProcessStatus.POSTPONED);
        record.setProcessEndAt(Instant.now());
        repository.persist(record);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void onFailed(TaskQueueProcessContext context, String recordId, Exception exception) {
        onFailed(context, recordId, exception, (r)->{});
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void onFailed(TaskQueueProcessContext context, String recordId, Exception exception, Consumer<R> consumer) {
        var record = repository.lock(recordId);
        record.setFailedMessage(Throwables.getStackTraceAsString(exception));
        record.setProcessStatus(ProcessStatus.FAILED);
        record.setProcessEndAt(Instant.now());
        repository.persist(record);
        context.markFailedRecord(recordId);
        consumer.accept(record);
    }
}
