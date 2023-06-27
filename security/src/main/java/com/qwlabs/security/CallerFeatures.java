package com.qwlabs.security;

import com.qwlabs.lang.C2;
import jakarta.validation.constraints.NotNull;

public interface CallerFeatures {
    @NotNull boolean has(@NotNull String feature);

    default @NotNull boolean hasAny(@NotNull String... features) {
        return C2.stream(features).anyMatch(this::has);
    }

    default @NotNull boolean hasAll(@NotNull String... features) {
        return C2.stream(features).allMatch(this::has);
    }
}
