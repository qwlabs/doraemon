package com.qwlabs.tq.repositories;

import com.qwlabs.tq.models.ProcessStatus;
import com.qwlabs.tq.models.TaskQueueRecord;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Optional;

public interface TaskQueueRecordRepository {
    <R extends TaskQueueRecord> void persist(@NotNull R record);

    int resetByTimeout(@NotNull String topic, @Nullable String bucket, @NotNull Instant before, @NotNull Integer priority);

    int resetByStatus(@NotNull String topic, @Nullable String bucket, @NotNull ProcessStatus processStatus, @NotNull Integer priority);

    Optional<String> peekId(@NotNull String topic, @Nullable String bucket);

    <R extends TaskQueueRecord> R find(@NotNull String id);

    <R extends TaskQueueRecord> R lock(@NotNull String id);

    int cleanup(@NotNull String topic, @Nullable String bucket, @NotNull ProcessStatus status);
}
