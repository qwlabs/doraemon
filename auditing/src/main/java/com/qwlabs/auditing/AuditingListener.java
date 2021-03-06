package com.qwlabs.auditing;

import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
public class AuditingListener {
    private static final AuditorIdProvider DEFAULT_AUDITOR_ID_PROVIDER = new DefaultAuditorIdProvider();

    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof CreatedAuditedEntity) {
            touchCreated((CreatedAuditedEntity) entity);
        }
        if (entity instanceof AuditedEntity) {
            touchUpdated((AuditedEntity) entity);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof AuditedEntity) {
            touchUpdated((AuditedEntity) entity);
        }
    }

    private static void touchCreated(CreatedAuditedEntity entity) {
        entity.setCreatedAt(Optional.ofNullable(entity.getCreatedAt())
                .orElseGet(() -> Instant.now(Clock.systemUTC())));
        entity.setCreatedBy(Optional.ofNullable(entity.getCreatedBy())
                .orElseGet(() -> getAuditorId().orElse(null)));
    }

    private static void touchUpdated(AuditedEntity entity) {
        entity.setUpdatedAt(Instant.now(Clock.systemUTC()));
        entity.setUpdatedBy(Optional.ofNullable(entity.getUpdatedBy())
                .orElseGet(() -> getAuditorId().orElse(null)));
    }

    private static Optional<String> getAuditorId() {
        return getAuditorIdProvider().get();
    }

    private static AuditorIdProvider getAuditorIdProvider() {
        Instance<AuditorIdProvider> instance = CDI.current().select(AuditorIdProvider.class);
        return instance.stream().findFirst().orElse(DEFAULT_AUDITOR_ID_PROVIDER);
    }

    private static class DefaultAuditorIdProvider implements AuditorIdProvider {
        @Override
        public Optional<String> get() {
            SecurityIdentity identity = CDI.current().select(SecurityIdentity.class).get();
            if (identity.isAnonymous()) {
                return Optional.empty();
            }
            return Optional.ofNullable(identity.getPrincipal().getName());
        }
    }
}
