package com.qwlabs.lang;

import com.google.common.io.Files;

import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public final class ResourceLoader {
    private static final String CLASSPATH_URL_PREFIX = "classpath:";

    private ResourceLoader() {
    }

    public static InputStream getResourceAsStream(@NotNull String location) throws IOException {
        Objects.requireNonNull(location, "Location must not be null");
        if (location.startsWith("/")) {
            return getResourceByPath(location);
        }
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return getResourceByClassPath(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        try {
            URL url = new URL(location);
            return url.openStream();
        } catch (MalformedURLException ex) {
            return getResourceByPath(location);
        }
    }

    private static InputStream getResourceByClassPath(String location) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
    }

    private static InputStream getResourceByPath(String location) throws IOException {
        return Files.asByteSource(new File(location)).openStream();
    }
}
