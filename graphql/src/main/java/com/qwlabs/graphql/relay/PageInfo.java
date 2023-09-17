package com.qwlabs.graphql.relay;


import graphql.PublicApi;

import jakarta.validation.constraints.NotNull;

@PublicApi
public class PageInfo {
    @NotNull
    private final String startCursor;
    @NotNull
    private final String endCursor;
    @NotNull
    private final boolean hasPreviousPage;
    @NotNull
    private final boolean hasNextPage;

    public PageInfo(String startCursor, String endCursor,
                    boolean hasPreviousPage, boolean hasNextPage) {
        this.startCursor = startCursor;
        this.endCursor = endCursor;
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
    }

    public String getStartCursor() {
        return startCursor;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
