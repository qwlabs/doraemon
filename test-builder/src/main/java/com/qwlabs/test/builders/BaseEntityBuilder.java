package com.qwlabs.test.builders;

import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

public abstract class BaseEntityBuilder<T> implements EntityBuilder<T> {

    @Transactional
    @Override
    public T persist() {
        T entity = this.build();
        getEntityManager().ifPresent(em->em.persist(entity));
        return entity;
    }


    protected Instant now() {
        return Instant.now();
    }

    protected Optional<EntityManager> getEntityManager() {
        return CDI.current().select(EntityManager.class)
                .stream().findAny();
    }

    protected Optional<String> getAuditorId() {
//        TODO use caller
        SecurityIdentity identity = CDI.current().select(SecurityIdentity.class).get();
        if (identity.isAnonymous()) {
            return Optional.empty();
        }
        return Optional.ofNullable(identity.getPrincipal().getName());
    }

}
