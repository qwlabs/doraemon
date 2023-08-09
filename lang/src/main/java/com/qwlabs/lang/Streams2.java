package com.qwlabs.lang;

import java.util.stream.Stream;

public class Streams2 {
    public static <T> Stream<T> parallel(Stream<T> stream, boolean parallel) {
        if (!parallel) {
            return stream;
        }
        return stream.parallel();
    }
}
