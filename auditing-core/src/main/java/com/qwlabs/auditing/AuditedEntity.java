package com.qwlabs.auditing;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
public class AuditedEntity extends CreatedAuditedEntity {
    private OffsetDateTime updatedAt;
    private String updatedBy;
}
