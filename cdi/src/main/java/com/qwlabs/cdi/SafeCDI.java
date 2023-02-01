package com.qwlabs.cdi;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import java.util.Optional;

public final class SafeCDI {
    private SafeCDI() {
    }

    public static Optional<CDI<Object>> current() {
        try {
            return Optional.of(CDI.current());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<Instance<T>> select(Class<T> clzzz) {
        return current().map(cdi -> cdi.select(clzzz));
    }

    public static <T> Optional<T> selectPrimary(Class<T> clzzz) {
        return current()
                .flatMap(cdi -> cdi.select(clzzz, PrimaryLiteral.INSTANCE).stream().findFirst());
    }
}
