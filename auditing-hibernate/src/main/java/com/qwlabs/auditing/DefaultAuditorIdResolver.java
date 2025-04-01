package com.qwlabs.auditing;

import jakarta.enterprise.inject.Instance;

import java.util.Optional;

public class DefaultAuditorIdResolver implements AuditorIdResolver {
    private static final AuditorIdResolver INSTANCE = new DefaultAuditorIdResolver();

    @Override
    public Optional<String> resolve(Object entity) {
        return Optional.empty();
    }

    protected static AuditorIdResolver orDefault(Instance<AuditorIdResolver> auditorIdResolvers) {
        return auditorIdResolvers.stream().findFirst().orElse(INSTANCE);
    }
}
