package com.qwlabs.cdi;

public interface Dispatchable<C> {
    boolean dispatchable(C context);
}