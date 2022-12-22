package com.qwlabs.panache.query;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public final class PanacheQueries {

    private PanacheQueries() {
    }

    public static <E> PanacheQuery<E> empty() {
        return new EmptyPanacheQuery<>();
    }
}
