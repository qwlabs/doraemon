package com.qwlabs.tree;

import java.util.Comparator;
import java.util.Objects;

public class LocationComparator<N extends Comparable<N>> implements Comparator<Location<N>> {
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
            var v1 = o1.get(index);
            var v2 = o2.get(index);
            int indexCompare = Objects.compare(v1, v2, Comparator.naturalOrder());
            if (indexCompare != 0) {
                return indexCompare;
            }
        }
        return 0;
    }

    public static <N extends Comparable<N>> LocationComparator<N> of() {
        return new LocationComparator<>();
    }
}
