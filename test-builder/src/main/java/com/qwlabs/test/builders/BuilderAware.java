package com.qwlabs.test.builders;

import com.qwlabs.cdi.CDI2;
import jakarta.enterprise.inject.Instance;

public final class BuilderAware {
    private BuilderAware() {
    }

    public static <B> B getBuilder(Class<B> builderClazz) {
        return CDI2.select(builderClazz)
                .map(Instance::get)
                .orElseGet(() -> {
                    try {
                        return builderClazz.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Can not found builder. class=" + builderClazz.getName());
                    }
                });
    }
}
