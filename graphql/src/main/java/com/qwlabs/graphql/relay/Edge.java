package com.qwlabs.graphql.relay;

import graphql.PublicApi;

import jakarta.validation.constraints.NotNull;

import static graphql.Assert.assertNotNull;

@PublicApi
public class Edge<T> {
    private final T node;

    private final String cursor;

    public Edge(T node, String cursor) {
        this.cursor = assertNotNull(cursor, () -> "cursor cannot be null");
        this.node = assertNotNull(node, () -> "node can not be null");
    }

    @NotNull
    public T getNode() {
        return node;
    }

    @NotNull
    public String getCursor() {
        return cursor;
    }
}
