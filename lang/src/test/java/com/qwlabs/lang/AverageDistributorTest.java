package com.qwlabs.lang;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AverageDistributorTest {
    @Test
    void should_single_total_1() {
        var distributor = AverageDistributor.of(1, List.of(1));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(0));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributions().size(), is(1));
    }

    @Test
    void should_single_total_2() {
        var distributor = AverageDistributor.of(2, List.of(1));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(1));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributions().size(), is(1));
    }

    @Test
    void should_single_total_3() {
        var distributor = AverageDistributor.of(2, List.of(1, 1));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(0));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributed(1), is(1));
        assertThat(distributor.getDistributions().size(), is(2));
    }

    @Test
    void should_single_total_4() {
        var distributor = AverageDistributor.of(2, List.of(1, 2));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(0));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributed(1), is(1));
        assertThat(distributor.getDistributions().size(), is(2));
    }

    @Test
    void should_single_total_5() {
        var distributor = AverageDistributor.of(10, List.of(1, 100));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(0));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributed(1), is(9));
        assertThat(distributor.getDistributions().size(), is(2));
    }


    @Test
    void should_multi_total_1() {
        var distributor = AverageDistributor.of(List.of(0, 1), List.of(1));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(0));
        assertThat(distributor.getLeft(0), is(0));
        assertThat(distributor.getLeft(1), is(0));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributions().size(), is(1));
    }

    @Test
    void should_multi_total_2() {
        var distributor = AverageDistributor.of(List.of(1, 1), List.of(1, 1));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(0));
        assertThat(distributor.getLeft(0), is(0));
        assertThat(distributor.getLeft(1), is(0));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributed(1), is(1));
        assertThat(distributor.getDistributions().size(), is(2));
    }

    @Test
    void should_multi_total_3() {
        var distributor = AverageDistributor.of(List.of(2, 1), List.of(1, 2));
        distributor.distribute();
        assertThat(distributor.getLeft(), is(0));
        assertThat(distributor.getLeft(0), is(0));
        assertThat(distributor.getLeft(1), is(0));
        assertThat(distributor.getDistributed(0), is(1));
        assertThat(distributor.getDistributed(1), is(2));
        assertThat(distributor.getDistributions().size(), is(3));
    }
}
