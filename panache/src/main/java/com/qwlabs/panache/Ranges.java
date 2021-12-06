package com.qwlabs.panache;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Range;

public final class Ranges {
    private Ranges() {
    }

    public static Range fromPage(Page page) {
        return fromPage(page.index, page.size);
    }

    public static Range fromPage(int index, int size) {
        if (index < 0) {
            throw new IllegalArgumentException("Page index can not be less than 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size can not be less than 1");
        }
        int startIndex = index * size;
        int lastIndex = startIndex + size - 1;
        return Range.of(startIndex, lastIndex);
    }
}
