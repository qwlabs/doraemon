package com.qwlabs.security;

import com.google.common.base.Suppliers;
import com.qwlabs.cdi.DispatchInstance;
import com.qwlabs.cdi.SafeCDI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Supplier;

@Builder
@AllArgsConstructor
public class AuthenticatedCaller implements Caller {
    private final String id;
    private final String type;
    private final Supplier<CallerAttributes> attributes = Suppliers.memoize(this::loadAttributes);
    private final Supplier<DispatchInstance<Caller, CallerPermissionsLoader>> permissionsLoader = Suppliers.memoize(this::loadPermissionsLoader);
    private final Supplier<CallerPermissions> permissions = Suppliers.memoize(this::loadPermissions);

    private CallerPermissions loadPermissions() {
        return permissionsLoader.get()
                .getOptional(this)
                .map(loader -> loader.permissions(this))
                .orElseGet(null);
    }

    private DispatchInstance<Caller, CallerPermissionsLoader> loadPermissionsLoader() {
        return DispatchInstance.of(SafeCDI.select(CallerPermissionsLoader.class).orElse(null));
    }

    private CallerAttributes loadAttributes() {
        return new CDICallerAttributes(this,
                SafeCDI.select(CallerAttributeLoader.class).orElse(null));
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
    public boolean authenticated() {
        return false;
    }
}
