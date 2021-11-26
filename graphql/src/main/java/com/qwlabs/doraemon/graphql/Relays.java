package com.qwlabs.doraemon.graphql;

import com.google.common.primitives.Ints;
import com.qwlabs.doraemon.panache.Ranged;
import io.quarkus.panache.common.Range;
import io.smallrye.common.constraint.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class Relays {
    private static final Connection EMPTY = createEmptyConnection();

    private Relays() {
    }

    public static <E> Connection<E> of(@NotNull Ranged<E> ranged) {
        return of(ranged, null);
    }

    public static <T, R> Connection<R> of(@NotNull Ranged<T> ranged,
                                               @Nullable Function<T, R> mapper) {
        Objects.requireNonNull(ranged, "paged can not be null.");
        if (ranged.getTotalCount() == 0) {
            return (Connection<R>) EMPTY;
        }
        Ranged<R> resultRanged = ranged.map(mapper);
        List<Edge<R>> edges = buildEdges(resultRanged);
        PageInfo pageInfo = buildPageInfo(resultRanged, edges);
        return new Connection<>(edges, pageInfo, resultRanged.getTotalCount());
    }

    public static Range of(String after, Integer first) {
        if (first <= 0) {
            throw new InvalidPageSizeException("first can not be less than 1");
        }
        int startIndex = Optional.ofNullable(Ints.tryParse(after)).orElse(0);
        int lastIndex = startIndex + first - 1;
        return Range.of(startIndex, lastIndex);
    }

    private static <T> PageInfo buildPageInfo(Ranged<T> ranged, List<Edge<T>> edges) {
        String firstCursor = null;
        String lastCursor = null;
        if (!edges.isEmpty()) {
            firstCursor = edges.get(0).getCursor();
            lastCursor = edges.get(edges.size() - 1).getCursor();
        }
        return new PageInfo(
                firstCursor,
                lastCursor,
                ranged.hasPreviousPage(),
                ranged.hasNextPage()
        );
    }


    private static <T> List<Edge<T>> buildEdges(Ranged<T> ranged) {
        Range range = ranged.getRange();
        List<Edge<T>> edges = new ArrayList<>(range.getLastIndex() - range.getStartIndex());
        int cursorIndex = range.getStartIndex() + 1;
        Iterator<T> iterator = ranged.getData().iterator();
        while (iterator.hasNext()) {
            T node = iterator.next();
            edges.add(new Edge<>(node, String.valueOf(cursorIndex++)));
        }
        return edges;
    }

    private static Connection createEmptyConnection() {
        PageInfo pageInfo = new PageInfo(null, null, false, false);
        return new Connection<>(Collections.emptyList(), pageInfo, 0L);
    }

}
