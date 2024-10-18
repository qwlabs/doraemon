package com.qwlabs.graphql.relay;

import com.qwlabs.jakarta.data.Pages;
import jakarta.annotation.Nullable;
import jakarta.data.page.Page;
import jakarta.validation.constraints.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
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
        return new Connection<>(edges, pageInfo, page.totalElements());
    }

    private static <P> PageInfo buildPageInfo(Page<P> page, List<Edge<P>> edges) {
        String startCursor = null;
        String endCursor = null;
        if (!edges.isEmpty()) {
            startCursor = edges.get(0).getCursor();
            endCursor = edges.get(edges.size() - 1).getCursor();
        }
        return new PageInfo(
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
            edges.add(new Edge<>(node, createCursor(startAt++)));
        }
        return edges;
    }

    private static String createCursor(long offset) {
        byte[] bytes = Long.toString(offset).getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
