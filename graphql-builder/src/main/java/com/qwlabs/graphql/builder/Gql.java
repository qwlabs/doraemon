package com.qwlabs.graphql.builder;

import com.qwlabs.graphql.builder.formatters.GqlCompressedFormatter;
import com.qwlabs.graphql.builder.formatters.GqlFormatter;
import com.qwlabs.graphql.builder.formatters.GqlPrettifyFormatter;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public final class Gql implements GqlVariablesAware<Gql>, GqlFieldsAware<Gql> {
    private static final String QUERY = "query";
    private static final String MUTATION = "mutation";
    private static final String SUBSCRIPTION = "subscription";
    private final String type;
    private final String name;
    private GqlVariables variables;
    private GqlFields fields;

    private Gql(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Gql vars(@NotNull GqlVariables variables) {
        this.variables = variables;
        return this;
    }

    public Gql fields(GqlFields fields) {
        this.fields = fields;
        return this;
    }

    public String build() {
        return build(prettifyFormatter());
    }

    public String build(@NotNull GqlFormatter formatter) {
        if (this.fields == null || this.fields.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one root field.");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(formatter.formatType(this.type))
                .append(" ")
                .append(this.name);
        Optional.ofNullable(this.variables)
                .ifPresent(vars -> builder.append(vars.build(formatter)));
        builder.append(this.fields.build(formatter, 0));
        return builder.toString();
    }


    public static Gql query(@NotNull String name) {
        return new Gql(QUERY, name);
    }

    public static Gql mutation(@NotNull String name) {
        return new Gql(MUTATION, name);
    }

    public static Gql subscription(@NotNull String name) {
        return new Gql(SUBSCRIPTION, name);
    }


    public static GqlFormatter compressedFormatter() {
        return new GqlCompressedFormatter();
    }

    public static GqlFormatter prettifyFormatter() {
        return new GqlPrettifyFormatter();
    }

    public static GqlVariable var(@NotNull String name, @NotNull String type) {
        return new GqlVariable(name, type);
    }

    public static GqlField field(@NotNull String name) {
        return new GqlField(name);
    }

    public static GqlArgument arg(@NotNull String name, Object value) {
        return new GqlArgument(name, value);
    }

    public static GqlObject obj(@NotNull String name, Object value) {
        return new GqlObject(name, value);
    }

    public static GqlRefArgument ref(@NotNull String ref) {
        return new GqlRefArgument(ref);
    }

    public static GqlField relayNode(){
        return new GqlField("edges").fields("node");
    }
}