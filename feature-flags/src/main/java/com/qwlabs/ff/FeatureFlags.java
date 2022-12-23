package com.qwlabs.ff;

import java.util.stream.Stream;

public interface FeatureFlags {
    Stream<FeatureFlag> get();

    FeatureFlag get(String feature);
}
