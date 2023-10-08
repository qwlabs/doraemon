package com.qwlabs.q.sort;


import com.google.common.base.Joiner;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class StringQSortFormatter implements QSortFormatter<String> {
    public static final StringQSortFormatter DEFAULT = StringQSortFormatter.builder().build();
    @NotNull
    @Builder.Default
    protected final String ascBy = "+";
    @NotNull
    @Builder.Default
    protected final String descBy = "-";
    @NotNull
    @Builder.Default
    protected final String joinBy = ",";

    @Override
    public String emptyValue() {
        return "";
    }

    @Override
    public String formatAsc(@NotNull String prefix, @NotNull String field) {
        return "%s%s%s".formatted(prefix, field, ascBy);
    }

    @Override
    public String formatDesc(@NotNull String prefix, @NotNull String field) {
        return "%s%s%s".formatted(prefix, field, descBy);
    }

    @Override
    public String join(@NotNull List<String> segments) {
        return Joiner.on(joinBy).join(segments);
    }
}
