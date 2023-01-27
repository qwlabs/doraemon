package com.qwlabs.tq.services;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class TaskQueueProcessContext {
    private final String topic;
    private final Duration timeout = Duration.ofHours(1);
    private final Set<String> failedRecordIds = Sets.newHashSet();

    public void markFailedRecord(String recordId) {
        this.failedRecordIds.add(recordId);
    }

    public boolean shouldContinue(String recordId) {
        return !this.failedRecordIds.contains(recordId);
    }
}