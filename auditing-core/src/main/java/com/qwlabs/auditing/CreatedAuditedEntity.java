package com.qwlabs.auditing;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
public class CreatedAuditedEntity {
    private OffsetDateTime createdAt;
    private String createdBy;
}
