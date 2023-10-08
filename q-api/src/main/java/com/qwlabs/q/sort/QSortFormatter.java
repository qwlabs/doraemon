package com.qwlabs.q.sort;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface QSortFormatter<O> {
    default O format(String prefix, @NotNull QSortSegment segment) {
        if (segment.getDirection().isAsc()) {
            return formatAsc(prefix, segment.getField());
        } else {
            return formatDesc(prefix, segment.getField());
        }
    }
    O emptyValue();
    O formatAsc(String prefix, String field);
    O formatDesc(String prefix, String field);
    O join(List<O> segments);
}
