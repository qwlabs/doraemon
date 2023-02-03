package com.qwlabs.tq.services;

import com.qwlabs.tq.models.CleanupTaskQueueCommand;
import com.qwlabs.tq.repositories.TaskQueueRecordRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ApplicationScoped
public class TaskQueueCleaner {

    private final TaskQueueRecordRepository repository;

    @Inject
    public TaskQueueCleaner(TaskQueueRecordRepository repository) {
        this.repository = repository;
    }

    public void cleanup(CleanupTaskQueueCommand command) {
        command.getTopics().forEach(topic ->
                command.getStatuses().forEach(status -> {
                    var count = repository.cleanup(topic, command.getBucket(), status);
                    LOGGER.warn("Cleanup task queue count: {}", count);
                })
        );
    }
}
