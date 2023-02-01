package com.qwlabs.q.formatters;

import com.qwlabs.q.conditions.QCondition;

import jakarta.validation.constraints.NotNull;

@FunctionalInterface
public interface QFormatter {
    @NotNull String format(@NotNull QCondition condition);
}
