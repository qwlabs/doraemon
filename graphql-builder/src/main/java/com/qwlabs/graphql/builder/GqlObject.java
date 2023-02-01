package com.qwlabs.graphql.builder;

import com.qwlabs.graphql.builder.formatters.GqlFormatter;
import com.qwlabs.graphql.builder.formatters.GqlValueFormatters;

import jakarta.validation.constraints.NotNull;
import java.util.StringJoiner;

public class GqlObject {
    private final String name;
    private final Object value;

    protected GqlObject(@NotNull String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String build(GqlFormatter formatter) {
        StringJoiner joiner = new StringJoiner(
                formatter.formatColon(":"),
                "{", "}");
        joiner.add(this.name).add(GqlValueFormatters.format(this.value, formatter));
        return joiner.toString();
    }
}
