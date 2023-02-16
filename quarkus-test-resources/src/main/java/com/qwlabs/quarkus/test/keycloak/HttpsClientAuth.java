package com.qwlabs.quarkus.test.keycloak;

public enum HttpsClientAuth {

    NONE,
    REQUEST,
    REQUIRED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
