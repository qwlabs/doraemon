package com.qwlabs.jakarta.data;

import com.google.common.primitives.Longs;
import jakarta.data.Limit;

import java.util.Optional;

public interface Limits {
    static Limit of(String after, Integer first) {
        if (first <= 0) {
            throw new IllegalArgumentException("first can not be less than 1");
        }
        long startAt = Optional.ofNullable(Longs.tryParse(after)).orElse(1L);
        return new Limit(first, startAt);
    }
}
