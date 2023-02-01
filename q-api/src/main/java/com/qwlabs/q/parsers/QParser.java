package com.qwlabs.q.parsers;

import com.qwlabs.q.conditions.QCondition;

import jakarta.validation.constraints.NotNull;

@FunctionalInterface
public interface QParser {
    @NotNull QCondition parse(@NotNull String query);
}
