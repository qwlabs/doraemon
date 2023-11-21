package com.qwlabs.excel.annotations;

public @interface SheetName {
    String value() default "Sheet1";
}
