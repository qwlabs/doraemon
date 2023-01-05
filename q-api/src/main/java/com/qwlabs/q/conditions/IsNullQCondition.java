package com.qwlabs.q.conditions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IsNullQCondition implements QCondition {
    private final String left;
}
