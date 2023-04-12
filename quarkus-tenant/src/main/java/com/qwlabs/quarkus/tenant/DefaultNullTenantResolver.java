package com.qwlabs.quarkus.tenant;

import jakarta.enterprise.inject.Default;

@Default
public class DefaultNullTenantResolver implements DefaultTenantResolver {
    @Override
    public String get(Tenant tenant) {
        return null;
    }
}
