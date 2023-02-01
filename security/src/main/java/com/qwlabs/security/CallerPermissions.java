package com.qwlabs.security;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public interface CallerPermissions {

    @NotNull Set<@NotNull String> raw();

    @NotNull Set<@NotNull String> raw(@NotNull String scope);

    @NotNull boolean has(@NotNull String permission);

    @NotNull boolean has(@NotNull String scope, @NotNull String permission);
}
