package com.qwlabs.test.builders;


import com.qwlabs.auditing.CreatedAuditedEntity;

import java.time.Instant;
import java.util.Optional;

@SuppressWarnings("checkstyle:VisibilityModifier")
public abstract class CreatedAuditedEntityBuilder<T extends CreatedAuditedEntity, B extends CreatedAuditedEntityBuilder<T, B>>
        extends BaseEntityBuilder<T> {
    protected String createdBy;
    protected Instant createdAt;

    protected abstract T preBuild();

    @Override
    public T build() {
        T t = this.preBuild();
        t.setCreatedAt(Optional.ofNullable(this.createdAt).orElseGet(this::now));
        t.setCreatedBy(Optional.ofNullable(this.createdBy).orElse(getAuditorId().orElse(null)));
        return t;
    }

    public B createdBy(String createdBy) {
        this.createdBy = createdBy;
        return (B) this;
    }

    public B createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return (B) this;
    }
}
