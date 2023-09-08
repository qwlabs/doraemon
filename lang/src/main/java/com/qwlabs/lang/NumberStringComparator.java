package com.qwlabs.lang;

import java.util.Comparator;

public class NumberStringComparator implements Comparator<String> {
    public static final NumberStringComparator CHECK_NULL = new NumberStringComparator(true);
    public static final NumberStringComparator NOT_CHECK_NULL = new NumberStringComparator(false);

    private final boolean checkNull;

    public NumberStringComparator(boolean checkNull) {
        this.checkNull = checkNull;
    }

    @Override
    public int compare(String o1, String o2) {
        if (checkNull) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }
        }
        var length1 = o1.length();
        var length2 = o2.length();
        if (length1 > length2) {
            return 1;
        }
        if (length1 < length2) {
            return -1;
        }
        return o1.compareTo(o2);
    }
}
