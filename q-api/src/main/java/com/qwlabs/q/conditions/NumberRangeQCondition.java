package com.qwlabs.q.conditions;

import com.google.common.collect.Range;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class NumberRangeQCondition implements QCondition {
    private final String left;
    private final Range<BigDecimal> range;
}
