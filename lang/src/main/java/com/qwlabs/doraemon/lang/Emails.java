package com.qwlabs.doraemon.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public final class Emails {
    private static final Logger LOGGER = LoggerFactory.getLogger(Emails.class);
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    private Emails() {
    }

    public static boolean isEmail(String email) {
        try {
            return PATTERN.matcher(email).matches();
        } catch (Exception e) {
            LOGGER.warn("invalid email", e);
            return false;
        }
    }
}
