package com.qwlabs.security;

public interface CallerAttributes {
    default <A> A get(String name) {
        return get(name, null);
    }

    <A, C> A get(String name, C context);
}
