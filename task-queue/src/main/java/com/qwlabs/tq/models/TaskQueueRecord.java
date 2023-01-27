package com.qwlabs.tq.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TaskQueueRecord {
    private String id;
    private String topic;
    private Integer priority;
    private Object context;
    private ProcessStatus processStatus;
    private Instant processStartAt;
    private Instant processEndAt;
    private String failedMessage;
}
