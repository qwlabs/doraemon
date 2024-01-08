package com.qwlabs.excel;

import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public final class ExcelHelper {
    private ExcelHelper() {
    }

    public static int toIndex(int rowOrColumn) {
        return rowOrColumn - 1;
    }

    public static int toNaturalSequence(int index) {
        return index + 1;
    }

    public static Map<Integer, String> cleanup(Map<Integer, String> data) {
        return cleanup(data, (index, value) -> Objects.nonNull(value));
    }

    public static Map<Integer, String> cleanup(Map<Integer, String> data,
                                               BiPredicate<Integer, String> predicate) {
        Map<Integer, String> cleanup = Maps.newHashMap();
        Optional.ofNullable(data).orElseGet(Map::of)
            .forEach((index, value) -> {
                if (predicate.test(index, value)) {
                    cleanup.put(index, value);
                }
            });
        return cleanup;
    }

    public static Map<Integer, String> lookupHeaders(@NotNull Map<Integer, String> headerData,
                                                     @NotNull Map<String, String> lookups) {
        Map<Integer, String> headers = Maps.newHashMapWithExpectedSize(headerData.size());
        cleanup(headerData).forEach((index, title) -> {
            var newTitle = lookups.get(title);
            if (Objects.nonNull(newTitle)) {
                headers.put(index, newTitle);
            }
        });
        headerData.forEach(headers::putIfAbsent);
        return headers;
    }
}
