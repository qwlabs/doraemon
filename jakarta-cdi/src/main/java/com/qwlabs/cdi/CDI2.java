package com.qwlabs.cdi;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;

import java.util.Optional;

public class CDI2 {
    private CDI2() {
    }

    public static Optional<CDI<Object>> current() {
        try {
            return Optional.of(CDI.current());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<Instance<T>> select(Class<T> clazz) {
        return current().map(cdi -> cdi.select(clazz));
    }

    public static <T> Optional<T> selectPrimary(Class<T> clazz) {
        return current()
            .flatMap(cdi -> cdi.select(clazz, PrimaryLiteral.INSTANCE).stream().findFirst());
    }
}
