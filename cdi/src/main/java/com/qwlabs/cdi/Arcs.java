package com.qwlabs.cdi;

import io.quarkus.arc.ClientProxy;

import java.util.Optional;

public final class Arcs {
    private Arcs() {
    }

    public static <T> Optional<T> contextualInstance(Object obj) {
        Object real = obj;
        if (obj instanceof ClientProxy) {
            real = ((ClientProxy) obj).arc_contextualInstance();
        }
        try {
            return Optional.ofNullable((T) real);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }
}
