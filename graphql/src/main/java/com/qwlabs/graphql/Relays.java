package com.qwlabs.graphql;

import com.qwlabs.jakarta.data.Pages;
import graphql.relay.Connection;
import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import jakarta.annotation.Nullable;
import jakarta.data.page.Page;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Relays {

    public static <E, P> Connection<P> of(@NotNull Page<E> page, @Nullable Function<E, P> mapper) {
        if (Objects.isNull(mapper)) {
            return (Connection<P>) of(page);
        }
        return of(Pages.map(page, mapper));
    }

    public static <P> Connection<P> of(@NotNull Page<P> page) {
        Objects.requireNonNull(page, "page can not be null.");
        List<Edge<P>> edges = buildEdges(page);
        PageInfo pageInfo = buildPageInfo(page, edges);
        return new DefaultConnection<>(edges, pageInfo);
    }

    private static <P> PageInfo buildPageInfo(Page<P> page, List<Edge<P>> edges) {
        ConnectionCursor startCursor = null;
        ConnectionCursor endCursor = null;
        if (!edges.isEmpty()) {
            startCursor = edges.get(0).getCursor();
            endCursor = edges.get(edges.size() - 1).getCursor();
        }
        return new DefaultPageInfo(
            startCursor,
            endCursor,
            page.hasPrevious(),
            page.hasNext()
        );
    }

    private static <P> List<Edge<P>> buildEdges(Page<P> page) {
        long startAt = (page.pageRequest().page() - 1) * page.pageRequest().size();
        List<Edge<P>> edges = new ArrayList<>(page.numberOfElements());
        Iterator<P> iterator = page.content().iterator();
        while (iterator.hasNext()) {
            P node = iterator.next();
            edges.add(new DefaultEdge<>(node, new DefaultConnectionCursor(String.valueOf(startAt++))));
        }
        return edges;
    }
}
