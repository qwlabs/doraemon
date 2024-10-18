package com.qwlabs.graphql.relay;


import graphql.Assert;
import graphql.PublicApi;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@PublicApi
@Getter
@EqualsAndHashCode
public class Edge<T> {
    @NotNull
    private final T node;
    @NotNull
    private final String cursor;

    public Edge(T node, String cursor) {
        this.cursor =  Assert.assertNotNull(cursor, () -> "cursor cannot be null");
        this.node = node;
    }
}
