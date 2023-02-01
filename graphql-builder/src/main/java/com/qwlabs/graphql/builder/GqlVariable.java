package com.qwlabs.graphql.builder;


import com.qwlabs.graphql.builder.formatters.GqlFormatter;
import com.qwlabs.graphql.builder.formatters.GqlValueFormatters;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;

public final class GqlVariable {
    private final String name;
    private final String type;
    private Object defaultValue;
    private boolean required;

    public GqlVariable(@NotNull String name,
                       @NotNull String type) {
        this.name = name;
        this.type = type;
    }

    public GqlVariable defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public GqlVariable required() {
        return required(true);
    }

    public GqlVariable nonRequired() {
        return required(false);
    }

    public GqlVariable required(boolean required) {
        this.required = required;
        return this;
    }

    public String build(GqlFormatter formatter) {
        StringBuilder builder = new StringBuilder();
        builder.append("$").append(this.name)
                .append(formatter.formatColon(":"))
                .append(this.type);
        if (this.required) {
            builder.append("!");
        }
        Optional.ofNullable(this.defaultValue)
                .ifPresent(dv -> builder.append(formatter.formatEquals("="))
                        .append(GqlValueFormatters.format(this.defaultValue, formatter)));
        return builder.toString();
    }
}