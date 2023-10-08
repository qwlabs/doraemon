package com.qwlabs.panache.q;

import com.google.common.base.Strings;
import com.qwlabs.lang.C2;
import com.qwlabs.q.sort.QSortFormatter;
import io.quarkus.panache.common.Sort;

import java.util.List;

import static com.qwlabs.panache.q.PanacheQSort.nativeField;

public class PanacheQSortFormatter implements QSortFormatter<Sort> {
    public static final PanacheQSortFormatter NATIVE_QUERY = new PanacheQSortFormatter(true);
    public static final PanacheQSortFormatter QUERY = new PanacheQSortFormatter(false);
    private final boolean nativeQuery;

    private PanacheQSortFormatter(boolean nativeQuery) {
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
    public Sort emptyValue() {
        return Sort.empty();
    }

    @Override
    public Sort formatAsc(String prefix, String field) {
        return Sort.ascending(field(prefix, field));
    }

    @Override
    public Sort formatDesc(String prefix, String field) {
        return Sort.descending(field(prefix, field));
    }

    @Override
    public Sort join(List<Sort> segments) {
        var result = Sort.empty();
        segments.stream()
            .map(Sort::getColumns)
            .filter(C2::isNotEmpty)
            .map(columns -> columns.get(0))
            .forEach(column -> result.and(column.getName(), column.getDirection()));
        return result;
    }
}
