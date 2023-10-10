package com.qwlabs.lang;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class WeightDistributor {
    private final List<Integer> totals;
    private final List<Integer> requires;
    private TreeMap<Integer, Integer> leftTotals;
    private Map<Integer, Integer> distributed;
    private Map<Distribution, Distribution> distributions;
    private boolean distributedFlag;
    private boolean exceed;

    private WeightDistributor(int total, @NotNull List<@NotNull Integer> requires, boolean exceed) {
        this(List.of(total), requires, exceed);
    }

    private WeightDistributor(@NotNull List<@NotNull Integer> totals, @NotNull List<@NotNull Integer> requires, boolean exceed) {
        this.totals = totals;
        this.requires = requires;
        this.exceed = exceed;
        init();
    }

    private void init() {
        this.distributedFlag = false;
        this.distributed = Maps.newHashMap();
        this.distributions = Maps.newHashMap();
        this.leftTotals = buildLeftTotals();
    }

    public WeightDistributor distribute() {
        if (this.distributedFlag) {
            return this;
        }
        this.distributedFlag = true;
        doDistribute(buildLeftRequires());
        return this;
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
        var weights = calculateWeights(left, leftRequires);
        Map<Integer, Integer> nextLeftRequires = Maps.newHashMap();
        for (Map.Entry<Integer, Integer> entry : leftRequires.entrySet()) {
            var weight = weights.getOrDefault(entry.getKey(), 1);
            int distribution = exceed ? Ints.min(weight, left) : Ints.min(entry.getValue(), weight, left);
            int leftRequired = entry.getValue();
            if (distribution > 0) {
                left -= distribution;
                leftRequired -= distribution;
                var key = Distribution.of(totalEntry.getKey(), entry.getKey());
                distributed.put(entry.getKey(), distributed.getOrDefault(entry.getKey(), 0) + distribution);
                distributions.computeIfAbsent(key, (k) -> k).append(distribution);
            }
            put(nextLeftRequires, entry.getKey(), leftRequired);
        }
        put(leftTotals, totalEntry.getKey(), left);
        doDistribute(nextLeftRequires);
    }

    private Map<Integer, Integer> calculateWeights(int left, Map<Integer, Integer> leftRequires) {
        var tmpLeft = BigDecimal.valueOf(left);
        int scale = String.valueOf(left).length();
        var totalRequires = BigDecimal.valueOf(leftRequires.values().stream().mapToInt(v -> v).sum());
        Map<Integer, Integer> weights = new HashMap<>(leftRequires.size());
        var orderedLefts = C2.stream(leftRequires.entrySet())
            .sorted(Map.Entry.comparingByValue())
            .toList();
        for (Map.Entry<Integer, Integer> entry : orderedLefts) {
            var weight = BigDecimal.valueOf(entry.getValue())
                .divide(totalRequires, scale, RoundingMode.UP)
                .multiply(tmpLeft)
                .setScale(0, RoundingMode.UP)
                .intValue();
            weights.put(entry.getKey(), Math.min(left, weight));
            left -= weight;
        }
        return weights;
    }

    public Set<Distribution> getDistributions() {
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

    public static WeightDistributor of(int total, @NotNull List<@NotNull Integer> requires) {
        return new WeightDistributor(total, requires, false);
    }

    public static WeightDistributor of(int total, @NotNull List<@NotNull Integer> requires, boolean exceed) {
        return new WeightDistributor(total, requires, exceed);
    }

    public static WeightDistributor of(@NotNull List<@NotNull Integer> totals, @NotNull List<@NotNull Integer> requires) {
        return new WeightDistributor(totals, requires, false);
    }

    public static WeightDistributor of(@NotNull List<@NotNull Integer> totals, @NotNull List<@NotNull Integer> requires, boolean exceed) {
        return new WeightDistributor(totals, requires, exceed);
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
