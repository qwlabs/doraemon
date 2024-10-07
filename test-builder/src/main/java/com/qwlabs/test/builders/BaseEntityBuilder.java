package com.qwlabs.test.builders;

import com.qwlabs.cdi.SafeCDI;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.inject.Instance;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Optional;

public abstract class BaseEntityBuilder<T> implements EntityBuilder<T> {

    @Transactional
    @Override
    public T persist() {
        T entity = this.build();
        getEntityManager().ifPresent(em -> em.persist(entity));
        return entity;
    }

    protected OffsetDateTime now() {
        return OffsetDateTime.now();
    }

    protected Optional<EntityManager> getEntityManager() {
        return SafeCDI.select(EntityManager.class)
            .map(Instance::get);
    }

    protected Optional<String> getAuditorId() {
        return SafeCDI.select(SecurityIdentity.class)
            .map(Instance::get)
            .filter(identity -> !identity.isAnonymous())
            .map(SecurityIdentity::getPrincipal)
            .map(Principal::getName);
    }

}
