package com.qwlabs.q.parsers.antlr4;

import com.qwlabs.lang.C2;
import com.qwlabs.q.conditions.AndQCondition;
import com.qwlabs.q.conditions.OrQCondition;
import com.qwlabs.q.conditions.QCondition;
import com.qwlabs.q.parsers.antlr4.g4.QueryExpressionBaseVisitor;
import com.qwlabs.q.parsers.antlr4.g4.QueryExpressionParser;

public class QConditionsVisitor extends QueryExpressionBaseVisitor<QCondition> {
    @Override
    public QCondition visitCondition(QueryExpressionParser.ConditionContext ctx) {
        if (ctx.AND() != null) {
            return AndQCondition.builder()
                    .conditions(C2.list(ctx.condition(), this::visitCondition))
                    .build();
        }
        if (ctx.OR() != null) {
            return OrQCondition.builder()
                    .conditions(C2.list(ctx.condition(), this::visitCondition))
                    .build();
        }
        if (ctx.conditionLeft() != null) {
            return Antlr4QConditionBuilders.build(ctx);
        }
        return this.visitCondition(ctx.condition().get(0));
    }
}
