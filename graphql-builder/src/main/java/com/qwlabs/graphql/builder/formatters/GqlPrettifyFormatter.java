package com.qwlabs.graphql.builder.formatters;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

public class GqlPrettifyFormatter implements GqlFormatter {
    private static final Map<Integer, String> PRETTIFY_LEVEL_CACHE = Maps.newConcurrentMap();

    @Override
    public String formatStartBraces(String braces) {
        return String.format(" %s\n", braces);
    }

    @Override
    public String formatEndBraces(String braces, int level) {
        return String.format("\n%s%s", getIndent(level), braces);
    }

    @Override
    public String formatColon(String colon) {
        return String.format("%s ", colon);
    }

    @Override
    public String formatEquals(String value) {
        return String.format(" %s ", value);
    }

    @Override
    public String getIndent(int level) {
        if (level <= 0) {
            return "";
        }
        return PRETTIFY_LEVEL_CACHE.computeIfAbsent(level, l -> Strings.repeat("\t", l));
    }

    @Override
    public String formatVariableDelimiter(String delimiter) {
        return String.format("%s ", delimiter);
    }

    @Override
    public String formatFieldDelimiter(String delimiter) {
        return String.format("%s\n", delimiter);
    }

    @Override
    public String formatFieldAlias(String alias) {
        return String.format("%s ", alias);
    }
}
