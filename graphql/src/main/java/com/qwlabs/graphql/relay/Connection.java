package com.qwlabs.graphql.relay;

import com.google.common.collect.ImmutableList;
import graphql.PublicApi;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import static graphql.Assert.assertNotNull;

@PublicApi
public class Connection<T> {
    @NotNull
    private final ImmutableList<@NotNull Edge<T>> edges;
    @NotNull
    private final PageInfo pageInfo;
    @NotNull
    private final Long totalCount;

    public Connection(List<Edge<T>> edges, PageInfo pageInfo, Long totalCount) {
        this.edges = ImmutableList.copyOf(assertNotNull(edges, () -> "edges cannot be null"));
        this.pageInfo = assertNotNull(pageInfo, () -> "page info cannot be null");
        this.totalCount = assertNotNull(totalCount, () -> "total count cannot be null");
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public Long getTotalCount() {
        return totalCount;
    }
}
