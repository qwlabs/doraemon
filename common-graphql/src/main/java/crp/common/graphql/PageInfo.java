package crp.common.graphql;


import graphql.PublicApi;

import javax.validation.constraints.NotNull;

@PublicApi
public class PageInfo {

    private final String startCursor;
    private final String endCursor;
    private final boolean hasPreviousPage;
    private final boolean hasNextPage;

    public PageInfo(String startCursor, String endCursor,
                    boolean hasPreviousPage, boolean hasNextPage) {
        this.startCursor = startCursor;
        this.endCursor = endCursor;
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
    }

    @NotNull
    public String getStartCursor() {
        return startCursor;
    }

    @NotNull
    public String getEndCursor() {
        return endCursor;
    }

    @NotNull
    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    @NotNull
    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
