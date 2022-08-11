package com.qwlabs.cdi;


import lombok.extern.slf4j.Slf4j;


import javax.enterprise.inject.Instance;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public final class DispatchInstance<C, D> {
    private final Instance<D> instance;

    private DispatchInstance(Instance<D> instance) {
        this.instance = instance;
    }

    private boolean matches(D dispatcher, C context) {
        if (!(dispatcher instanceof Dispatchable)) {
            LOGGER.warn("Class {} is not Dispatchable", dispatcher.getClass().getName());
            return false;
        }
        return ((Dispatchable<C>) dispatcher).dispatchable(context);
    }

    public Optional<D> getOptional(C context) {
        return this.instance.stream()
                .filter(dispatcher -> matches(dispatcher, context))
                .findFirst();
    }

    public D get(C context) {
        return getOptional(context)
                .orElseThrow(() -> new RuntimeException("Instance not found."));
    }

    public static <C, D> DispatchInstance<C, D> of(Instance<D> instance) {
        return new DispatchInstance<>(instance);
    }

    public Stream<D> stream(C context) {
        return this.instance.stream()
                .filter(dispatcher -> matches(dispatcher, context));
    }
}