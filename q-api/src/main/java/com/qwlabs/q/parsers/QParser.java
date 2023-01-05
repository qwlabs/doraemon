package com.qwlabs.q.parsers;

import com.qwlabs.q.conditions.QCondition;

import javax.validation.constraints.NotNull;

@FunctionalInterface
public interface QParser {
    @NotNull QCondition parse(@NotNull String query);
}
