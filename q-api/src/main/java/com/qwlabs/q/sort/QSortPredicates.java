package com.qwlabs.q.sort;

import com.qwlabs.lang.C2;

import java.util.Set;

public final class QSortPredicates {
    private QSortPredicates() {
    }

    public static QSortPredicate all() {
        return segment -> true;
    }

    public static QSortPredicate includesStartWith(String startWith) {
        return segment -> segment.getField().startsWith(startWith);
    }

    public static QSortPredicate includes(String... fields) {
        return includes(C2.set(fields));
    }

    public static QSortPredicate includes(Set<String> fields) {
        return segment -> fields.contains(segment.getField());
    }
}
