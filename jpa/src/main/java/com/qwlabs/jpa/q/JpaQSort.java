package com.qwlabs.jpa.q;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.qwlabs.q.sort.QSort;
import com.qwlabs.q.sort.QSortPredicate;
import com.qwlabs.q.sort.QSortPredicates;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JpaQSort {
    private final QSort sort;
    private final boolean nativeQuery;
    private final List<Formatter> formatters;

    private JpaQSort(QSort sort, boolean nativeQuery) {
        this.sort = Optional.ofNullable(sort).orElse(QSort.empty());
        this.nativeQuery = nativeQuery;
        this.formatters = Lists.newArrayList();
    }

    public JpaQSort clear() {
        formatters.clear();
        return this;
    }

    private JpaQSortFormatter formatter() {
        return nativeQuery ? JpaQSortFormatter.NATIVE_QUERY : JpaQSortFormatter.QUERY;
    }

    public JpaQSort with(QSortPredicate predicate) {
        return with(predicate, "");
    }

    public JpaQSort with(QSortPredicate predicate, String prefix) {
        formatters.add(Formatter.builder()
            .prefix(prefix)
            .formatter(formatter())
            .predicate(predicate)
            .build());
        return this;
    }

    public String format() {
        return format(QSort.empty());
    }

    public String format(QSort defaultSort) {
        return format(defaultSort, QSortPredicates.all());
    }

    public String format(QSort defaultSort, String... fields) {
        return format(defaultSort, QSortPredicates.includes(fields));
    }

    public String format(String defaultSort) {
        return format(QSort.of(defaultSort), QSortPredicates.all());
    }

    public String format(QSort defaultSort, QSortPredicate defaultPredicate) {
        if (formatters.isEmpty()) {
            with(defaultPredicate);
        }
        List<String> sql = formatters.stream()
            .map(formatter -> formatter.format(sort))
            .map(Strings::emptyToNull)
            .filter(Objects::nonNull)
            .toList();
        if (sql.isEmpty()) {
            return defaultSort.format(QSortPredicates.all(), formatter());
        }
        return formatter().join(sql);
    }

    public static JpaQSort ofNative(QSort sort) {
        return new JpaQSort(sort, true);
    }

    public static JpaQSort of(QSort sort) {
        return new JpaQSort(sort, false);
    }

    public static JpaQSort of(QSort sort, boolean nativeQuery) {
        return new JpaQSort(sort, nativeQuery);
    }

    public static String nativeField(String filed) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, filed);
    }

    @Builder
    @AllArgsConstructor
    private static class Formatter {
        private QSortPredicate predicate;
        private JpaQSortFormatter formatter;
        private String prefix;

        public String format(QSort sort) {
            return sort.format(predicate, formatter, prefix);
        }
    }

}
