package com.qwlabs.tq.models;

import java.time.Instant;
import java.util.Optional;


public interface TaskQueueRecord {
    String getId();

    void setId(String id);

    String getTopic();

    void setTopic(String topic);

    String getBucket();

    void setBucket(String bucket);

    Integer getPriority();

    void setPriority(Integer topic);

    <C> Optional<C> getContext(Class<C> clazz);

    void setContext(Object context);

    ProcessStatus getProcessStatus();

    void setProcessStatus(ProcessStatus processStatus);

    Instant getProcessStartAt();

    void setProcessStartAt(Instant processStartAt);

    Instant getProcessEndAt();

    void setProcessEndAt(Instant processEndAt);

    String getFailedMessage();

    void setFailedMessage(String failedMessage);
}
