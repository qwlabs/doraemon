package com.qwlabs.lang;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class WithExpiring<T> {
    private final T data;
    private final long expiresInNanos;

    private WithExpiring(T data, long expiresInNanos) {
        this.data = data;
        this.expiresInNanos = expiresInNanos;
    }

    public static <T> WithExpiring<T> of(T data, long expiresIn, TimeUnit timeUnit) {
        return new WithExpiring<>(data, timeUnit.toNanos(expiresIn));
    }

    public static <T> WithExpiring<T> of(T data, long expiresInNanos) {
        return new WithExpiring<>(data, expiresInNanos);
    }
}
