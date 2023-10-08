package com.qwlabs.q.sort;

public enum QSortDirection {
    ASCENDING,
    DESCENDING;

    public boolean isAsc() {
        return ASCENDING == this;
    }

    public boolean isDesc() {
        return DESCENDING == this;
    }
}
