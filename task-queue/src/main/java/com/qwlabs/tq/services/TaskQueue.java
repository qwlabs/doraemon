package com.qwlabs.tq.services;

import com.google.common.base.Throwables;
import com.qwlabs.tq.models.CleanupTaskQueueCommand;
import com.qwlabs.tq.models.ProcessStatus;
import com.qwlabs.tq.models.TaskQueueRecord;
import com.qwlabs.tq.repositories.TaskQueueRecordRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.narayana.jta.TransactionExceptionResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.qwlabs.tq.models.TaskQueuePriorities.FAILED_TO_IDLE;
import static com.qwlabs.tq.models.TaskQueuePriorities.NEW;
import static com.qwlabs.tq.models.TaskQueuePriorities.POSTPONED_TO_IDLE;
import static com.qwlabs.tq.models.TaskQueuePriorities.PROCESSING_TIMEOUT;

@Slf4j
@ApplicationScoped
public class TaskQueue {

    private final TaskQueueRecordRepository repository;

    @Inject
    public TaskQueue(TaskQueueRecordRepository repository) {
        this.repository = repository;
    }

    public <R extends TaskQueueRecord> void enqueue(final R record) {
        QuarkusTransaction.joiningExisting().run(() -> {
            record.setPriority(NEW);
            record.setProcessStatus(ProcessStatus.IDLE);
            record.setProcessStartAt(null);
            record.setProcessEndAt(null);
            repository.persist(record);
        });
    }

    @Retry(delay = 1, delayUnit = ChronoUnit.SECONDS)
    public void cleanup(CleanupTaskQueueCommand command) {
        command.getTopics().forEach(topic -> command.getStatuses().forEach(status -> {
            QuarkusTransaction.requiringNew().run(() -> {
                var count = repository.cleanup(topic, command.getBucket(), status);
                LOGGER.warn("Cleanup task queue count: {}", count);
            });
        }));
    }

    public void onBefore(final TaskQueueProcessContext context) {
        QuarkusTransaction.requiringNew()
            .exceptionHandler(throwable -> TransactionExceptionResult.ROLLBACK)
            .run(() -> {
                repository.resetByTimeout(context.getTopic(), context.getBucket(), Instant.now().minus(context.getTimeout()), PROCESSING_TIMEOUT);
                repository.resetByStatus(context.getTopic(), context.getBucket(), ProcessStatus.FAILED, POSTPONED_TO_IDLE);
                repository.resetByStatus(context.getTopic(), context.getBucket(), ProcessStatus.POSTPONED, FAILED_TO_IDLE);
            });
    }

    public <R extends TaskQueueRecord> String poll(TaskQueueProcessContext context) {
        return QuarkusTransaction.requiringNew()
            .call(() -> {
                boolean findCompleted;
                Optional<R> mayRecord;
                do {
                    mayRecord = repository.peekId(context.getTopic(), context.getBucket()).map(repository::lock);
                    findCompleted = mayRecord.map(record -> record.getProcessStatus() == ProcessStatus.IDLE).orElse(true);
                } while (!findCompleted);
                mayRecord.ifPresent(record -> {
                    record.setProcessStatus(ProcessStatus.PROCESSING);
                    record.setProcessStartAt(Instant.now());
                    record.setProcessEndAt(null);
                    repository.persist(record);
                });
                return mayRecord.map(TaskQueueRecord::getId).orElse(null);
            });
    }

    public <R extends TaskQueueRecord> void onWork(String recordId, Function<R, Boolean> processor) {
        QuarkusTransaction.requiringNew()
            .run(() -> {
                var record = repository.<R>find(recordId);
                if (Objects.isNull(record)) {
                    throw new RuntimeException("Can not onWork because record: %s is null.".formatted(recordId));
                }
                boolean succeed = processor.apply(record);
                record.setProcessStatus(succeed ? ProcessStatus.SUCCEED : ProcessStatus.POSTPONED);
                record.setProcessEndAt(Instant.now());
                repository.persist(record);
                repository.clear();
            });
    }

    public <R extends TaskQueueRecord> void onWorkWithOutTx(String recordId, Function<R, Boolean> processor) {
        var workRecord = repository.<R>find(recordId);
        if (Objects.isNull(workRecord)) {
            throw new RuntimeException("Can not onWork because record: %s is null.".formatted(recordId));
        }
        AtomicBoolean succeed = new AtomicBoolean(false);
        try {
            succeed.set(processor.apply(workRecord));
        } finally {
            QuarkusTransaction.requiringNew()
                .run(() -> {
                    var record = repository.<R>find(recordId);
                    record.setProcessStatus(succeed.get() ? ProcessStatus.SUCCEED : ProcessStatus.POSTPONED);
                    record.setProcessEndAt(Instant.now());
                    repository.persist(record);
                });
        }
    }

    public <R extends TaskQueueRecord> void onFailed(TaskQueueProcessContext context, String recordId, Exception exception) {
        this.onFailed(context, recordId, exception, (r, e) -> LOGGER.error("Process task error. id={}", r.getId(), e));
    }

    public <R extends TaskQueueRecord> void onFailed(TaskQueueProcessContext context,
                                                     String recordId,
                                                     Exception exception,
                                                     BiConsumer<R, Exception> consumer) {
        QuarkusTransaction.requiringNew()
            .run(() -> {
                var record = repository.<R>find(recordId);
                if (Objects.isNull(record)) {
                    LOGGER.error("Can not onFailed because record is null. can not found recordId {}.", recordId, exception);
                    return;
                }
                record.setFailedMessage(Throwables.getStackTraceAsString(exception));
                record.setProcessStatus(ProcessStatus.FAILED);
                record.setProcessEndAt(Instant.now());
                repository.persist(record);
                context.markFailedRecord(record.getId());
                consumer.accept(record, exception);
            });
    }
}
