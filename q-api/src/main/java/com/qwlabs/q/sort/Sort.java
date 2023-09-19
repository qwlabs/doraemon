package com.qwlabs.q.sort;

import com.google.common.base.Splitter;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class Sort {
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

    private Sort(String raw) {
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
        var segments = segmentsSupplier.get();
        if (segments.isEmpty()) {
            return defaultSupplier.get();
        }
        var iter = segments.iterator();
        O value = null;
        do {
            var segment = iter.next();
            var currentValue = (segment.asc ? ascFormatter : descFormatter).apply(segment.field);
            if (value == null) {
                value = currentValue;
                continue;
            }
            value = merger.apply(value, currentValue);
        } while (iter.hasNext());
        return value;
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

    public static Sort of(String raw) {
        return new Sort(raw);
    }

    private static class Segment {
        private final String field;
        private final boolean asc;

        private Segment(String field, boolean asc) {
            this.field = field;
            this.asc = asc;
        }

        private static Segment of(String raw) {
            Boolean asc = isAsc(raw);
            if (Objects.isNull(asc)) {
                return null;
            }
            var filed = raw.substring(0, raw.length() - RAW_SEGMENT_FLAG_LENGTH).trim();
            return new Segment(filed, asc);
        }

        private static Boolean isAsc(String raw) {
            if (raw.endsWith(RAW_ASC_FLAG)) {
                return Boolean.TRUE;
            }
            if (raw.endsWith(RAW_DESC_FLAG)) {
                return Boolean.FALSE;
            }
            return null;
        }
    }
}
