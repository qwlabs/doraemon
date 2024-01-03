package com.qwlabs.excel;

import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Objects;

public class ExcelHeaders {
    public static Map<Integer, String> lookupHeaders(@NotNull Map<Integer, String> headerData,
                                                     @NotNull Map<String, String> lookups) {
        Map<Integer, String> headers = Maps.newHashMapWithExpectedSize(headerData.size());
        headerData.forEach((index, title) -> {
            if (Objects.isNull(title)) {
                return;
            }
            var newTitle = lookups.get(title);
            if (Objects.nonNull(newTitle)) {
                headers.put(index, newTitle);
            }
        });
        headerData.forEach((index, title) -> {
            if (!headers.containsKey(index) && Objects.nonNull(title)) {
                headers.put(index, title);
            }
        });
        return headers;
    }
}
