package com.qwlabs.q.conditions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrQCondition implements QCondition {
    private final List<? extends QCondition> conditions;
}
