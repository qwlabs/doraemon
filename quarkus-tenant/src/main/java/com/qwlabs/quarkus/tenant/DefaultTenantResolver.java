package com.qwlabs.quarkus.tenant;

public interface DefaultTenantResolver {
    String get(Tenant tenant);
}
