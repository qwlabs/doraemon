package com.qwlabs.quarkus.tenant;

public interface TenantAttributeResolver<T> {
    default boolean resolvable(Tenant tenant, String name) {
        return true;
    }

    T resolve(Tenant tenant);
}
