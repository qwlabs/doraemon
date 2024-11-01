package com.qwlabs.jakarta.data;

import com.google.common.primitives.Longs;
import jakarta.annotation.Nullable;
import jakarta.data.page.PageRequest;

import java.util.Optional;

public interface PageRequests {

    static PageRequest of(int first) {
        return of(null, first);
    }

    static PageRequest of(@Nullable String after, int first) {
        if (first <= 0) {
            throw new IllegalArgumentException("first can not be less than 1");
        }
        var startAt = Optional.ofNullable(after).map(Longs::tryParse).orElse(0L);
        var pageNumber = (startAt + first) / first;
        return PageRequest.ofPage(pageNumber, first, true);
    }

    static int firstResult(PageRequest request){
        return Long.valueOf((request.page() - 1)).intValue() * request.size();
    }

    static int maxResults(PageRequest request){
        return request.size();
    }
}
