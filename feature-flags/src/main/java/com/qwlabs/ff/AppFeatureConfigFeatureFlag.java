package com.qwlabs.ff;


import javax.validation.constraints.NotNull;

public abstract class AppFeatureConfigFeatureFlag extends ConfigFeatureFlag {
    private static final String DEFAULT_PREFIX = "app.features";
    private static final String CONFIG_KEY_TEMPLATE = "%s.%s";
    private final String prefix;

    public AppFeatureConfigFeatureFlag() {
        this(DEFAULT_PREFIX);
    }

    public AppFeatureConfigFeatureFlag(@NotNull String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected String propertyName() {
        return String.format(CONFIG_KEY_TEMPLATE, prefix, feature());
    }
}
