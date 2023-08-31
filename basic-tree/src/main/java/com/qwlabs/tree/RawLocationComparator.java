package com.qwlabs.tree;

import com.google.common.base.Splitter;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static com.qwlabs.tree.Location.DEFAULT_SEPARATOR;

public class RawLocationComparator implements Comparator<String> {
    public static final RawLocationComparator DEFAULT = new RawLocationComparator();
    private final String separator;

    public RawLocationComparator() {
        this(DEFAULT_SEPARATOR);
    }

    public RawLocationComparator(String separator) {
        this.separator = Optional.ofNullable(separator).orElse(DEFAULT_SEPARATOR);
    }

    @Override
    public int compare(String o1, String o2) {
        if (Objects.isNull(o1) && Objects.isNull(o2)) {
            return 0;
        }
        if (Objects.isNull(o1)) {
            return -1;
        }
        if (Objects.isNull(o2)) {
            return 1;
        }
        if (o1.isBlank() && o2.isBlank()) {
            return 0;
        }
        if (o1.isBlank()) {
            return -1;
        }
        if (o2.isBlank()) {
            return 1;
        }
        var o1List = Splitter.on(separator).splitToList(o1);
        var o2List = Splitter.on(separator).splitToList(o2);
        if (o1List.size() > o2List.size()) {
            return 1;
        }
        if (o1List.size() < o2List.size()) {
            return -1;
        }
        for (var index = 0; index < o1List.size(); index++) {
            var v1 = o1List.get(index);
            var v2 = o2List.get(index);
            int indexCompare = Objects.compare(v1, v2, Comparator.naturalOrder());
            if (indexCompare != 0) {
                return indexCompare;
            }
        }
        return 0;
    }
}
