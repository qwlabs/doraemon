package com.qwlabs.quarkus.tenant;

@FunctionalInterface
public interface DefaultTenantResolver {
    String get(Tenant tenant);
}
