package com.qwlabs.graphql.builder;

import com.qwlabs.graphql.builder.formatters.GqlFormatter;
import com.qwlabs.graphql.builder.formatters.GqlValueFormatters;

import javax.validation.constraints.NotNull;

public class GqlArgument {
    private final String name;
    private final Object value;

    protected GqlArgument(@NotNull String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String build(GqlFormatter formatter) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name)
                .append(formatter.formatColon(":"))
                .append(GqlValueFormatters.format(this.value, formatter));
        return builder.toString();
    }
}
