package com.qwlabs.lang;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BufferedCallTest {

    @Test
    void should_add() {
        var bufferedCall = BufferedCall.<String, String>of();
        bufferedCall.add("1");
        bufferedCall.add("2", "3");
        bufferedCall.add(List.of("4"));
        bufferedCall.add(Set.of("5"));
        bufferedCall.add(Stream.of("6", "7"));

        var result = bufferedCall.call(Joiner.on(",")::join).toList();

        assertThat(result.size(), is(1));
        assertThat(result.get(0), is("1,2,3,4,5,6,7"));
    }

    @Test
    void should_empty_when_no_call() {
        var bufferedCall = BufferedCall.<String, String>of(10);
        List<String> result = call(bufferedCall, Stream.iterate(1, v -> v + 1).limit(0).map(Object::toString));
        assertThat(result.size(), is(0));
    }

    @Test
    void should_one_when_one() {
        var bufferedCall = BufferedCall.<String, String>of(10);
        List<String> result = call(bufferedCall, Stream.iterate(1, v -> v + 1).limit(1).map(Object::toString));
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is("1"));
    }

    @Test
    void should_buffered() {
        var bufferedCall = BufferedCall.<String, String>of(3);
        List<String> result = call(bufferedCall, Stream.iterate(1, v -> v + 1).limit(14).map(Object::toString));
        assertThat(result.size(), is(5));
        assertThat(result.get(0), is("1,2,3"));
        assertThat(result.get(1), is("4,5,6"));
        assertThat(result.get(2), is("7,8,9"));
        assertThat(result.get(3), is("10,11,12"));
        assertThat(result.get(4), is("13,14"));
    }

    private List<String> call(BufferedCall<String, String> bufferedCall, Stream<String> input) {
        input.sequential().forEach(bufferedCall::add);
        return bufferedCall.call(Joiner.on(",")::join).toList();
    }
}
