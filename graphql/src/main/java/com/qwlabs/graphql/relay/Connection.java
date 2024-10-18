package com.qwlabs.graphql.relay;

import graphql.Assert;
import graphql.PublicApi;
import graphql.com.google.common.collect.ImmutableList;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@PublicApi
@Getter
@EqualsAndHashCode
public class Connection<T> {
    @NotNull
    private final ImmutableList<@NotNull Edge<T>> edges;
    @NotNull
    private final PageInfo pageInfo;
    @NotNull
    private final Long totalCount;

    public Connection(List<@NotNull Edge<T>> edges,
                      PageInfo pageInfo,
                      Long totalCount) {
        this.edges = ImmutableList.copyOf(Assert.assertNotNull(edges, () -> "edges cannot be null"));
        this.pageInfo = Assert.assertNotNull(pageInfo, () -> "page info cannot be null");
        this.totalCount = Assert.assertNotNull(totalCount, () -> "total count cannot be null");
    }
}
