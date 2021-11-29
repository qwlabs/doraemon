package com.qwlabs.lang;

import java.util.Objects;

@FunctionalInterface
public interface EmptyConsumer {

    void accept();

    default EmptyConsumer andThen(EmptyConsumer after) {
        Objects.requireNonNull(after);
        return () -> {
            accept();
            after.accept();
        };
    }
}
