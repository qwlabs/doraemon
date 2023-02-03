package com.qwlabs.tq.services;

import com.google.common.base.Throwables;
import com.qwlabs.tq.models.ProcessStatus;
import com.qwlabs.tq.models.TaskQueueRecord;
import com.qwlabs.tq.repositories.TaskQueueRecordRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@ApplicationScoped
public class TaskQueue {
    public static final Integer PROCESSING_TIMEOUT_PRIORITY = 60;
    public static final Integer POSTPONED_TO_IDLE_PRIORITY = 50;
    public static final Integer FAILED_TO_IDLE_PRIORITY = 0;
    public static final Integer MAX_PRIORITY = 100;
    public static final Integer NEW_PRIORITY = 80;

    private final TaskQueueRecordRepository repository;

    @Inject
    public TaskQueue(TaskQueueRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public <R extends TaskQueueRecord> void enqueue(R record) {
        record.setPriority(NEW_PRIORITY);
        record.setProcessStatus(ProcessStatus.IDLE);
        record.setProcessStartAt(null);
        record.setProcessEndAt(null);
        repository.persist(record);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void onBefore(TaskQueueProcessContext context) {
        repository.resetByTimeout(context.getBucket(), context.getTopic(), Instant.now().minus(context.getTimeout()), PROCESSING_TIMEOUT_PRIORITY);
        repository.resetByStatus(context.getBucket(), context.getTopic(), ProcessStatus.FAILED, POSTPONED_TO_IDLE_PRIORITY);
        repository.resetByStatus(context.getBucket(), context.getTopic(), ProcessStatus.POSTPONED, FAILED_TO_IDLE_PRIORITY);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public <R extends TaskQueueRecord> String poll(TaskQueueProcessContext context) {
        boolean findCompleted;
        Optional<R> mayRecord;
        do {
            mayRecord = repository.peekId(context.getTopic(), context.getBucket())
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
    public <R extends TaskQueueRecord> void onWork(String recordId,
                                                   Function<R, Boolean> processor) {
        R record = repository.lock(recordId);
        boolean succeed = processor.apply(record);
        record.setProcessStatus(succeed ? ProcessStatus.SUCCEED : ProcessStatus.POSTPONED);
        record.setProcessEndAt(Instant.now());
        repository.persist(record);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public <R extends TaskQueueRecord> void onFailed(TaskQueueProcessContext context,
                                                     String recordId,
                                                     Exception exception) {
        this.doOnFailed(context, recordId, exception, (r, e) -> LOGGER.error("Process task error. id={}", r.getId(), e));
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public <R extends TaskQueueRecord> void onFailed(TaskQueueProcessContext context,
                                                     String recordId,
                                                     Exception exception,
                                                     BiConsumer<R, Exception> consumer) {
        this.doOnFailed(context, recordId, exception, consumer);
    }


    private <R extends TaskQueueRecord> void doOnFailed(TaskQueueProcessContext context,
                                                        String recordId,
                                                        Exception exception,
                                                        BiConsumer<R, Exception> consumer) {
        R record = repository.lock(recordId);
        record.setFailedMessage(Throwables.getStackTraceAsString(exception));
        record.setProcessStatus(ProcessStatus.FAILED);
        record.setProcessEndAt(Instant.now());
        repository.persist(record);
        context.markFailedRecord(record.getId());
        consumer.accept(record, exception);
    }
}
