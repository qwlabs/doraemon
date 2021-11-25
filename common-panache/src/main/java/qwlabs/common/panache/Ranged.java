package qwlabs.common.panache;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Range;
import io.smallrye.common.constraint.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Ranged<T> {
    private final Stream<T> data;
    private final Long totalCount;
    private final Range range;

    public Ranged(Stream<T> data, Long totalCount, Range range) {
        this.data = data;
        this.totalCount = totalCount;
        this.range = range;
    }

    public Stream<T> getData() {
        return this.data;
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
                .map(m -> new Ranged<>(data.map(mapper), totalCount, range))
                .orElseGet(() -> (Ranged<R>) this);
    }

    public static <T> Ranged<T> of(PanacheQuery<T> pageQuery, Range range) {
        return new Ranged<>(pageQuery.range(range.getStartIndex(), range.getLastIndex()).stream(),
                pageQuery.count(), range);
    }
}
