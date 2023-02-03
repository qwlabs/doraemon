package com.qwlabs.cdi;

import io.quarkus.arc.ClientProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
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
            LOGGER.error("Load Contextual Instance error.", e);
            return Optional.empty();
        }
    }
}
