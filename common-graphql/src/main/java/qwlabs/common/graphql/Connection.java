package qwlabs.common.graphql;

import com.google.common.collect.ImmutableList;
import graphql.PublicApi;

import javax.validation.constraints.NotNull;
import java.util.List;

import static graphql.Assert.assertNotNull;

@PublicApi
public class Connection<T> {
    private final ImmutableList<Edge<T>> edges;
    private final PageInfo pageInfo;
    private final Long totalCount;

    public Connection(List<Edge<T>> edges, PageInfo pageInfo, Long totalCount) {
        this.edges = ImmutableList.copyOf(assertNotNull(edges, () -> "edges cannot be null"));
        this.pageInfo = assertNotNull(pageInfo, () -> "page info cannot be null");
        this.totalCount = assertNotNull(totalCount, () -> "total count cannot be null");
    }

    @NotNull
    public List<@NotNull Edge<T>> getEdges() {
        return edges;
    }

    @NotNull
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    @NotNull
    public Long getTotalCount() {
        return totalCount;
    }
}
