package com.qwlabs.tree;

import com.google.common.collect.Lists;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Collections;
import java.util.List;

@State(Scope.Benchmark)
public class LocationComparatorBenchmark {

    /**
     * 47.049 ±(99.9%) 55.574 ops/ms
     *
     */
    @Benchmark
    public void measure_local_compare(Blackhole bh) {
        List<Location<String>> locations = Lists.newArrayList(
            null,
            null,
            Location.of(List.of()),
            Location.of(List.of()),
            Location.of(List.of("1")),
            Location.of(List.of("1")),
            Location.of(List.of("2")),
            Location.of(List.of("2")),
            Location.of(List.of("3")),
            Location.of(List.of("1", "1")),
            Location.of(List.of("1", "2")),
            Location.of(List.of("1", "3")),
            Location.of(List.of("2", "1")),
            Location.of(List.of("2", "2")),
            Location.of(List.of("2", "3")),
            Location.of(List.of("2", "4")),
            Location.of(List.of("2", "5")),
            Location.of(List.of("1", "2", "3")),
            Location.of(List.of("1", "2", "3", "4")),
            Location.of(List.of("1", "2", "3", "5"))
        );
        Collections.shuffle(locations);
        locations.sort(LocationComparator.of());
    }
}
