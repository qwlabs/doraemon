package com.qwlabs.quarkus.tenant;

public interface Tenant {
    String tenantId();

    <T> T attribute(String name);

    TenantConfig config();

    default boolean enabled() {
        return config().enabled();
    }

    default boolean isMulti() {
        return enabled();
    }

    default boolean isSingle() {
        return !enabled();
    }
}
