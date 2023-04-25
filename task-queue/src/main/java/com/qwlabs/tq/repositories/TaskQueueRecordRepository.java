package com.qwlabs.tq.repositories;

import com.qwlabs.tq.models.ProcessStatus;
import com.qwlabs.tq.models.TaskQueueRecord;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.Optional;

public interface TaskQueueRecordRepository {
    <R extends TaskQueueRecord> void persist(@NotNull R record);

    int resetByTimeout(@NotNull String topic, @Nullable String bucket, @NotNull Instant before, @NotNull Integer priority);

    int resetByStatus(@NotNull String topic, @Nullable String bucket, @NotNull ProcessStatus processStatus, @NotNull Integer priority);

    Optional<String> peekId(@NotNull String topic, @Nullable String bucket);

    <R extends TaskQueueRecord> R lock(@NotNull String id);

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    int cleanup(@NotNull String topic, @Nullable String bucket, @NotNull ProcessStatus status);
}
