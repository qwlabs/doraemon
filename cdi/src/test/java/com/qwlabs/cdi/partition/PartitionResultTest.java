package com.qwlabs.cdi.partition;

import com.qwlabs.exceptions.CodeException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PartitionResultTest {
    @Test
    void should_return_stream() {
        Stream<Stream> data = Stream.of(Stream.of("1"), Stream.of("2"));

        var result = PartitionResult.get(data, Stream.class);

        assertThat(result.toList(), is(List.of("1", "2")));
    }

    @Test
    void should_return_set() {
        Stream<Set> data = Stream.of(Set.of("1"), Set.of("2"));

        var result = PartitionResult.get(data, Set.class);

        assertThat(result, is(Set.of("1", "2")));
    }

    @Test
    void should_return_list() {
        Stream<List> data = Stream.of(List.of("1"), List.of("2"));

        var result = PartitionResult.get(data, List.class);

        assertThat(result, is(List.of("1", "2")));
    }

    @Test
    void should_return_integer() {
        Stream<Integer> data = Stream.of(1, 2);

        var result = PartitionResult.get(data, Integer.class);

        assertThat(result, is(3));
    }

    @Test
    void should_return_long() {
        Stream<Long> data = Stream.of(1L, 2L);

        var result = PartitionResult.get(data, Long.class);

        assertThat(result, is(3L));
    }

    @Test
    void should_exception() {
        Stream<Map> data = Stream.of(Map.of("a", "b"));

        var exception = assertThrows(CodeException.class, () -> PartitionResult.get(data, Map.class));

        assertThat(exception.getMessage(), is("DORA000001: Code error: PartitionResult can not support type java.util.Map"));
    }
}
