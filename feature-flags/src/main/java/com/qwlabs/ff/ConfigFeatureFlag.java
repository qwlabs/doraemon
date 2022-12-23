package com.qwlabs.ff;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.eclipse.microprofile.config.ConfigProvider;

@Slf4j
public abstract class ConfigFeatureFlag extends CachedFeatureFlag {
    protected abstract String propertyName();

    @Override
    protected boolean loadEnabled(@Nullable String topic) {
        try {
            return ConfigProvider.getConfig().getValue(propertyName(), Boolean.class);
        } catch (Exception e) {
            LOGGER.warn("can not found config feature flag:{}", propertyName());
            return false;
        }
    }
}
