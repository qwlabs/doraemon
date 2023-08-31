package com.qwlabs.lang;

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
public class C2Benchmark {
    private List<String> listData;

    @Setup
    public void up() {
        this.listData = Stream.iterate(1, v -> v + 1).limit(1_000_000).map(Object::toString).toList();
    }

    /**
     * 0.001 ±(99.9%) 0.002 ops/ms [Average]
     *
     */
    @Benchmark
    public void toSet(Blackhole bh) {
        var result = C2.set(listData);
        assertThat(result.size(), is(this.listData.size()));
    }


}
