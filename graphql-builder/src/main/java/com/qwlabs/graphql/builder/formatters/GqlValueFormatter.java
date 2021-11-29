package com.qwlabs.graphql.builder.formatters;

import javax.validation.constraints.NotNull;

public interface GqlValueFormatter {
    boolean support(@NotNull Object value);

    @NotNull String format(@NotNull Object value, GqlFormatter formatter);
}
