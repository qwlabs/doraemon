package com.qwlabs.cdi.dispatch;


import jakarta.annotation.Nullable;

public interface Dispatchable<C> {
    boolean dispatchable(@Nullable C context);
}
