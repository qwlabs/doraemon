package com.qwlabs.excel.annotations;

public @interface CellName {
    String[] value() default {};

    boolean required() default false;
}
