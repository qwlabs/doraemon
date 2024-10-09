package com.qwlabs.jakarta.data;

import jakarta.data.page.Page;
import jakarta.data.page.impl.PageRecord;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.function.Function;

public interface Pages {
    static <E, R> Page<R> map(@NotNull Page<E> page, @NotNull Function<E, R> mapper) {
        List<R> content = page.stream().map(mapper).toList();
        return new PageRecord<>(page.pageRequest(), content, page.totalElements());
    }
}
