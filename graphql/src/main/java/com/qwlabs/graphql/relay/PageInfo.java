package com.qwlabs.graphql.relay;

import graphql.PublicApi;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@PublicApi
@Getter
@EqualsAndHashCode
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
                    boolean hasPreviousPage,
                    boolean hasNextPage) {
        this.startCursor = startCursor;
        this.endCursor = endCursor;
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
    }
}
