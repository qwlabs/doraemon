package com.qwlabs.cdi;


import jakarta.annotation.Nullable;

public interface Dispatchable<C> {
    boolean dispatchable(@Nullable C context);
}
