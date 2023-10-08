package com.qwlabs.q.sort;


import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.qwlabs.lang.C2;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
public class QSortStringFormatter implements QSortFormatter<String> {
    public static final QSortStringFormatter DEFAULT = QSortStringFormatter.builder().build();
    @NotNull
    @Builder.Default
    protected final String ascBy = "+";
    @NotNull
    @Builder.Default
    protected final String descBy = "-";
    @NotNull
    @Builder.Default
    protected final String joinBy = ",";
    @Builder.Default
    protected final boolean trim = true;
    @Builder.Default
    protected final boolean skipEmpty = true;

    @Override
    public String emptyValue() {
        return "";
    }

    @Override
    public String formatAsc(String prefix, String field) {
        return "%s%s%s".formatted(prefix, field, ascBy);
    }

    @Override
    public String formatDesc(String prefix, String field) {
        return "%s%s%s".formatted(prefix, field, descBy);
    }

    @Override
    public String join(@NotNull List<String> segments) {
        var pretreated = pretreat(segments);
        if (C2.isEmpty(segments)) {
            return emptyValue();
        }
        return doJoin(pretreated);
    }

    private List<String> pretreat(List<String> segments) {
        var stream = segments.stream();
        if (skipEmpty) {
            stream = stream.filter(Objects::nonNull);
        }
        if (trim) {
            stream = stream.map(String::trim);
        }
        if (skipEmpty) {
            stream = stream.map(Strings::emptyToNull).filter(Objects::nonNull);
        }
        return stream.toList();
    }

    protected String doJoin(@NotNull List<String> segments) {
        return Joiner.on(joinBy).join(segments);
    }
}
