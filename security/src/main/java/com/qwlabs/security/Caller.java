package com.qwlabs.security;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

public interface Caller {
    @Nullable String id();

    @Nullable String type();

    default boolean is(String type){
        return Objects.equals(type, type());
    }

    @NotNull CallerAttributes attributes();

    default @Nullable <A> A attribute(String name) {
        return attributes().get(name);
    }

    @NotNull CallerPermissions permissions();

    default boolean isAnonymous(){
        return !authenticated();
    }

    boolean authenticated();

    static Caller load(SecurityIdentity securityIdentity) {
        return securityIdentity.getAttribute(Caller.class.getName());
    }

    static void set(QuarkusSecurityIdentity.Builder builder, Caller caller) {
        set(builder, caller, null);
    }

    static void set(QuarkusSecurityIdentity.Builder builder, Caller caller, @Nullable String scope) {
        var roles = Optional.ofNullable(scope)
                .map(s -> caller.permissions().raw(s))
                .orElseGet(() -> caller.permissions().raw());
        builder.addRoles(roles);
        builder.addAttribute(Caller.class.getName(), caller);
    }
}
