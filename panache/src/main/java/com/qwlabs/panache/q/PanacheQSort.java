package com.qwlabs.panache.q;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.qwlabs.lang.C2;
import com.qwlabs.q.sort.QSort;
import com.qwlabs.q.sort.QSortPredicate;
import com.qwlabs.q.sort.QSortPredicates;
import io.quarkus.panache.common.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PanacheQSort {
    private final QSort sort;
    private final boolean nativeQuery;
    private final List<Formatter> formatters;

    private PanacheQSort(QSort sort, boolean nativeQuery) {
        this.sort = Optional.ofNullable(sort).orElse(QSort.empty());
        this.nativeQuery = nativeQuery;
        this.formatters = Lists.newArrayList();
    }

    public PanacheQSort clear() {
        formatters.clear();
        return this;
    }

    private PanacheQSortFormatter formatter() {
        return nativeQuery ? PanacheQSortFormatter.NATIVE_QUERY : PanacheQSortFormatter.QUERY;
    }

    public PanacheQSort with(QSortPredicate predicate) {
        return with(predicate, "");
    }

    public PanacheQSort with(QSortPredicate predicate, String prefix) {
        formatters.add(Formatter.builder()
            .prefix(prefix)
            .formatter(formatter())
            .predicate(predicate)
            .build());
        return this;
    }

    public Sort format() {
        return format(QSort.empty());
    }

    public Sort format(QSort defaultSort) {
        return format(defaultSort, QSortPredicates.all());
    }

    public Sort format(QSort defaultSort, String... fields) {
        return format(defaultSort, QSortPredicates.includes(fields));
    }

    public Sort format(String defaultSort) {
        return format(QSort.of(defaultSort), QSortPredicates.all());
    }

    public Sort format(QSort defaultSort, QSortPredicate defaultPredicate) {
        if (formatters.isEmpty()) {
            with(defaultPredicate);
        }
        List<Sort> sorts = formatters.stream()
            .map(formatter -> formatter.format(sort))
            .filter(Objects::nonNull)
            .filter(sort -> C2.isNotEmpty(sort.getColumns()))
            .toList();
        if (sorts.isEmpty()) {
            return defaultSort.format(QSortPredicates.all(), formatter());
        }
        return formatter().join(sorts);
    }

    public static PanacheQSort ofNative(QSort sort) {
        return new PanacheQSort(sort, true);
    }

    public static PanacheQSort of(QSort sort) {
        return new PanacheQSort(sort, false);
    }

    public static PanacheQSort of(QSort sort, boolean nativeQuery) {
        return new PanacheQSort(sort, nativeQuery);
    }

    public static String nativeField(String filed) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filed);
    }

    @Builder
    @AllArgsConstructor
    private static class Formatter {
        private QSortPredicate predicate;
        private PanacheQSortFormatter formatter;
        private String prefix;

        public Sort format(QSort sort) {
            return sort.format(predicate, formatter, prefix);
        }
    }
}
