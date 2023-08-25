package com.qwlabs.lang;

import com.google.common.base.Joiner;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@State(Scope.Benchmark)
public class BufferedCallBenchmark {
    private List<String> data;

    @Setup
    public void up() {
        this.data = Stream.iterate(1, v -> v + 1).limit(1_000_000).map(Object::toString).toList();
    }

    /**
     * 0.009 ± 0.004  ops/ms
     *
     */
    @Benchmark
    public void sync(Blackhole bh) {
        var bufferedCall = BufferedCall.<String, String>of();
        bufferedCall.add(data);
        bufferedCall.add(data.stream());
        var result = bufferedCall.call(Joiner.on(",")::join).toList();
        assertThat(result.size(), is(2000));
        bh.consume(result);
    }

    /**
     * 0.007 ± 0.024  ops/ms
     *
     */
    @Benchmark
    public void async(Blackhole bh) {
        var bufferedCall = BufferedCall.<String, String>of();
        bufferedCall.add(data);
        bufferedCall.add(data.stream());
        var result = bufferedCall.call(Joiner.on(",")::join).toList();
        assertThat(result.size(), is(2000));
        bh.consume(result);
    }
}
