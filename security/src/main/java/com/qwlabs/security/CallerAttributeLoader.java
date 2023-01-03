package com.qwlabs.security;

public interface CallerAttributeLoader<A> {
    boolean cacheable(Caller caller);
    A load(Caller caller);
}
