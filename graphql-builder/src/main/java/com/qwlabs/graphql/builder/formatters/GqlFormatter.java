package com.qwlabs.graphql.builder.formatters;

import jakarta.validation.constraints.NotNull;

@SuppressWarnings("checkstyle:MethodName")
public interface GqlFormatter {

    default @NotNull String formatType(@NotNull String type) {
        return type;
    }

    default String formatFieldAlias(String alias) {
        return alias;
    }

    default @NotNull String formatFieldName(@NotNull String name) {
        return name;
    }

    default @NotNull String formatStartBraces(@NotNull String braces) {
        return braces;
    }

    default @NotNull String formatEndBraces(@NotNull String braces, int level) {
        return braces;
    }

    default @NotNull String formatColon(@NotNull String colon) {
        return colon;
    }

    default @NotNull String formatEquals(@NotNull String value) {
        return value;
    }


    default String getIndent(int level) {
        return "";
    }

    default String formatVariableDelimiter(String delimiter) {
        return delimiter;
    }

    default String formatFieldDelimiter(String delimiter) {
        return delimiter;
    }

    default String formatStartVariables(String value) {
        return value;
    }

    default String formatEndVariables(String value) {
        return value;
    }

}
