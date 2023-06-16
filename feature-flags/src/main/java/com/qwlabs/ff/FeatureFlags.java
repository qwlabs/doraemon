package com.qwlabs.ff;

import java.util.stream.Stream;

import static com.qwlabs.ff.FeatureFlag.LOGGER;


public interface FeatureFlags {
    Stream<FeatureFlag> get();

    FeatureFlag get(String feature);

    default void invalidateAll() {
        get().forEach(featureFlag -> {
            try {
                featureFlag.invalidateAll();
            } catch (Exception e) {
                LOGGER.warn("Invalidate feature %s error.".formatted(featureFlag.feature()), e);
            }
        });
    }

    default void invalidateBy(String... topics) {
        get().forEach(featureFlag -> {
            try {
                featureFlag.invalidateBy(topics);
            } catch (Exception e) {
                LOGGER.warn(
                    "Invalidate %s feature %s error.".formatted(
                        topics,
                        featureFlag.feature()
                    ), e);
            }
        });
    }
}
