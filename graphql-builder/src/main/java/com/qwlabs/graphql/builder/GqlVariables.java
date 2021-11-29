package com.qwlabs.graphql.builder;

import com.qwlabs.graphql.builder.formatters.GqlFormatter;
import com.qwlabs.lang.C2;

import java.util.List;
import java.util.StringJoiner;

public final class GqlVariables {
    private List<GqlVariable> variables;

    protected GqlVariables(List<GqlVariable> variables) {
        this.variables = variables;
    }

    public String build(GqlFormatter formatter) {
        if (C2.isEmpty(this.variables)) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(
                formatter.formatVariableDelimiter(","),
                formatter.formatStartVariables("("),
                formatter.formatEndVariables(")"));
        this.variables.forEach(var -> joiner.add(var.build(formatter)));
        return joiner.toString();
    }
}