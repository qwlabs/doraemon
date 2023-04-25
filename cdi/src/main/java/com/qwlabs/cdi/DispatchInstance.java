package com.qwlabs.cdi;


import com.qwlabs.lang.F2;
import jakarta.annotation.Nullable;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class DispatchInstance<C, D> {
    private static final String NULL_CACHE_KEY = "NULL";
    private final Instance<D> instance;
    private final Map<Object, D> cachedInstance;
    private final Function<C, D> loader;

    private DispatchInstance(Instance<D> instance) {
        this(instance, false);
    }

    private DispatchInstance(Instance<D> instance, boolean enabledCache) {
        this.instance = instance;
        this.cachedInstance = enabledCache ? new ConcurrentHashMap<>() : null;
        this.loader = enabledCache ? this::doLoadFromCache : this::doLoad;
    }

    private boolean matches(D dispatcher, C context) {
        Optional<Dispatchable<C>> mayDispatcher = Arcs.contextualInstance(dispatcher);
        return mayDispatcher.map(d -> d.dispatchable(context)).orElseGet(() -> {
            LOGGER.warn("Class {} is not Dispatchable", dispatcher.getClass().getName());
            return false;
        });
    }

    private Stream<D> stream() {
        return Optional.ofNullable(this.instance).map(Instance::stream).orElseGet(Stream::empty);
    }

    protected Object buildCacheKey(C context) {
        return F2.ifPresent(context, Function.identity(), NULL_CACHE_KEY);
    }

    private D doLoadFromCache(final C context) {
        Object cacheKey = buildCacheKey(context);
        return cachedInstance.computeIfAbsent(cacheKey, key -> doLoad(context));
    }

    public D doLoad(C context) {
        D dispatcher = stream().filter(d -> matches(d, context)).findFirst().orElse(null);
        LOGGER.info("Loaded context:{} with dispatcher: {}", context, dispatcher);
        return dispatcher;
    }


    public Optional<D> getOptional(C context) {
        return Optional.ofNullable(loader.apply(context));
    }

    public D get(C context) {
        return getOptional(context).orElseThrow(() -> new RuntimeException("Instance not found."));
    }

    public Stream<D> stream(C context) {
        return stream().filter(dispatcher -> matches(dispatcher, context));
    }

    public static <C, D> DispatchInstance<C, D> of(@Nullable Instance<D> instance) {
        return new DispatchInstance<>(instance);
    }

    public static <C, D> DispatchInstance<C, D> of(@Nullable Instance<D> instance, boolean enabledCache) {
        return new DispatchInstance<>(instance, enabledCache);
    }

    public static <C, D> DispatchInstance<C, D> ofCache(@Nullable Instance<D> instance, Function<C, Object> keyFun) {
        return new DispatchInstance<>(instance, true) {
            @Override
            protected Object buildCacheKey(C context) {
                return keyFun.apply(context);
            }
        };
    }

}
