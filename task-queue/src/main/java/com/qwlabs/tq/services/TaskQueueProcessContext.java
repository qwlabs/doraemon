package com.qwlabs.tq.services;

import com.google.common.collect.Sets;
import com.qwlabs.tq.models.TaskQueueRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class TaskQueueProcessContext<R extends TaskQueueRecord> {
    private final String topic;
    private final String bucket;
    private final Duration timeout = Duration.ofHours(1);
    private final Set<String> failedRecordIds = Sets.newHashSet();

    public void markFailedRecord(R record) {
        this.failedRecordIds.add(record.getId());
    }

    public boolean shouldContinue(R record) {
        return !this.failedRecordIds.contains(record.getId());
    }
}