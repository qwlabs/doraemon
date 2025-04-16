package com.qwlabs.quarkus.cache;

import com.qwlabs.cdi.CDI2;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.runtime.noop.NoOpCache;

import jakarta.enterprise.inject.Instance;

public interface CacheAware {
    Cache DEFAULT = new NoOpCache();

    default Cache getCache(String cacheName) {
        return CDI2.select(CacheManager.class)
                .filter(Instance::isResolvable)
                .map(Instance::get)
                .flatMap(cacheManager -> cacheManager.getCache(cacheName))
                .orElse(DEFAULT);
    }
}
