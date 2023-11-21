package com.qwlabs.excel.annotations;

public @interface CellIndex {
    int index() default -1;
    String[] validNames() default {};
}
