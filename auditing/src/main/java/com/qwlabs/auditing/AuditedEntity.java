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
public class AuditedEntity extends CreatedAuditedEntity {
    private Instant updatedAt;
    private String updatedBy;
}
