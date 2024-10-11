package com.qwlabs.auditing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.PreUpsertEvent;
import org.hibernate.event.spi.PreUpsertEventListener;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Objects;

@ApplicationScoped
public class AuditedEventListener implements PreUpdateEventListener, PreUpsertEventListener, PreInsertEventListener {
    private final AuditorIdResolver auditorIdResolver;

    @Inject
    public AuditedEventListener(Instance<AuditorIdResolver> auditorIdResolvers) {
        this.auditorIdResolver = DefaultAuditorIdResolver.orDefault(auditorIdResolvers);
    }

    @Override
    public boolean onPreUpsert(PreUpsertEvent preUpsertEvent) {
        handle(preUpsertEvent.getEntity(), true);
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent preInsertEvent) {
        handle(preInsertEvent.getEntity(), false);
        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent preUpdateEvent) {
        handle(preUpdateEvent.getEntity(), true);
        return false;
    }

    private void handle(Object entity, boolean force) {
        if (!(entity instanceof AuditedEntity audited)) {
            return;
        }
        if (force || Objects.isNull(audited.getUpdatedAt())) {
            audited.setUpdatedAt(OffsetDateTime.now(Clock.systemUTC()));
        }
        if (force || Objects.isNull(audited.getUpdatedBy())) {
            audited.setUpdatedBy(auditorIdResolver.resolve(entity).orElse(null));
        }
    }

}
