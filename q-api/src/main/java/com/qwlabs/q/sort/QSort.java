package com.qwlabs.q.sort;

import com.google.common.base.Splitter;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class QSort {
    private static final QSort EMPTY = QSort.of(null);
    private static final String DEFAULT = "";
    private static final String FORMAT_SEPARATOR = " ";
    private static final String DEFAULT_FORMAT_JOIN_FLAG = ",";
    private static final String DEFAULT_FORMAT_ASC_FLAG = "asc";
    private static final String DEFAULT_FORMAT_DESC_FLAG = "desc";
    private static final String RAW_SEPARATOR = ",";
    private static final String RAW_ASC_FLAG = "+";
    private static final String RAW_DESC_FLAG = "-";
    private static final int RAW_SEGMENT_FLAG_LENGTH = 1;
    private final String raw;
    private final Supplier<List<Segment>> segmentsSupplier = Suppliers.memoize(this::parseSegments);
    private Set<String> includes;

    private QSort(String raw) {
        this.raw = Optional.ofNullable(raw).map(String::trim).orElse(null);
    }

    private List<Segment> parseSegments() {
        if (Objects.isNull(raw)) {
            return List.of();
        }
        return Lists.newArrayList(Splitter.on(RAW_SEPARATOR)
            .trimResults()
            .omitEmptyStrings()
            .splitToStream(raw)
            .map(Segment::of)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(s -> s.field, Function.identity(), (v1, v2) -> v1))
            .values());
    }

    public <O> O format(Function<String, O> ascFormatter,
                        Function<String, O> descFormatter,
                        BiFunction<O, O, O> merger,
                        Supplier<O> defaultSupplier) {
        Map<QSortDirection, Function<String, O>> directionFormatter = Map.of(
            QSortDirection.ASCENDING, ascFormatter,
            QSortDirection.DESCENDING, descFormatter
        );
        var segments = filteredSegments();
        if (segments.isEmpty()) {
            return defaultSupplier.get();
        }
        var iter = segments.iterator();
        O value = null;
        do {
            var segment = iter.next();
            var currentValue = directionFormatter.get(segment.direction).apply(segment.field);
            if (value == null) {
                value = currentValue;
                continue;
            }
            value = merger.apply(value, currentValue);
        } while (iter.hasNext());
        return value;
    }

    private List<Segment> filteredSegments() {
        var segments = segmentsSupplier.get();
        if (Objects.isNull(includes)) {
            return segments;
        }
        return segments.stream()
            .filter(segment -> includes.contains(segment.field))
            .collect(Collectors.toList());
    }

    public <O> O format(Function<String, O> ascFormatter,
                        Function<String, O> descFormatter,
                        BiFunction<O, O, O> merger) {
        return format(ascFormatter, descFormatter, merger, () -> null);
    }

    public String format(String joinFlag,
                         Function<String, String> ascFormatter,
                         Function<String, String> descFormatter) {
        return format(
            ascFormatter,
            descFormatter,
            (segment1, segment2) -> segment1 + FORMAT_SEPARATOR + joinFlag + FORMAT_SEPARATOR + segment2,
            () -> DEFAULT
        );
    }

    public String format(String joinFlag,
                         Function<String, String> ascFormatter,
                         Function<String, String> descFormatter,
                         String defaultValue) {
        return format(
            ascFormatter,
            descFormatter,
            (segment1, segment2) -> segment1 + FORMAT_SEPARATOR + joinFlag + FORMAT_SEPARATOR + segment2,
            () -> defaultValue
        );
    }

    public String format(String joinFlag, String ascFlag, String descFlag) {
        return format(
            joinFlag,
            field -> field + FORMAT_SEPARATOR + ascFlag,
            field -> field + FORMAT_SEPARATOR + descFlag,
            DEFAULT
        );
    }

    public String format(String joinFlag, String ascFlag, String descFlag, String defaultValue) {
        return format(
            joinFlag,
            field -> field + FORMAT_SEPARATOR + ascFlag,
            field -> field + FORMAT_SEPARATOR + descFlag,
            defaultValue
        );
    }

    public String format(String defaultValue) {
        return format(
            DEFAULT_FORMAT_JOIN_FLAG,
            DEFAULT_FORMAT_ASC_FLAG,
            DEFAULT_FORMAT_DESC_FLAG,
            defaultValue
        );
    }

    public String format() {
        return format(DEFAULT);
    }

    public QSort includeAll() {
        this.includes = null;
        return this;
    }

    public QSort includes(Set<String> includes) {
        this.includes = includes;
        return this;
    }

    public static QSort of(String raw) {
        return new QSort(raw);
    }

    public static QSort empty() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return format();
    }

    public static QSort fromString(String raw) {
        return of(raw);
    }

    private static class Segment {
        private final String field;
        private final QSortDirection direction;

        private Segment(String field, QSortDirection direction) {
            this.field = field;
            this.direction = direction;
        }

        private static Segment of(String raw) {
            QSortDirection direction = ofDirection(raw);
            if (Objects.isNull(direction)) {
                return null;
            }
            var filed = raw.substring(0, raw.length() - RAW_SEGMENT_FLAG_LENGTH).trim();
            return new Segment(filed, direction);
        }


        private static QSortDirection ofDirection(String raw) {
            if (raw.endsWith(RAW_ASC_FLAG)) {
                return QSortDirection.ASCENDING;
            }
            if (raw.endsWith(RAW_DESC_FLAG)) {
                return QSortDirection.DESCENDING;
            }
            return null;
        }
    }
}
