package com.qwlabs.security;

public interface CallerAttributeLoader<A, C> {
    default boolean cacheable(Caller caller) {
        return true;
    }

    A load(Caller caller, C context);
}
