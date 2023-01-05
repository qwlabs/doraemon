// Generated from /Users/pipinet/workspace/doraemon/q-parser-antlr4/src/main/java/com/qwlabs/q/parsers/antlr4/g4/QueryExpression.g4 by ANTLR 4.10.1
package com.qwlabs.q.parsers.antlr4.g4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link QueryExpressionParser}.
 */
public interface QueryExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link QueryExpressionParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(QueryExpressionParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryExpressionParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(QueryExpressionParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryExpressionParser#conditions}.
	 * @param ctx the parse tree
	 */
	void enterConditions(QueryExpressionParser.ConditionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryExpressionParser#conditions}.
	 * @param ctx the parse tree
	 */
	void exitConditions(QueryExpressionParser.ConditionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryExpressionParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(QueryExpressionParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryExpressionParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(QueryExpressionParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryExpressionParser#conditionLeft}.
	 * @param ctx the parse tree
	 */
	void enterConditionLeft(QueryExpressionParser.ConditionLeftContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryExpressionParser#conditionLeft}.
	 * @param ctx the parse tree
	 */
	void exitConditionLeft(QueryExpressionParser.ConditionLeftContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryExpressionParser#conditionRight}.
	 * @param ctx the parse tree
	 */
	void enterConditionRight(QueryExpressionParser.ConditionRightContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryExpressionParser#conditionRight}.
	 * @param ctx the parse tree
	 */
	void exitConditionRight(QueryExpressionParser.ConditionRightContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryExpressionParser#inExpr}.
	 * @param ctx the parse tree
	 */
	void enterInExpr(QueryExpressionParser.InExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryExpressionParser#inExpr}.
	 * @param ctx the parse tree
	 */
	void exitInExpr(QueryExpressionParser.InExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryExpressionParser#inItem}.
	 * @param ctx the parse tree
	 */
	void enterInItem(QueryExpressionParser.InItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryExpressionParser#inItem}.
	 * @param ctx the parse tree
	 */
	void exitInItem(QueryExpressionParser.InItemContext ctx);
}