package com.qwlabs.doraemon.lang;

import java.util.function.Consumer;

public final class S2 {
    private S2() {
    }

    public static boolean isEmpty(String value) {
        return (value == null || "".equals(value));
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isBlank(String value) {
        return value == null || isEmpty(value.trim());
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static void ifEmpty(String value, EmptyConsumer consumer) {
        if (isEmpty(value)) {
            consumer.accept();
        }
    }

    public static void ifNotEmpty(String value, EmptyConsumer consumer) {
        if (isNotEmpty(value)) {
            consumer.accept();
        }
    }

    public static void ifNotEmpty(String value, Consumer<String> consumer) {
        if (isNotEmpty(value)) {
            consumer.accept(value);
        }
    }

    public static void ifBlank(String value, EmptyConsumer consumer) {
        if (isBlank(value)) {
            consumer.accept();
        }
    }

    public static void ifNotBlank(String value, EmptyConsumer consumer) {
        if (isNotBlank(value)) {
            consumer.accept();
        }
    }

    public static void ifNotBlank(String value, Consumer<String> consumer) {
        if (isNotBlank(value)) {
            consumer.accept(value);
        }
    }
}
