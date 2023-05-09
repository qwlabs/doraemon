package com.qwlabs.security;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public interface CallerPermissions {

    @NotNull Set<@NotNull String> raw();

    @NotNull Set<@NotNull String> raw(@Nullable String scope);

    @NotNull boolean has(@NotNull String permission);

    @NotNull boolean has(@Nullable String scope, @NotNull String permission);

    @NotNull GrantTargets targets(@NotNull String permission, @NotNull String targetType);
}
