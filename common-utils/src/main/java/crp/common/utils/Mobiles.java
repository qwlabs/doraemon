package crp.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public final class Mobiles {
    private static final Logger LOGGER = LoggerFactory.getLogger(Mobiles.class);
    private static final Pattern PATTERN =
            Pattern.compile("^1(3\\d|4[5-8]|5[0-35-9]|6[567]|7[01345-8]|8\\d|9[025-9])\\d{8}$");

    private Mobiles() {
    }

    public static boolean isMobile(String mobile) {
        try {
            return PATTERN.matcher(mobile).matches();
        } catch (Exception e) {
            LOGGER.warn("invalid mobile", e);
            return false;
        }
    }
}
