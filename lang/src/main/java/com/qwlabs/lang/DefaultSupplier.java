package com.qwlabs.lang;

import java.util.Objects;

@FunctionalInterface
public interface DefaultSupplier<R> {
    R get();

    default DefaultSupplier<R> then(DefaultSupplier<R> after) {
        Objects.requireNonNull(after);
        return () -> {
            R r = get();
            return r == null ? after.get() : r;
        };
    }
}
