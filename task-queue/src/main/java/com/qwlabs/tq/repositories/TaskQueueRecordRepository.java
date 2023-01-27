package com.qwlabs.tq.repositories;

import com.qwlabs.tq.models.ProcessStatus;
import com.qwlabs.tq.models.TaskQueueRecord;

import java.time.Instant;
import java.util.Optional;

public interface TaskQueueRecordRepository<R extends TaskQueueRecord> {
    void persist(R record);

    int resetByTimeout(String topic, Instant before, Integer priority);

    int resetByStatus(String topic, ProcessStatus processStatus, Integer priority);

    Optional<String> peekId(String topic);

    R lock(String id);
}
