package com.qwlabs.graphql.builder.formatters;

import com.qwlabs.graphql.builder.GqlObject;
import com.qwlabs.graphql.builder.GqlRefArgument;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public final class GqlValueFormatters {
    private static final List<GqlValueFormatter> FORMATTERS = buildDefaultFormatters();

    public static void register(GqlValueFormatter formatter) {
        FORMATTERS.add(0, formatter);
    }

    public static String format(Object value, GqlFormatter gqlFormatter) {
        if (value == null) {
            return "null";
        }
        return FORMATTERS.stream()
                .filter(formatter -> formatter.support(value))
                .findFirst()
                .map(formatter -> formatter.format(value, gqlFormatter))
                .orElseThrow(() -> {
                    try {
                        return new InputMismatchException(String.format("Can not found variable:%s formatter.", value));
                    } catch (Exception e) {
                        return new InputMismatchException("Can not found variable formatter.");
                    }
                });
    }

    private static List<GqlValueFormatter> buildDefaultFormatters() {
        List<GqlValueFormatter> formatters = new ArrayList<>();
        formatters.add(new StringFormatter());
        formatters.add(new NumberFormatter());
        formatters.add(new BooleanFormatter());
        formatters.add(new GqlObjectFormatter());
        return formatters;
    }

    private static class NumberFormatter implements GqlValueFormatter {

        @Override
        public boolean support(Object value) {
            return value instanceof Number;
        }

        @Override
        public String format(Object value, GqlFormatter formatter) {
            return value.toString();
        }
    }

    private static class StringFormatter implements GqlValueFormatter {

        @Override
        public boolean support(Object value) {
            return value instanceof String;
        }

        @Override
        public String format(Object value, GqlFormatter formatter) {
            return (String) value;
        }
    }

    private static class BooleanFormatter implements GqlValueFormatter {

        @Override
        public boolean support(Object value) {
            return value instanceof Boolean;
        }

        @Override
        public String format(Object value, GqlFormatter formatter) {
            return value.toString();
        }
    }

    private static class GqlObjectFormatter implements GqlValueFormatter {

        @Override
        public boolean support(Object value) {
            return value instanceof GqlObject;
        }

        @Override
        public String format(Object value, GqlFormatter formatter) {
            return ((GqlObject) value).build(formatter);
        }
    }
}
