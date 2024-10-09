package com.qwlabs.jakarta.data;

import com.google.common.primitives.Longs;
import jakarta.annotation.Nullable;
import jakarta.data.Limit;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface Limits {
    static Limit of(@Nullable String after, @NotNull int first) {
        if (first <= 0) {
            throw new IllegalArgumentException("first can not be less than 1");
        }
        var startAt = Optional.ofNullable(after).map(Longs::tryParse).orElse(1L);
        return new Limit(first, startAt);
    }
}
