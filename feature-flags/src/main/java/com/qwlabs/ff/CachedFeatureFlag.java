package com.qwlabs.ff;

import com.qwlabs.lang.C2;
import com.qwlabs.quarkus.cache.CacheAware;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public abstract class CachedFeatureFlag implements FeatureFlag, CacheAware {
    private static final String CACHE_NAME = "feature-flags";

    @Override
    public final boolean enabled(@Nullable String topic) {
        String key = getCacheKey(topic);
        return enabledFromCache(key, topic);
    }

    @CacheResult(cacheName = CACHE_NAME)
    protected boolean enabledFromCache(@CacheKey String key, String topic) {
        var enabled = loadEnabled(topic);
        LOGGER.debug("load feature-flags by key:{}, enabled:{}", key, enabled);
        return enabled;
    }

    public void invalidateAll() {
        Cache cache = getCache(CACHE_NAME);
        cache.invalidateAll().await().indefinitely();
    }

    public void invalidateBy(String... topics) {
        var cacheKeys = C2.stream(topics)
            .map(this::getCacheKey)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        if (cacheKeys.isEmpty()) {
            return;
        }
        Cache cache = getCache(CACHE_NAME);
        cacheKeys.parallelStream().forEach(key -> cache.invalidate(key).await().indefinitely());
    }

    protected abstract boolean loadEnabled(@Nullable String topic);

    protected String getCacheKey(@Nullable String topic) {
        return String.format("%s/%s", feature(), Optional.ofNullable(topic).orElse(""));
    }
}
