package com.qwlabs.quarkus.test.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;

public class MultiTenancyProfile  implements QuarkusTestProfile {
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of("quarkus.tenant.enabled", Boolean.TRUE.toString());
    }
}
