package com.qwlabs.ff;

import com.qwlabs.lang.EmptyConsumer;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotNull;
import java.util.function.Supplier;

public interface FeatureFlag {
    @NotNull String feature();

    boolean enabled(@Nullable String topic);

    default boolean enabled() {
        return enabled(null);
    }

    default boolean disabled(@Nullable String topic) {
        return !enabled(topic);
    }

    default void ifEnabled(@Nullable String topic, @NotNull EmptyConsumer consumer) {
        if (enabled(topic)) {
            consumer.accept();
        }
    }

    default void ifEnabled(@NotNull EmptyConsumer consumer) {
        ifEnabled(null, consumer);
    }

    default void ifDisabled(@Nullable String topic, @NotNull EmptyConsumer consumer) {
        if (disabled(topic)) {
            consumer.accept();
        }
    }

    default void ifDisabled(@NotNull EmptyConsumer consumer) {
        ifDisabled(null, consumer);
    }

    default <E> E getIfEnabled(@Nullable String topic, @NotNull Supplier<E> supplier) {
        return enabled(topic) ? supplier.get() : null;
    }

    default <E> E getIfEnabled(@NotNull Supplier<E> supplier) {
        return getIfEnabled(null, supplier);
    }

    default <E> E getIfDisabled(@Nullable String topic, @NotNull Supplier<E> supplier) {
        return disabled(topic) ? supplier.get() : null;
    }

    default <E> E getIfDisabled(Supplier<E> supplier) {
        return getIfDisabled(null, supplier);
    }
}