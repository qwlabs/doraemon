package com.qwlabs.q.sort;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class QSort {
    private static final QSort EMPTY = QSort.of(null);
    private static final String RAW_SEPARATOR = ",";
    private final String raw;
    private final List<QSortSegment> segments;

    private QSort(String raw) {
        this.raw = Optional.ofNullable(raw).map(String::trim).orElse(null);
        this.segments = parseSegments(this.raw);
    }

    private static List<QSortSegment> parseSegments(String raw) {
        if (Objects.isNull(raw)) {
            return List.of();
        }
        List<QSortSegment> segments = Lists.newArrayList();
        Set<String> existFields = Sets.newHashSet();
        Splitter.on(RAW_SEPARATOR)
            .trimResults()
            .omitEmptyStrings()
            .splitToStream(raw)
            .map(QSortSegment::of)
            .filter(Objects::nonNull)
            .filter(segment -> !existFields.contains(segment.getField()))
            .forEach(segment -> {
                existFields.add(segment.getField());
                segments.add(segment);
            });
        return segments;
    }

    public <O> O format(@NotNull QSortFormatter<O> formatter) {
        return format(formatter, "");
    }

    public <O> O format(@NotNull QSortFormatter<O> formatter,
                        @NotNull String prefix) {
        return format(QSortPredicates.all(), formatter, prefix);
    }

    public <O> O format(@NotNull QSortPredicate predicate,
                        @NotNull QSortFormatter<O> formatter) {
        return format(predicate, formatter, "");
    }

    public <O> O format(@NotNull QSortPredicate predicate,
                        @NotNull QSortFormatter<O> formatter,
                        @NotNull String prefix) {
        var formatted = segments.stream()
            .filter(predicate::test)
            .map(segment -> formatter.format(prefix, segment))
            .toList();
        return formatter.join(formatted);
    }

    public boolean isEmpty() {
        return segments.isEmpty();
    }

    public static QSort of(String raw) {
        return new QSort(raw);
    }

    public static QSort empty() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return format(QSortStringFormatter.DEFAULT);
    }
}
