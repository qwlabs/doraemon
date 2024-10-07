package com.qwlabs.test.builders;

import com.qwlabs.auditing.AuditedEntity;

import java.time.OffsetDateTime;
import java.util.Optional;


@SuppressWarnings("checkstyle:VisibilityModifier")
public abstract class AuditedEntityBuilder<T extends AuditedEntity, B extends AuditedEntityBuilder<T, B>> extends BaseEntityBuilder<T> {

    protected String createdBy;
    protected OffsetDateTime createdAt;
    protected String updatedBy;
    protected OffsetDateTime updatedAt;

    protected abstract T preBuild();

    @Override
    public T build() {
        T t = this.preBuild();
        t.setCreatedAt(Optional.ofNullable(this.createdAt).orElseGet(this::now));
        t.setUpdatedAt(Optional.ofNullable(this.updatedAt).orElseGet(this::now));
        t.setCreatedBy(Optional.ofNullable(this.createdBy).orElse(getAuditorId().orElse(null)));
        t.setUpdatedBy(Optional.ofNullable(this.updatedBy).orElse(getAuditorId().orElse(null)));
        return t;
    }

    public B createdBy(String createdBy) {
        this.createdBy = createdBy;
        return (B) this;
    }

    public B createdAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return (B) this;
    }


    public B updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return (B) this;
    }

    public B updatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return (B) this;
    }
}
