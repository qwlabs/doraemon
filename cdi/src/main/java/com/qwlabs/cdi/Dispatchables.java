package com.qwlabs.cdi;

import io.quarkus.arc.ClientProxy;

public final class Dispatchables {
    private Dispatchables() {
    }

    public static <D> boolean isDispatchable(D dispatchable) {
        if (dispatchable instanceof Dispatchable) {
            return true;
        }
        if (!(dispatchable instanceof ClientProxy)) {
            return false;
        }
        return ((ClientProxy) dispatchable).arc_contextualInstance() instanceof Dispatchable;
    }
}
