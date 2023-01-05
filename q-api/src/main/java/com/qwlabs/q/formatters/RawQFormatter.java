package com.qwlabs.q.formatters;

import com.qwlabs.lang.C2;
import com.qwlabs.q.conditions.AndQCondition;
import com.qwlabs.q.conditions.EQQCondition;
import com.qwlabs.q.conditions.EmptyQCondition;
import com.qwlabs.q.conditions.InQCondition;
import com.qwlabs.q.conditions.IsNullQCondition;
import com.qwlabs.q.conditions.LikeQCondition;
import com.qwlabs.q.conditions.NumberRangeQCondition;
import com.qwlabs.q.conditions.OrQCondition;
import com.qwlabs.q.conditions.QCondition;

import java.util.Map;

public class RawQFormatter {
    public static final String DIALECT = "raw";

    public static final RawQFormatter INSTANCE = new RawQFormatter();
    private static final Map<Class<? extends QCondition>, QFormatter> FORMATTERS = createFormatters();

    private static Map<Class<? extends QCondition>, QFormatter> createFormatters() {
        return Map.of(
                AndQCondition.class, (condition) -> {
                    var con = (AndQCondition) condition;
                    return String.format("and:%s", C2.list(con.getConditions(), INSTANCE::format));
                },
                OrQCondition.class, (condition) -> {
                    var con = (OrQCondition) condition;
                    return String.format("or:%s", C2.list(con.getConditions(), INSTANCE::format));
                },
                EQQCondition.class, (condition) -> {
                    var con = (EQQCondition) condition;
                    if (con.getNumberRight() != null) {
                        return String.format("%s=%s", con.getLeft(), con.getNumberRight());
                    }
                    return String.format("%s='%s'", con.getLeft(), con.getStringRight());
                },
                NumberRangeQCondition.class, (condition) -> {
                    var con = (NumberRangeQCondition) condition;
                    return String.format("%s=%s", con.getLeft(), con.getRange());
                },
                LikeQCondition.class, (condition) -> {
                    var con = (LikeQCondition) condition;
                    return String.format("%s like '%s'", con.getLeft(), con.getRight());
                },
                IsNullQCondition.class, (condition) -> {
                    var con = (IsNullQCondition) condition;
                    return String.format("%s is null", con.getLeft());
                },
                InQCondition.class, (condition) -> {
                    var con = (InQCondition) condition;
                    return String.format("%s in (%s)", con.getLeft(), con.getRight());
                },
                EmptyQCondition.class, (condition) -> "empty"
        );
    }

    public String format(QCondition condition) {
        return FORMATTERS.get(condition.getClass()).format(condition);
    }
}
