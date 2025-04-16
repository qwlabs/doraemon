package com.qwlabs.cdi;

import jakarta.annotation.Nullable;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DispatchInstance<C, D> {
    private final Instance<D> instance;

    private DispatchInstance(Instance<D> instance) {
        this.instance = instance;
    }

    private boolean matches(D dispatcher, C context) {
        try {
            return ((Dispatchable<C>) dispatcher).dispatchable(context);
        } catch (ClassCastException e) {
            LOGGER.warn("Class {} is not Dispatchable", dispatcher.getClass().getName());
            return false;
        }
    }

    public Optional<D> getOptional(C context) {
        return instance.stream().filter(dispatcher -> this.matches(dispatcher, context)).findFirst();
    }

    public D get(C context) {
        return getOptional(context).orElseThrow(() -> new RuntimeException("Instance not found."));
    }

    public static <C, D> DispatchInstance<C, D> of(@Nullable Instance<D> instance) {
        return new DispatchInstance<>(instance);
    }
}
