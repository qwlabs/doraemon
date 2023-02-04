package com.qwlabs.tq.models;

public final class TaskQueuePriorities {
    public static final Integer PROCESSING_TIMEOUT = 60;
    public static final Integer POSTPONED_TO_IDLE = 50;
    public static final Integer FAILED_TO_IDLE = 0;
    public static final Integer MAX = 100;
    public static final Integer NEW = 80;
}
