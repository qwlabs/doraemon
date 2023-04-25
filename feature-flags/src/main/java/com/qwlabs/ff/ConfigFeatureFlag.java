package com.qwlabs.ff;

import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Nullable;
import org.eclipse.microprofile.config.ConfigProvider;

@Slf4j
public abstract class ConfigFeatureFlag extends CachedFeatureFlag {
    protected abstract String propertyName();

    @Override
    protected boolean loadEnabled(@Nullable String topic) {
        try {
            return ConfigProvider.getConfig().getValue(propertyName(), Boolean.class);
        } catch (Exception e) {
            LOGGER.error("Can not found config feature flag:{}", propertyName());
            return false;
        }
    }
}
