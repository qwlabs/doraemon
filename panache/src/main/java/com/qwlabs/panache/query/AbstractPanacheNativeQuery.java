package com.qwlabs.panache.query;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Range;

public abstract class AbstractPanacheNativeQuery<Entity> implements PanacheQuery<Entity> {

    private Page page;
    protected Range range;


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
        checkPagination();
        page(page.next());
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> previousPage() {
        checkPagination();
        page(page.previous());
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> firstPage() {

        checkPagination();
        page(page.first());
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> lastPage() {
        checkPagination();
        page(page.index(pageCount() - 1));
        return (PanacheQuery<T>) this;
    }

    @Override
    public boolean hasNextPage() {
        checkPagination();
        return page.index < (pageCount() - 1);
    }

    @Override
    public boolean hasPreviousPage() {
        checkPagination();
        return page.index > 0;
    }

    @Override
    public int pageCount() {
        checkPagination();
        long count = count();
        if (count == 0) {
            return 1;
        }
        return (int) Math.ceil((double) count / (double) page.size);
    }

    @Override
    public Page page() {
        checkPagination();
        return page;
    }

    private void checkPagination() {
        if (page == null) {
            throw new UnsupportedOperationException("Cannot call a page related method, " +
                    "call page(Page) or page(int, int) to initiate pagination first");
        }
        if (range != null) {
            throw new UnsupportedOperationException("Cannot call a page related method in a ranged query, " +
                    "call page(Page) or page(int, int) to initiate pagination first");
        }
    }

    @Override
    public <T extends Entity> PanacheQuery<T> range(int startIndex, int lastIndex) {
        this.range = Range.of(startIndex, lastIndex);
        this.page = new Page(startIndex, lastIndex - startIndex + 1);
        return (PanacheQuery<T>) this;
    }

}