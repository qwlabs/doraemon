package com.qwlabs.doraemon.lang;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class F2 {
    private F2() {
    }

    public static <E> void ifEmpty(E value, EmptyConsumer consumer) {
        if (value == null) {
            consumer.accept();
        }
    }

    public static <E> void ifPresent(E value, EmptyConsumer consumer) {
        if (value != null) {
            consumer.accept();
        }
    }

    public static <E> void ifPresent(E value, Consumer<E> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public static <E, R> R ifPresent(E value, Function<E, R> mapping) {
        return ifPresent(value, mapping, (R) null);
    }

    public static <E, R> R ifPresent(E value, Supplier<R> presentSupplier) {
        return ifPresent(value, presentSupplier, (R) null);
    }

    public static <E, R> R ifPresent(E value, Function<E, R> mapping, R defaultValue) {
        return Optional.ofNullable(value).map(mapping).orElse(defaultValue);
    }

    public static <E, R> R ifPresent(E value, Supplier<R> presentSupplier, R defaultValue) {
        return Optional.ofNullable(value).map(v -> presentSupplier.get()).orElse(defaultValue);
    }

    public static <E, R> R ifPresent(E value, Function<E, R> mapping, Supplier<R> defaultSupplier) {
        return Optional.ofNullable(value).map(mapping).orElseGet(defaultSupplier);
    }
}
