package com.qwlabs.lang;

import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AverageDistributor {
    private final int total;
    private final List<Integer> requires;
    private int left;
    private Map<Integer, Integer> distributions;
    private boolean distributed;

    private AverageDistributor(int total, @NotNull List<@NotNull Integer> requires) {
        this.total = total;
        this.requires = requires;
        init();
    }

    private void init() {
        this.distributed = false;
        this.left = total;
        this.distributions = Maps.newHashMap();
    }

    public void distribute() {
        if (this.distributed) {
            return;
        }
        this.distributed = true;
        doDistribute(buildLeftRequires());
    }

    private Map<Integer, Integer> buildLeftRequires() {
        Map<Integer, Integer> leftRequires = Maps.newHashMap();
        for (int index = 0; index < requires.size(); index++) {
            putLeftRequires(leftRequires, index, requires.get(index));
        }
        return leftRequires;
    }

    private void putLeftRequires(Map<Integer, Integer> leftRequires, Integer index, Integer required) {
        if (Objects.isNull(required) || required <= 0) {
            return;
        }
        leftRequires.put(index, required);
    }

    private void doDistribute(Map<Integer, Integer> leftRequires) {
        if (left <= 0) {
            return;
        }
        if (C2.isEmpty(leftRequires)) {
            return;
        }
        int avg = Integer.max(left / leftRequires.size(), 1);
        Map<Integer, Integer> nextLeftRequires = Maps.newHashMap();
        for (Map.Entry<Integer, Integer> entry : leftRequires.entrySet()) {
            int distribution = Ints.min(entry.getValue(), avg, left);
            if (distribution <= 0) {
                break;
            }
            left -= distribution;
            int leftRequired = entry.getValue() - distribution;
            distributions.put(entry.getKey(), distributions.getOrDefault(entry.getKey(), 0) + distribution);
            putLeftRequires(nextLeftRequires, entry.getKey(), leftRequired);
        }
        doDistribute(nextLeftRequires);
    }

    public int getDistribution(int index) {
        return distributions.getOrDefault(index, 0);
    }

    public int getLeft() {
        return left;
    }

    public Map<Integer, Integer> getDistributions() {
        return distributions;
    }

    public static AverageDistributor of(int total, @NotNull List<@NotNull Integer> requires) {
        return new AverageDistributor(total, requires);
    }
}
