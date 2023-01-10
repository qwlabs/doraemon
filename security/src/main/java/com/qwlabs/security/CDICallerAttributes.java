package com.qwlabs.security;

import com.google.common.collect.Maps;
import com.qwlabs.cdi.DispatchInstance;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class CDICallerAttributes implements CallerAttributes {
    private final Caller caller;
    private final Map<String, Object> cachedAttributes;
    private final DispatchInstance<String, CallerAttributeLoader<?>> loaders;

    public CDICallerAttributes(Caller caller, @NotNull DispatchInstance<String, CallerAttributeLoader<?>> loader) {
        this.caller = caller;
        this.cachedAttributes = Maps.newConcurrentMap();
        this.loaders = loader;
    }

    @Override
    public <A> A get(String name) {
        return loaders.getOptional(name)
                .map(loader -> this.get(name, (CallerAttributeLoader<A>) loader))
                .orElse(null);
    }

    private <A> A get(String name, CallerAttributeLoader<A> loader) {
        if (!loader.cacheable(caller)) {
            return loader.load(caller);
        }
        return (A) cachedAttributes.computeIfAbsent(name, key -> loader.load(caller));
    }
}
