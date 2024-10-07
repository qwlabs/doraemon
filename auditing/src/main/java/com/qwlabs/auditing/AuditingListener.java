package com.qwlabs.auditing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Clock;
import java.time.OffsetDateTime;
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
            touchUpdated((AuditedEntity) entity, false);
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof AuditedEntity) {
            touchUpdated((AuditedEntity) entity, true);
        }
    }

    private static void touchCreated(CreatedAuditedEntity entity) {
        entity.setCreatedAt(Optional.ofNullable(entity.getCreatedAt())
                .orElseGet(() -> OffsetDateTime.now(Clock.systemUTC())));
        entity.setCreatedBy(Optional.ofNullable(entity.getCreatedBy())
                .orElseGet(() -> getAuditorId(entity).orElse(null)));
    }

    private static void touchUpdated(AuditedEntity entity, boolean force) {
        entity.setUpdatedAt(OffsetDateTime.now(Clock.systemUTC()));
        entity.setUpdatedBy(Optional.ofNullable(entity.getUpdatedBy())
            .filter(by -> !force)
            .orElseGet(() -> getAuditorId(entity).orElse(null)));
    }

    private static Optional<String> getAuditorId(Object entity) {
        return getAuditorIdProvider().get(entity);
    }

    private static AuditorIdProvider getAuditorIdProvider() {
        Instance<AuditorIdProvider> instance = CDI.current().select(AuditorIdProvider.class);
        return instance.stream().findFirst().orElse(DEFAULT_AUDITOR_ID_PROVIDER);
    }

    private static class DefaultAuditorIdProvider implements AuditorIdProvider {
        @Override
        public Optional<String> get(Object entity) {
            return Optional.empty();
        }
    }
}
