package com.qwlabs.tree;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class LocationComparator<N extends Comparable<N>> implements Comparator<Location<N>> {
    private final Comparator<N> nodeComparator;

    public LocationComparator() {
        this(null);
    }

    public LocationComparator(Comparator<N> nodeComparator) {
        this.nodeComparator = Optional.ofNullable(nodeComparator)
            .orElseGet(() -> Comparable::compareTo);
    }

    @Override
    public int compare(Location<N> o1, Location<N> o2) {
        if (Objects.isNull(o1) && Objects.isNull(o2)) {
            return 0;
        }
        if (Objects.isNull(o1)) {
            return -1;
        }
        if (Objects.isNull(o2)) {
            return 1;
        }
        if (o1.size() > o2.size()) {
            return 1;
        }
        if (o1.size() < o2.size()) {
            return -1;
        }
        for (var index = 0; index < o1.size(); index++) {
            int indexCompare = nodeComparator.compare(o1.get(index), o2.get(index));
            if (indexCompare != 0) {
                return indexCompare;
            }
        }
        return 0;
    }

    public static <N extends Comparable<N>> LocationComparator<N> of() {
        return new LocationComparator<>();
    }

    public static <N extends Comparable<N>> LocationComparator<N> of(Comparator<N> nodeComparator) {
        return new LocationComparator<>(nodeComparator);
    }
}
