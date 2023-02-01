package com.qwlabs.auditing;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingListener.class)
public class CreatedAuditedEntity {
    private Instant createdAt;
    private String createdBy;
}
