package com.qwlabs.cdi;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface Dispatchable<C> {
    boolean dispatchable(@Nullable C context);
}
