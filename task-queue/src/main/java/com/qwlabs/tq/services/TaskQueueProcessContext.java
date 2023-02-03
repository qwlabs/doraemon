package com.qwlabs.tq.services;

import com.google.common.collect.Sets;
import com.qwlabs.tq.models.TaskQueueRecord;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class TaskQueueProcessContext {
    @NotNull
    private final String topic;
    @Nullable
    private final String bucket;
    @Builder.Default
    @NotNull
    private final Duration timeout = Duration.ofHours(1);
    @Builder.Default
    @NotNull
    private final Set<String> failedRecordIds = Sets.newHashSet();

    public void markFailedRecord(String recordId) {
        this.failedRecordIds.add(recordId);
    }

    public boolean shouldContinue(String recordId) {
        return !this.failedRecordIds.contains(recordId);
    }
}