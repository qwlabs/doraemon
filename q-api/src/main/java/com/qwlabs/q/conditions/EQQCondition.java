package com.qwlabs.q.conditions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class EQQCondition implements QCondition {
    private final String left;
    private final String stringRight;
    private final BigDecimal numberRight;
}
