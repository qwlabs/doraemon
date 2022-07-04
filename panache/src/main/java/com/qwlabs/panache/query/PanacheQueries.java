package com.qwlabs.panache.query;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

public class PanacheQueries {
    public static <E> PanacheQuery<E> empty() {
        return new EmptyPanacheQuery<>();
    }
}