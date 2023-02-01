package com.qwlabs.panache.query;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Range;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class EmptyPanacheQuery<Entity> implements PanacheQuery<Entity> {
    private Page page;
    private Range range;

    protected EmptyPanacheQuery() {
    }

    @Override
    public <T> PanacheQuery<T> project(Class<T> type) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> page(Page page) {
        this.page = page;
        this.range = null;
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> page(int pageIndex, int pageSize) {
        this.page = Page.of(pageIndex, pageSize);
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> nextPage() {
        page(page.next());
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> previousPage() {
        page(page.previous());
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> firstPage() {
        page(page.first());
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> lastPage() {
        page(page.index(pageCount() - 1));
        return (PanacheQuery<T>) this;
    }

    @Override
    public boolean hasNextPage() {
        return false;
    }

    @Override
    public boolean hasPreviousPage() {
        return false;
    }

    @Override
    public int pageCount() {
        return 1;
    }

    @Override
    public Page page() {
        return page;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> range(int startIndex, int lastIndex) {
        this.range = Range.of(startIndex, lastIndex);
        // reset the page to its default to be able to switch from page to range
        this.page = null;
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withLock(LockModeType lockModeType) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withHint(String hintName, Object value) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName, Parameters parameters) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName, Map<String, Object> parameters) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public <T extends Entity> List<T> list() {
        return List.of();
    }

    @Override
    public <T extends Entity> Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public <T extends Entity> T firstResult() {
        return null;
    }

    @Override
    public <T extends Entity> Optional<T> firstResultOptional() {
        return Optional.empty();
    }

    @Override
    public <T extends Entity> T singleResult() {
        return null;
    }

    @Override
    public <T extends Entity> Optional<T> singleResultOptional() {
        return Optional.empty();
    }
}
