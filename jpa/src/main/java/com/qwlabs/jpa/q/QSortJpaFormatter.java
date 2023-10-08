package com.qwlabs.jpa.q;

import com.google.common.base.Strings;
import com.qwlabs.q.sort.QSortStringFormatter;

import static com.qwlabs.jpa.q.JpaQSort.nativeField;

public class QSortJpaFormatter extends QSortStringFormatter {
    public static final QSortJpaFormatter NATIVE_QUERY = new QSortJpaFormatter(true);
    public static final QSortJpaFormatter QUERY = new QSortJpaFormatter(false);
    private final boolean nativeQuery;
    private QSortJpaFormatter(boolean nativeQuery) {
        super("asc", "desc", ",", true, true);
        this.nativeQuery = nativeQuery;
    }

    private String field(String prefix, String field) {
        if (nativeQuery) {
            field = nativeField(field);
        }
        if (Strings.isNullOrEmpty(prefix)) {
            return field;
        }
        return "%s.%s".formatted(prefix, field);
    }

    @Override
    public String formatAsc(String prefix, String field) {
        return "%s %s".formatted(field(prefix, field), ascBy);
    }

    @Override
    public String formatDesc(String prefix, String field) {
        return "%s %s".formatted(field(prefix, field), descBy);
    }
}
