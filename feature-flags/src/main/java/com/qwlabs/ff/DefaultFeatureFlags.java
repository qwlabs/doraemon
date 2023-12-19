package com.qwlabs.ff;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.Objects;
import java.util.stream.Stream;

@ApplicationScoped
public class DefaultFeatureFlags implements FeatureFlags {
    private final Instance<FeatureFlag> instances;

    @Inject
    public DefaultFeatureFlags(Instance<FeatureFlag> instances) {
        this.instances = instances;
    }

    @Override
    public Stream<FeatureFlag> get() {
        return instances.stream();
    }

    @Override
    public FeatureFlag get(String feature) {
        return instances.stream()
            .filter(instance -> Objects.equals(instance.feature(), feature))
            .findFirst()
            .orElseThrow(() -> FFMessages.INSTANCE.featureNotFound(feature));
    }
}
