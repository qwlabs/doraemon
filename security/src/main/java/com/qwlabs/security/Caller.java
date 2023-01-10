package com.qwlabs.security;

import io.quarkus.security.identity.SecurityIdentity;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public interface Caller {
    @Nullable String id();

    @Nullable String type();

    default boolean is(String type) {
        return Objects.equals(type, type());
    }

    @NotNull CallerAttributes attributes();

    default @Nullable <A> A attribute(String name) {
        return attributes().get(name);
    }

    @NotNull CallerPermissions permissions();

    @Nullable SecurityIdentity identity();

    default void identity(@Nullable SecurityIdentity identity) {
    }

    default boolean isAnonymous() {
        return !authenticated();
    }

    boolean authenticated();

    static Caller load(SecurityIdentity securityIdentity) {
        return securityIdentity.getAttribute(Caller.class.getName());
    }
}
