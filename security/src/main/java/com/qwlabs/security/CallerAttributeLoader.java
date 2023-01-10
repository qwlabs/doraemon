package com.qwlabs.security;

public interface CallerAttributeLoader<A> {
    default boolean cacheable(Caller caller) {
        return true;
    }

    A load(Caller caller);
}
