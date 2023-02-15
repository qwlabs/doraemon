package com.qwlabs.quarkus.tenant;

public interface TenantAttributeResolver<T> {
    T resolve(Tenant tenant);
}
