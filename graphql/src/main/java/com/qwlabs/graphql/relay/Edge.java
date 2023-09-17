package com.qwlabs.graphql.relay;

import graphql.PublicApi;
import jakarta.validation.constraints.NotNull;

import static graphql.Assert.assertNotNull;

@PublicApi
public class Edge<T> {
    @NotNull
    private final T node;
    @NotNull
    private final String cursor;

    public Edge(T node, String cursor) {
        this.cursor = assertNotNull(cursor, () -> "cursor cannot be null");
        this.node = assertNotNull(node, () -> "node can not be null");
    }

    public T getNode() {
        return node;
    }

    public String getCursor() {
        return cursor;
    }
}
