package com.qwlabs.quarkus.tenant;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public interface Tenant {
    @Nullable
    String tenantId();

    @Nullable
    <T> T attribute(@NotNull String name);

    @NotNull  TenantConfig config();

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
