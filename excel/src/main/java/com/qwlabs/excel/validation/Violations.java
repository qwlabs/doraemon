package com.qwlabs.excel.validation;


import com.alibaba.excel.metadata.Cell;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class Violations {
    private final Map<Cell, List<String>> violations;

    public Violations() {
        this.violations = Maps.newHashMap();
    }
}
