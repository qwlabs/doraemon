package com.qwlabs.cdi;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public final class SafeCDI {
    private SafeCDI() {
    }

    public static Optional<CDI<Object>> current() {
        try {
            return Optional.of(CDI.current());
        } catch (Exception e) {
            LOGGER.error("Load current CDI instance error.", e);
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
