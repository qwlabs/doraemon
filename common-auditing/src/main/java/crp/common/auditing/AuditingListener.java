package crp.common.auditing;

import io.quarkus.arc.Unremovable;
import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Unremovable
@Dependent
public class AuditingListener {
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
        SecurityIdentity identity = CDI.current().select(SecurityIdentity.class).get();
        if (identity.isAnonymous()) {
            return Optional.empty();
        }
        return Optional.ofNullable(identity.getPrincipal().getName());
    }
}
