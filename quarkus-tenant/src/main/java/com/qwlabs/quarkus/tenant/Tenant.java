package com.qwlabs.quarkus.tenant;

import com.google.common.base.Strings;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public interface Tenant {

    @Nullable
    default boolean isEmpty() {
        return Strings.isNullOrEmpty(tenantId());
    }
    
    @Nullable
    default boolean isPresent() {
        return !isEmpty();
    }

    @Nullable
    String tenantId();

    @Nullable
    <T> T attribute(@NotNull String name);

    @NotNull TenantConfig config();

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
