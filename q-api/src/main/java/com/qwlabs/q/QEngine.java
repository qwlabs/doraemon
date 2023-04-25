package com.qwlabs.q;

import com.qwlabs.q.conditions.QCondition;
import jakarta.annotation.Nullable;

import jakarta.validation.constraints.NotNull;

public interface QEngine {
    @Nullable String format(@NotNull String dialect, @Nullable QCondition condition);

    @NotNull QCondition parse(@NotNull String dialect, @Nullable String query);
}
