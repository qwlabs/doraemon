package com.qwlabs.q.parsers.antlr4;

import com.google.common.base.Joiner;
import com.google.common.collect.Range;
import com.qwlabs.q.conditions.EQQCondition;
import com.qwlabs.q.conditions.InQCondition;
import com.qwlabs.q.conditions.IsNullQCondition;
import com.qwlabs.q.conditions.LikeQCondition;
import com.qwlabs.q.conditions.NumberRangeQCondition;
import com.qwlabs.q.conditions.QCondition;
import com.qwlabs.q.parsers.antlr4.g4.QueryExpressionParser;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.qwlabs.q.parsers.antlr4.Antlr4Utils.nodeToNumber;
import static com.qwlabs.q.parsers.antlr4.Antlr4Utils.nodeToString;


public final class Antlr4QConditionBuilders {
    private static final List<QConditionBuilder> CONDITION_BUILDERS = createConditionBuilders();
    private static final String IN_QUERY_RIGHT_ITEM_JOINER = ",";

    private Antlr4QConditionBuilders() {
    }

    private static List<QConditionBuilder> createConditionBuilders() {
        return List.of(
                new EQQConditionBuilder(),
                new GTQConditionBuilder(),
                new GTEQConditionBuilder(),
                new LTQConditionBuilder(),
                new LTEQConditionBuilder(),
                new IsNullQConditionBuilder(),
                new InQConditionBuilder(),
                new LikeQConditionBuilder()
        );
    }

    private interface QConditionBuilder {
        boolean matches(QueryExpressionParser.ConditionRightContext rightContext);

        QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext);
    }

    private static class EQQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.EQ() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            return EQQCondition.builder()
                    .left(left)
                    .numberRight(nodeToNumber(rightContext.NUMBER()))
                    .stringRight(nodeToString(rightContext.STRING()))
                    .build();
        }
    }

    private static class GTQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.GT() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            return create(left, Range.greaterThan(nodeToNumber(rightContext.NUMBER())));
        }
    }

    private static class GTEQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.GTE() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            return create(left, Range.atLeast(nodeToNumber(rightContext.NUMBER())));
        }
    }

    private static class LTQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.LT() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            return create(left, Range.lessThan(nodeToNumber(rightContext.NUMBER())));
        }
    }

    private static class LTEQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.LTE() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            return create(left, Range.atMost(nodeToNumber(rightContext.NUMBER())));
        }
    }


    private static class IsNullQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.IS() != null && rightContext.NULL() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            return IsNullQCondition.builder()
                    .left(left)
                    .build();
        }
    }

    private static class InQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.IN() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            var inItems = rightContext.inExpr().inItem().stream()
                    .map(item -> item.STRING() != null ? item.STRING().toString() : item.NUMBER().toString())
                    .collect(Collectors.toList());
            var itemJoinString = Joiner.on(IN_QUERY_RIGHT_ITEM_JOINER).join(inItems);
            return InQCondition.builder()
                    .left(left)
                    .right(itemJoinString)
                    .build();
        }
    }


    private static class LikeQConditionBuilder implements QConditionBuilder {

        @Override
        public boolean matches(QueryExpressionParser.ConditionRightContext rightContext) {
            return rightContext.LIKE() != null;
        }

        @Override
        public QCondition build(String left, QueryExpressionParser.ConditionRightContext rightContext) {
            return LikeQCondition.builder()
                    .left(left)
                    .right(nodeToString(rightContext.STRING()))
                    .build();
        }
    }

    private static NumberRangeQCondition create(String left, Range<BigDecimal> range) {
        return NumberRangeQCondition.builder()
                .left(left)
                .range(range)
                .build();
    }

    public static QCondition build(QueryExpressionParser.ConditionContext ctx) {
        var left = ctx.conditionLeft().getText();
        var right = ctx.conditionRight();
        return CONDITION_BUILDERS.stream()
                .filter(builder -> builder.matches(right))
                .map(builder -> builder.build(left, right))
                .findFirst()
                .orElse(null);
    }
}
