package com.qwlabs.q;

import com.qwlabs.q.conditions.QCondition;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotNull;

public interface QEngine {
    @Nullable String format(@NotNull String dialect, @Nullable QCondition condition);

    @NotNull QCondition parse(@NotNull String dialect, @Nullable String query);
}