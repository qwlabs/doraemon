package com.qwlabs.tree;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

class RawLocationComparatorTest {

    @Test
    void should_compare_with_default() {
        List<String> locations = Lists.newArrayList(
            null,
            null,
            "",
            "1",
            "1",
            "2",
            "1.2",
            "2.1",
            "1.2.3"
        );
        Collections.shuffle(locations);
        locations.sort(RawLocationComparator.DEFAULT);

        assertNull(locations.get(0));
        assertNull(locations.get(1));
        assertThat(locations.get(2), is(""));
        assertThat(locations.get(3), is("1"));
        assertThat(locations.get(4), is("1"));
        assertThat(locations.get(5), is("2"));
        assertThat(locations.get(6), is("1.2"));
        assertThat(locations.get(7), is("2.1"));
        assertThat(locations.get(8), is("1.2.3"));
    }

}
