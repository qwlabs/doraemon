// Generated from java-escape by ANTLR 4.11.1
package com.qwlabs.q.parsers.antlr4.g4;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link QueryExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface QueryExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link QueryExpressionParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(QueryExpressionParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryExpressionParser#conditions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditions(QueryExpressionParser.ConditionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryExpressionParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(QueryExpressionParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryExpressionParser#conditionLeft}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionLeft(QueryExpressionParser.ConditionLeftContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryExpressionParser#conditionRight}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionRight(QueryExpressionParser.ConditionRightContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryExpressionParser#inExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInExpr(QueryExpressionParser.InExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link QueryExpressionParser#inItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInItem(QueryExpressionParser.InItemContext ctx);
}