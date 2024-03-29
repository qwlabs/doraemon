package com.qwlabs.jpa.q;

import com.google.common.base.Strings;
import com.qwlabs.q.sort.StringQSortFormatter;
import jakarta.validation.constraints.NotNull;

import static com.qwlabs.jpa.q.JpaQSort.nativeField;

public class JpaQSortFormatter extends StringQSortFormatter {
    public static final JpaQSortFormatter NATIVE_QUERY = new JpaQSortFormatter(true);
    public static final JpaQSortFormatter QUERY = new JpaQSortFormatter(false);
    private final boolean nativeQuery;

    private JpaQSortFormatter(boolean nativeQuery) {
        super("asc", "desc", ",");
        this.nativeQuery = nativeQuery;
    }

    private String field(@NotNull String prefix, @NotNull String field) {
        if (nativeQuery) {
            field = nativeField(field);
        }
        if (Strings.isNullOrEmpty(prefix)) {
            return field;
        }
        return "%s.%s".formatted(prefix, field);
    }

    @Override
    public String formatAsc(@NotNull String prefix, @NotNull String field) {
        return "%s %s".formatted(field(prefix, field), ascBy);
    }

    @Override
    public String formatDesc(@NotNull String prefix, @NotNull String field) {
        return "%s %s".formatted(field(prefix, field), descBy);
    }
}
