package com.qwlabs.cdi;


import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.enterprise.inject.Instance;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public final class DispatchInstance<C, D> {
    private final Instance<D> instance;
    private final Map<C, D> cachedInstance;
    private final Function<C, D> loader;

    private DispatchInstance(Instance<D> instance) {
        this(instance, false);
    }

    private DispatchInstance(Instance<D> instance, boolean enabledCache) {
        this.instance = instance;
        this.cachedInstance = enabledCache ? new ConcurrentHashMap<>() : null;
        this.loader = enabledCache
                ? (context) -> cachedInstance.computeIfAbsent(context, this::doLoad)
                : this::doLoad;
    }

    private boolean matches(D dispatcher, C context) {
        if (Dispatchables.isDispatchable(dispatcher)) {
            return ((Dispatchable<C>) dispatcher).dispatchable(context);
        }
        LOGGER.warn("Class {} is not Dispatchable", dispatcher.getClass().getName());
        return false;
    }

    private Stream<D> stream() {
        return Optional.ofNullable(this.instance)
                .map(Instance::stream)
                .orElseGet(Stream::empty);
    }

    public D doLoad(C context) {
        return stream()
                .filter(dispatcher -> matches(dispatcher, context))
                .findFirst()
                .orElse(null);
    }


    public Optional<D> getOptional(C context) {
        return Optional.ofNullable(loader.apply(context));
    }

    public D get(C context) {
        return getOptional(context)
                .orElseThrow(() -> new RuntimeException("Instance not found."));
    }

    public Stream<D> stream(C context) {
        return stream().filter(dispatcher -> matches(dispatcher, context));
    }

    public static <C, D> DispatchInstance<C, D> of(@Nullable Instance<D> instance) {
        return new DispatchInstance<>(instance);
    }

    public static <C, D> DispatchInstance<C, D> ofCache(@Nullable Instance<D> instance) {
        return new DispatchInstance<>(instance, true);
    }

}
