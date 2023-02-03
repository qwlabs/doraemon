package com.qwlabs.tq.services;

import com.qwlabs.tq.models.CleanupTaskQueueCommand;
import com.qwlabs.tq.models.TaskQueueRecord;
import com.qwlabs.tq.repositories.TaskQueueRecordRepository;
import jakarta.inject.Inject;


public class TaskQueueCleaner<R extends TaskQueueRecord> {

    private final TaskQueueRecordRepository<R> repository;

    @Inject
    public TaskQueueCleaner(TaskQueueRecordRepository<R> repository) {
        this.repository = repository;
    }

    public void cleanup(CleanupTaskQueueCommand command) {
        command.getTopics().forEach(topic->
            command.getStatuses().forEach(status->
                repository.cleanup(topic, command.getBucket(), status)
            )
        );
    }
}
