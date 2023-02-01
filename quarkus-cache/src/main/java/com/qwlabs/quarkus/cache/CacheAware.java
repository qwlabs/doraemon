package com.qwlabs.quarkus.cache;

import com.qwlabs.cdi.SafeCDI;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.runtime.noop.NoOpCache;

import jakarta.enterprise.inject.Instance;

public interface CacheAware {
    Cache DEFAULT = new NoOpCache();

    default Cache getCache(String cacheName) {
        return SafeCDI.current()
                .map(cdi -> cdi.select(CacheManager.class))
                .filter(Instance::isResolvable)
                .map(Instance::get)
                .flatMap(cacheManager -> cacheManager.getCache(cacheName))
                .orElse(DEFAULT);
    }
}
