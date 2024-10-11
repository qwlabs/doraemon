package com.qwlabs.auditing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpsertEvent;
import org.hibernate.event.spi.PreUpsertEventListener;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class CreatedAuditedEventListener implements PreUpsertEventListener, PreInsertEventListener {
    private final AuditorIdResolver auditorIdResolver;

    @Inject
    public CreatedAuditedEventListener(Instance<AuditorIdResolver> auditorIdResolvers) {
        this.auditorIdResolver = DefaultAuditorIdResolver.orDefault(auditorIdResolvers);
    }

    @Override
    public boolean onPreUpsert(PreUpsertEvent preUpsertEvent) {
        handle(preUpsertEvent.getEntity());
        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent preInsertEvent) {
        handle(preInsertEvent.getEntity());
        return false;
    }

    private void handle(Object entity) {
        if (!(entity instanceof CreatedAuditedEntity created)) {
            return;
        }
        created.setCreatedAt(Optional.ofNullable(created.getCreatedAt())
            .orElseGet(() -> OffsetDateTime.now(Clock.systemUTC())));
        created.setCreatedBy(Optional.ofNullable(created.getCreatedBy())
            .orElseGet(() -> auditorIdResolver.resolve(created).orElse(null)));
    }

}
