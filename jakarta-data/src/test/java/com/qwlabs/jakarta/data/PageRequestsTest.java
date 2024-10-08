package com.qwlabs.jakarta.data;

import jakarta.data.page.PageRequest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PageRequestsTest {
    @Test
    void should_of() {
        checkRequest(null, 2, 1L);
        checkRequest("0", 2, 1L);
        checkRequest("1", 2, 1L);
        checkRequest("1", 3, 1L);
        checkRequest("1", 10, 1L);
        checkRequest("2", 1, 3L);
        checkRequest("2", 2, 2L);
        checkRequest("2", 3, 1L);
        checkRequest("2", 4, 1L);
    }

    void checkRequest(String after, Integer first, long page) {
        var pageRequest = PageRequests.of(after, first);
        assertThat(pageRequest.page(), is(page));
        assertThat(pageRequest.size(), is(first));
        assertThat(pageRequest.requestTotal(), is(true));
        assertThat(pageRequest.mode(), is(PageRequest.Mode.OFFSET));
    }
}
