package com.qwlabs.lang;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class AverageDistributor {
    private final List<Integer> totals;
    private final List<Integer> requires;
    private TreeMap<Integer, Integer> leftTotals;
    private Map<Integer, Integer> distributed;
    private Map<Distribution, Distribution> distributions;
    private boolean distributedFlag;

    private AverageDistributor(int total, @NotNull List<@NotNull Integer> requires) {
        this(List.of(total), requires);
    }

    private AverageDistributor(@NotNull List<@NotNull Integer> totals, @NotNull List<@NotNull Integer> requires) {
        this.totals = totals;
        this.requires = requires;
        init();
    }

    private void init() {
        this.distributedFlag = false;
        this.distributed = Maps.newHashMap();
        this.distributions = Maps.newHashMap();
        this.leftTotals = buildLeftTotals();
    }

    public void distribute() {
        if (this.distributedFlag) {
            return;
        }
        this.distributedFlag = true;
        doDistribute(buildLeftRequires());
    }

    private TreeMap<Integer, Integer> buildLeftTotals() {
        TreeMap<Integer, Integer> leftTotals = Maps.newTreeMap(Comparator.<Integer>naturalOrder());
        for (int index = 0; index < totals.size(); index++) {
            put(leftTotals, index, totals.get(index));
        }
        return leftTotals;
    }

    private Map<Integer, Integer> buildLeftRequires() {
        Map<Integer, Integer> leftRequires = Maps.newHashMap();
        for (int index = 0; index < requires.size(); index++) {
            put(leftRequires, index, requires.get(index));
        }
        return leftRequires;
    }

    private void put(Map<Integer, Integer> mapping, Integer key, Integer value) {
        if (Objects.isNull(value) || value <= 0) {
            mapping.remove(key);
            return;
        }
        mapping.put(key, value);
    }

    private void doDistribute(Map<Integer, Integer> leftRequires) {
        if (C2.isEmpty(leftTotals)) {
            return;
        }
        if (C2.isEmpty(leftRequires)) {
            return;
        }
        var totalEntry = leftTotals.firstEntry();
        int left = totalEntry.getValue();
        int avg = Integer.max(left / leftRequires.size(), 1);
        Map<Integer, Integer> nextLeftRequires = Maps.newHashMap();
        for (Map.Entry<Integer, Integer> entry : leftRequires.entrySet()) {
            int distribution = Ints.min(entry.getValue(), avg, left);
            int leftRequired = entry.getValue();
            if (distribution > 0) {
                left -= distribution;
                leftRequired -= distribution;
                var key = Distribution.of(totalEntry.getKey(), entry.getKey());
                distributed.put(entry.getKey(), distributed.getOrDefault(entry.getKey(), 0) + distribution);
                distributions.computeIfAbsent(key, (k)->k).append(distribution);
            }
            put(nextLeftRequires, entry.getKey(), leftRequired);
        }
        put(leftTotals, totalEntry.getKey(), left);
        doDistribute(nextLeftRequires);
    }

    public Set<Distribution> getDistributions(){
        return distributions.keySet();
    }

    public int getDistributed(int index) {
        return distributed.getOrDefault(index, 0);
    }

    public int getLeft() {
        return leftTotals.values().stream()
            .mapToInt(v -> v)
            .sum();
    }

    public int getLeft(int index) {
        return leftTotals.getOrDefault(index, 0);
    }

    public static AverageDistributor of(int total, @NotNull List<@NotNull Integer> requires) {
        return new AverageDistributor(total, requires);
    }

    public static AverageDistributor of(@NotNull List<@NotNull Integer> totals, @NotNull List<@NotNull Integer> requires) {
        return new AverageDistributor(totals, requires);
    }

    @Getter
    public static class Distribution {
        private final int from;
        private final int to;
        private int value;

        public Distribution(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public static Distribution of(int from, int to) {
            return new Distribution(from, to);
        }

        public void append(int value) {
            this.value += value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Distribution that = (Distribution) o;
            return from == that.from && to == that.to;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
}
