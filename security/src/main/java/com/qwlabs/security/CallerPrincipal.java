package com.qwlabs.security;

import io.quarkus.security.runtime.QuarkusPrincipal;

public class CallerPrincipal extends QuarkusPrincipal {
    private final String type;

    public CallerPrincipal(String id, String type) {
        super(id);
        this.type = type;
    }

    public String id() {
        return getName();
    }

    public String type() {
        return type;
    }
}
