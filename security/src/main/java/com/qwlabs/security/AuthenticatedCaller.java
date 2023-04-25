package com.qwlabs.security;

import com.google.common.base.Suppliers;
import com.qwlabs.cdi.DispatchInstance;
import io.quarkus.security.identity.SecurityIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.annotation.Nullable;

import java.util.function.Supplier;

@Builder
@AllArgsConstructor
public class AuthenticatedCaller implements Caller {
    private final String id;
    private final String type;
    private SecurityIdentity identity;
    private final DispatchInstance<String, CallerAttributeLoader<?, ?>> attributeLoader;
    private final DispatchInstance<Caller, CallerPermissionsLoader> permissionsLoader;
    private final Supplier<CallerAttributes> attributes = Suppliers.memoize(this::loadAttributes);
    private final Supplier<CallerPermissions> permissions = Suppliers.memoize(this::loadPermissions);

    private CallerPermissions loadPermissions() {
        return permissionsLoader
                .getOptional(this)
                .map(loader -> loader.permissions(this))
                .orElseGet(null);
    }

    private CallerAttributes loadAttributes() {
        return new CDICallerAttributes(this, attributeLoader);
    }

    @Override
    public @Nullable String id() {
        return id;
    }

    @Override
    public @Nullable String type() {
        return type;
    }

    @Override
    public CallerAttributes attributes() {
        return attributes.get();
    }

    @Override
    public CallerPermissions permissions() {
        return permissions.get();
    }

    @Override
    public @Nullable SecurityIdentity identity() {
        return identity;
    }

    @Override
    public void identity(@Nullable SecurityIdentity identity) {
        this.identity = identity;
    }

    @Override
    public boolean authenticated() {
        return true;
    }
}
