package com.qwlabs.q.conditions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikeQCondition implements QCondition {
    private final String left;
    private final String right;
}
