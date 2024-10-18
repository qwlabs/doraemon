package com.qwlabs.graphql.relay;

import graphql.Assert;
import graphql.PublicApi;
import graphql.relay.DefaultConnection;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@PublicApi
public class Connection<T> extends DefaultConnection<T> {
    private final Long totalCount;

    public Connection(List<@NotNull Edge<@NotNull T>> edges, PageInfo pageInfo, Long totalCount) {
        super(edges, pageInfo);
        this.totalCount = Assert.assertNotNull(totalCount, () -> "total count cannot be null");
    }

    @NotNull
    public Long getTotalCount() {
        return totalCount;
    }
}
