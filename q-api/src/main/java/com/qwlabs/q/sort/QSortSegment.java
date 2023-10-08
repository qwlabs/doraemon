package com.qwlabs.q.sort;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class QSortSegment {
    private static final String RAW_ASC_FLAG = "+";
    private static final String RAW_DESC_FLAG = "-";
    private static final int RAW_SEGMENT_FLAG_LENGTH = 1;
    @NotNull
    private final String field;
    @NotNull
    private final QSortDirection direction;

    private QSortSegment(@NotNull String field, @NotNull QSortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public QSortDirection getDirection() {
        return direction;
    }

    protected static QSortSegment of(String raw) {
        QSortDirection direction = ofDirection(raw);
        if (Objects.isNull(direction)) {
            return null;
        }
        var filed = raw.substring(0, raw.length() - RAW_SEGMENT_FLAG_LENGTH).trim();
        return new QSortSegment(filed, direction);
    }

    protected static QSortDirection ofDirection(String raw) {
        if (raw.endsWith(RAW_ASC_FLAG)) {
            return QSortDirection.ASCENDING;
        }
        if (raw.endsWith(RAW_DESC_FLAG)) {
            return QSortDirection.DESCENDING;
        }
        return null;
    }
}
