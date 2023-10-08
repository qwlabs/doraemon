package com.qwlabs.q.sort;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface QSortFormatter<O> {
    default O format(@NotNull String prefix, @NotNull QSortSegment segment) {
        if (segment.getDirection().isAsc()) {
            return formatAsc(prefix, segment.getField());
        } else {
            return formatDesc(prefix, segment.getField());
        }
    }
    O emptyValue();
    O formatAsc(@NotNull String prefix, @NotNull String field);
    O formatDesc(@NotNull String prefix, @NotNull String field);
    O join(@NotNull List<O> segments);
}
