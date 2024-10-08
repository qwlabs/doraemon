package com.qwlabs.jakarta.data;

import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class LimitsTest {
    @Test
    void should_of() {
        var limit = Limits.of("1", 10);
        assertThat(limit.startAt(), is(1L));
        assertThat(limit.maxResults(), is(10));
    }
}
