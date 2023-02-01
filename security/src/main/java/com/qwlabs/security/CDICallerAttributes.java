package com.qwlabs.security;

import com.google.common.collect.Maps;
import com.qwlabs.cdi.DispatchInstance;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class CDICallerAttributes implements CallerAttributes {
    private final Caller caller;
    private final Map<String, Object> cachedAttributes;
    private final DispatchInstance<String, CallerAttributeLoader<?, ?>> loaders;

    public CDICallerAttributes(Caller caller, @NotNull DispatchInstance<String, CallerAttributeLoader<?, ?>> loader) {
        this.caller = caller;
        this.cachedAttributes = Maps.newConcurrentMap();
        this.loaders = loader;
    }

    @Override
    public <A, C> A get(String name, C context) {
        return loaders.getOptional(name)
                .map(loader -> this.get(name, (CallerAttributeLoader<A, C>) loader, context))
                .orElse(null);
    }

    private <A, C> A get(String name, CallerAttributeLoader<A, C> loader, C context) {
        if (!loader.cacheable(caller)) {
            return loader.load(caller, context);
        }
        return (A) cachedAttributes.computeIfAbsent(name, key -> loader.load(caller, context));
    }
}
