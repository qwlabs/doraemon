package com.qwlabs.graphql.builder;

import com.qwlabs.graphql.builder.formatters.GqlFormatter;
import com.qwlabs.lang.C2;

import java.util.List;
import java.util.StringJoiner;

public class GqlArguments {
    private List<GqlArgument> arguments;

    protected GqlArguments(List<GqlArgument> arguments) {
        this.arguments = arguments;
    }

    public String build(GqlFormatter formatter) {
        if (C2.isEmpty(this.arguments)) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(
                formatter.formatVariableDelimiter(","),
                formatter.formatStartVariables("("),
                formatter.formatEndVariables(")"));
        this.arguments.forEach(var -> joiner.add(var.build(formatter)));
        return joiner.toString();
    }
}
