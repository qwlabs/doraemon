package com.qwlabs.lang;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class Caches {

    public static <K, V> V getUnchecked(@NotNull LoadingCache<K, V> cache,
                                        @NotNull K key,
                                        V defaultValue) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            return defaultValue;
        }
    }

    public static <K, V> ImmutableMap<K, V> getAllUnchecked(@NotNull LoadingCache<K, V> cache,
                                                            @NotNull Iterable<? extends K> keys) {
        try {
            return cache.getAll(keys);
        } catch (ExecutionException e) {
            return ImmutableMap.of();
        }
    }
}
