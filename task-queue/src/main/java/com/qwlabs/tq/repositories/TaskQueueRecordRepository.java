package com.qwlabs.tq.repositories;

import com.qwlabs.tq.models.ProcessStatus;
import com.qwlabs.tq.models.TaskQueueRecord;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Instant;
import java.util.Optional;

public interface TaskQueueRecordRepository<R extends TaskQueueRecord> {
    void persist(@NotNull R record);

    int resetByTimeout(@NotNull String topic, @Nullable String bucket, @NotNull Instant before, @NotNull Integer priority);

    int resetByStatus(@NotNull String topic, @Nullable String bucket, @NotNull ProcessStatus processStatus, @NotNull Integer priority);

    Optional<String> peekId(@NotNull String bucket, @NotNull String topic);

    R lock(@NotNull String id);

    void cleanup(@NotNull String topic, @Nullable String bucket, @NotNull ProcessStatus status);
}
