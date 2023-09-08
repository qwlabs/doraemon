package com.qwlabs.tree;

import com.google.common.collect.Lists;
import com.qwlabs.lang.NumberStringComparator;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

class LocationComparatorTest {
    @Test
    void should_compare_without_node_comparator() {
        List<Location<String>> locations = Lists.newArrayList(
            null,
            null,
            Location.of(List.of()),
            Location.of(List.of()),
            Location.of(List.of("1")),
            Location.of(List.of("1")),
            Location.of(List.of("10")),
            Location.of(List.of("2")),
            Location.of(List.of("2")),
            Location.of(List.of("1", "2")),
            Location.of(List.of("2", "1")),
            Location.of(List.of("1", "2", "3"))
        );
        Collections.shuffle(locations);
        locations.sort(LocationComparator.of());

        assertNull(locations.get(0));
        assertNull(locations.get(1));
        assertThat(locations.get(2), is(Location.of()));
        assertThat(locations.get(3), is(Location.of()));
        assertThat(locations.get(4), is(Location.of("1")));
        assertThat(locations.get(5), is(Location.of("1")));
        assertThat(locations.get(6), is(Location.of("10")));
        assertThat(locations.get(7), is(Location.of("2")));
        assertThat(locations.get(8), is(Location.of("2")));
        assertThat(locations.get(9), is(Location.of("1", "2")));
        assertThat(locations.get(10), is(Location.of("2", "1")));
        assertThat(locations.get(11), is(Location.of("1", "2", "3")));
    }

    @Test
    void should_compare_with_node_comparator() {
        List<Location<String>> locations = Lists.newArrayList(
            null,
            null,
            Location.of(List.of()),
            Location.of(List.of()),
            Location.of(List.of("1")),
            Location.of(List.of("1")),
            Location.of(List.of("10")),
            Location.of(List.of("2")),
            Location.of(List.of("2")),
            Location.of(List.of("1", "2")),
            Location.of(List.of("2", "1")),
            Location.of(List.of("2", "10")),
            Location.of(List.of("2", "12")),
            Location.of(List.of("2", "20")),
            Location.of(List.of("1", "2", "3"))
        );
        Collections.shuffle(locations);
        locations.sort(LocationComparator.of(NumberStringComparator.NOT_CHECK_NULL));

        assertNull(locations.get(0));
        assertNull(locations.get(1));
        assertThat(locations.get(2), is(Location.of()));
        assertThat(locations.get(3), is(Location.of()));
        assertThat(locations.get(4), is(Location.of("1")));
        assertThat(locations.get(5), is(Location.of("1")));
        assertThat(locations.get(6), is(Location.of("2")));
        assertThat(locations.get(7), is(Location.of("2")));
        assertThat(locations.get(8), is(Location.of("10")));
        assertThat(locations.get(9), is(Location.of("1", "2")));
        assertThat(locations.get(10), is(Location.of("2", "1")));
        assertThat(locations.get(11), is(Location.of("2", "10")));
        assertThat(locations.get(12), is(Location.of("2", "12")));
        assertThat(locations.get(13), is(Location.of("2", "20")));
        assertThat(locations.get(14), is(Location.of("1", "2", "3")));
    }
}
