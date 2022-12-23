package com.qwlabs.test.builders;

import com.qwlabs.cdi.SafeCDI;

public final class BuilderAware {
    private BuilderAware() {
    }

    public static <B> B getBuilder(Class<B> builderClazz) {
        return SafeCDI.current()
                .map(cdi -> cdi.select(builderClazz).get())
                .orElseGet(() -> {
                    try {
                        return builderClazz.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Can not found builder. class=" + builderClazz.getName());
                    }
                });
    }
}
