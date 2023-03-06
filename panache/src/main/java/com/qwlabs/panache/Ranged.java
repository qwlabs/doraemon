package com.qwlabs.panache;

import com.qwlabs.lang.C2;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Range;
import io.smallrye.common.constraint.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Ranged<T> {
    private final List<T> data;
    private final Long totalCount;
    private final Range range;

    public Ranged(List<T> data, Long totalCount, Range range) {
        this.data = data;
        this.totalCount = totalCount;
        this.range = range;
    }

    public List<T> getData() {
        return this.data;
    }

    public Stream<T> getStreamData() {
        return C2.stream(this.data);
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public Range getRange() {
        return range;
    }

    public boolean hasPreviousPage() {
        return this.totalCount > 0 && range.getStartIndex() > 1;
    }

    public boolean hasNextPage() {
        return range.getLastIndex() < this.totalCount;
    }

    public <R> Ranged<R> map(@Nullable Function<T, R> mapper) {
        return Optional.ofNullable(mapper)
                .map(m -> new Ranged<>(C2.list(getStreamData(), m), totalCount, range))
                .orElseGet(() -> (Ranged<R>) this);
    }

    public <R> Ranged<R> batchMap(@Nullable Function<List<T>, List<R>> mapper) {
        return Optional.ofNullable(mapper)
                .map(m -> new Ranged<>(m.apply(getData()), totalCount, range))
                .orElseGet(() -> (Ranged<R>) this);
    }

    public static <T> Ranged<T> of(PanacheQuery<T> pageQuery, Range range) {
        var dataQuery = pageQuery.range(range.getStartIndex(), range.getLastIndex());
        return new Ranged<>(dataQuery.list(), pageQuery.count(), range);
    }
}
