package com.qwlabs.lang;

import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public final class InputStreams {
    private InputStreams() {
    }

    @SneakyThrows
    public static InputStream toRepeatableRead(InputStream inputStream) {
        InputStream stream = new ByteArrayInputStream(ByteStreams.toByteArray(inputStream));
        stream.mark(0);
        return stream;
    }

    public static void reset(InputStream inputStream) {
        if (inputStream.markSupported()) {
            try {
                inputStream.reset();
            } catch (IOException ignore) {
                LOGGER.debug("mark/reset not supported");
            }
        }
    }
}
