// Generated from /Users/pipinet/workspace/doraemon/q-parser-antlr4/src/main/java/com/qwlabs/q/parsers/antlr4/g4/QueryExpression.g4 by ANTLR 4.10.1
package com.qwlabs.q.parsers.antlr4.g4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryExpressionParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OPEN_PAREN=1, CLOSE_PAREN=2, OPEN_BRACKET=3, CLOSE_BRACKET=4, EQ=5, GT=6, 
		LT=7, GTE=8, LTE=9, IN=10, AND=11, OR=12, LIKE=13, IS=14, NULL=15, STRING=16, 
		NUMBER=17, DATE=18, TIME=19, DATETIME=20, OPEN_RANGE=21, CLOSE_RANGE=22, 
		COMMA=23, DOT=24, WS=25, IDENTIFIER=26, DOUBLE_QUOTED_STRING=27, SINGLE_QUOTED_STRING=28;
	public static final int
		RULE_query = 0, RULE_conditions = 1, RULE_condition = 2, RULE_conditionLeft = 3, 
		RULE_conditionRight = 4, RULE_inExpr = 5, RULE_inItem = 6;
	private static String[] makeRuleNames() {
		return new String[] {
			"query", "conditions", "condition", "conditionLeft", "conditionRight", 
			"inExpr", "inItem"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'['", "']'", "'='", "'>'", "'<'", "'>='", "'<='", 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "','", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OPEN_PAREN", "CLOSE_PAREN", "OPEN_BRACKET", "CLOSE_BRACKET", "EQ", 
			"GT", "LT", "GTE", "LTE", "IN", "AND", "OR", "LIKE", "IS", "NULL", "STRING", 
			"NUMBER", "DATE", "TIME", "DATETIME", "OPEN_RANGE", "CLOSE_RANGE", "COMMA", 
			"DOT", "WS", "IDENTIFIER", "DOUBLE_QUOTED_STRING", "SINGLE_QUOTED_STRING"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "QueryExpression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public QueryExpressionParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class QueryContext extends ParserRuleContext {
		public ConditionsContext conditions() {
			return getRuleContext(ConditionsContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryExpressionVisitor ) return ((QueryExpressionVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14);
			conditions(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionsContext extends ParserRuleContext {
		public ConditionsContext conditions() {
			return getRuleContext(ConditionsContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public ConditionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).enterConditions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).exitConditions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryExpressionVisitor ) return ((QueryExpressionVisitor<? extends T>)visitor).visitConditions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionsContext conditions() throws RecognitionException {
		return conditions(0);
	}

	private ConditionsContext conditions(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConditionsContext _localctx = new ConditionsContext(_ctx, _parentState);
		ConditionsContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_conditions, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			}
			_ctx.stop = _input.LT(-1);
			setState(21);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ConditionsContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_conditions);
					setState(17);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(18);
					condition(0);
					}
					} 
				}
				setState(23);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public ConditionLeftContext conditionLeft() {
			return getRuleContext(ConditionLeftContext.class,0);
		}
		public ConditionRightContext conditionRight() {
			return getRuleContext(ConditionRightContext.class,0);
		}
		public TerminalNode OPEN_PAREN() { return getToken(QueryExpressionParser.OPEN_PAREN, 0); }
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(QueryExpressionParser.CLOSE_PAREN, 0); }
		public TerminalNode AND() { return getToken(QueryExpressionParser.AND, 0); }
		public TerminalNode OR() { return getToken(QueryExpressionParser.OR, 0); }
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryExpressionVisitor ) return ((QueryExpressionVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		return condition(0);
	}

	private ConditionContext condition(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConditionContext _localctx = new ConditionContext(_ctx, _parentState);
		ConditionContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_condition, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(25);
				conditionLeft();
				setState(26);
				conditionRight();
				}
				break;
			case OPEN_PAREN:
				{
				setState(28);
				match(OPEN_PAREN);
				setState(29);
				condition(0);
				setState(30);
				match(CLOSE_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(42);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(40);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new ConditionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_condition);
						setState(34);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(35);
						match(AND);
						setState(36);
						condition(3);
						}
						break;
					case 2:
						{
						_localctx = new ConditionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_condition);
						setState(37);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(38);
						match(OR);
						setState(39);
						condition(2);
						}
						break;
					}
					} 
				}
				setState(44);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ConditionLeftContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(QueryExpressionParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(QueryExpressionParser.IDENTIFIER, i);
		}
		public List<TerminalNode> DOT() { return getTokens(QueryExpressionParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(QueryExpressionParser.DOT, i);
		}
		public ConditionLeftContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionLeft; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).enterConditionLeft(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).exitConditionLeft(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryExpressionVisitor ) return ((QueryExpressionVisitor<? extends T>)visitor).visitConditionLeft(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionLeftContext conditionLeft() throws RecognitionException {
		ConditionLeftContext _localctx = new ConditionLeftContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_conditionLeft);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(IDENTIFIER);
			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==DOT) {
				{
				{
				setState(46);
				match(DOT);
				setState(47);
				match(IDENTIFIER);
				}
				}
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionRightContext extends ParserRuleContext {
		public TerminalNode LIKE() { return getToken(QueryExpressionParser.LIKE, 0); }
		public TerminalNode STRING() { return getToken(QueryExpressionParser.STRING, 0); }
		public TerminalNode IN() { return getToken(QueryExpressionParser.IN, 0); }
		public InExprContext inExpr() {
			return getRuleContext(InExprContext.class,0);
		}
		public TerminalNode IS() { return getToken(QueryExpressionParser.IS, 0); }
		public TerminalNode NULL() { return getToken(QueryExpressionParser.NULL, 0); }
		public TerminalNode GT() { return getToken(QueryExpressionParser.GT, 0); }
		public TerminalNode NUMBER() { return getToken(QueryExpressionParser.NUMBER, 0); }
		public TerminalNode DATETIME() { return getToken(QueryExpressionParser.DATETIME, 0); }
		public TerminalNode GTE() { return getToken(QueryExpressionParser.GTE, 0); }
		public TerminalNode LT() { return getToken(QueryExpressionParser.LT, 0); }
		public TerminalNode LTE() { return getToken(QueryExpressionParser.LTE, 0); }
		public TerminalNode EQ() { return getToken(QueryExpressionParser.EQ, 0); }
		public ConditionRightContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionRight; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).enterConditionRight(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).exitConditionRight(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryExpressionVisitor ) return ((QueryExpressionVisitor<? extends T>)visitor).visitConditionRight(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionRightContext conditionRight() throws RecognitionException {
		ConditionRightContext _localctx = new ConditionRightContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_conditionRight);
		int _la;
		try {
			setState(69);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LIKE:
				enterOuterAlt(_localctx, 1);
				{
				setState(53);
				match(LIKE);
				setState(54);
				match(STRING);
				}
				break;
			case IN:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				match(IN);
				setState(56);
				inExpr();
				}
				break;
			case IS:
				enterOuterAlt(_localctx, 3);
				{
				setState(57);
				match(IS);
				setState(58);
				match(NULL);
				}
				break;
			case GT:
				enterOuterAlt(_localctx, 4);
				{
				setState(59);
				match(GT);
				setState(60);
				_la = _input.LA(1);
				if ( !(_la==NUMBER || _la==DATETIME) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case GTE:
				enterOuterAlt(_localctx, 5);
				{
				setState(61);
				match(GTE);
				setState(62);
				_la = _input.LA(1);
				if ( !(_la==NUMBER || _la==DATETIME) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 6);
				{
				setState(63);
				match(LT);
				setState(64);
				_la = _input.LA(1);
				if ( !(_la==NUMBER || _la==DATETIME) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case LTE:
				enterOuterAlt(_localctx, 7);
				{
				setState(65);
				match(LTE);
				setState(66);
				_la = _input.LA(1);
				if ( !(_la==NUMBER || _la==DATETIME) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			case EQ:
				enterOuterAlt(_localctx, 8);
				{
				setState(67);
				match(EQ);
				setState(68);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << NUMBER) | (1L << DATETIME))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InExprContext extends ParserRuleContext {
		public TerminalNode OPEN_PAREN() { return getToken(QueryExpressionParser.OPEN_PAREN, 0); }
		public List<InItemContext> inItem() {
			return getRuleContexts(InItemContext.class);
		}
		public InItemContext inItem(int i) {
			return getRuleContext(InItemContext.class,i);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(QueryExpressionParser.CLOSE_PAREN, 0); }
		public List<TerminalNode> COMMA() { return getTokens(QueryExpressionParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(QueryExpressionParser.COMMA, i);
		}
		public InExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).enterInExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).exitInExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryExpressionVisitor ) return ((QueryExpressionVisitor<? extends T>)visitor).visitInExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InExprContext inExpr() throws RecognitionException {
		InExprContext _localctx = new InExprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_inExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			match(OPEN_PAREN);
			setState(72);
			inItem();
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(73);
				match(COMMA);
				setState(74);
				inItem();
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(80);
			match(CLOSE_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InItemContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(QueryExpressionParser.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(QueryExpressionParser.NUMBER, 0); }
		public InItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).enterInItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryExpressionListener ) ((QueryExpressionListener)listener).exitInItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof QueryExpressionVisitor ) return ((QueryExpressionVisitor<? extends T>)visitor).visitInItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InItemContext inItem() throws RecognitionException {
		InItemContext _localctx = new InItemContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_inItem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			_la = _input.LA(1);
			if ( !(_la==STRING || _la==NUMBER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return conditions_sempred((ConditionsContext)_localctx, predIndex);
		case 2:
			return condition_sempred((ConditionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean conditions_sempred(ConditionsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean condition_sempred(ConditionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 2);
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001cU\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0005\u0001\u0014\b\u0001\n\u0001\f\u0001"+
		"\u0017\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002!\b\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002"+
		")\b\u0002\n\u0002\f\u0002,\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0005\u00031\b\u0003\n\u0003\f\u00034\t\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0003\u0004F\b\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0005\u0005L\b\u0005\n\u0005\f\u0005O\t\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0000\u0002"+
		"\u0002\u0004\u0007\u0000\u0002\u0004\u0006\b\n\f\u0000\u0003\u0002\u0000"+
		"\u0011\u0011\u0014\u0014\u0002\u0000\u0010\u0011\u0014\u0014\u0001\u0000"+
		"\u0010\u0011Z\u0000\u000e\u0001\u0000\u0000\u0000\u0002\u0010\u0001\u0000"+
		"\u0000\u0000\u0004 \u0001\u0000\u0000\u0000\u0006-\u0001\u0000\u0000\u0000"+
		"\bE\u0001\u0000\u0000\u0000\nG\u0001\u0000\u0000\u0000\fR\u0001\u0000"+
		"\u0000\u0000\u000e\u000f\u0003\u0002\u0001\u0000\u000f\u0001\u0001\u0000"+
		"\u0000\u0000\u0010\u0015\u0006\u0001\uffff\uffff\u0000\u0011\u0012\n\u0002"+
		"\u0000\u0000\u0012\u0014\u0003\u0004\u0002\u0000\u0013\u0011\u0001\u0000"+
		"\u0000\u0000\u0014\u0017\u0001\u0000\u0000\u0000\u0015\u0013\u0001\u0000"+
		"\u0000\u0000\u0015\u0016\u0001\u0000\u0000\u0000\u0016\u0003\u0001\u0000"+
		"\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0018\u0019\u0006\u0002"+
		"\uffff\uffff\u0000\u0019\u001a\u0003\u0006\u0003\u0000\u001a\u001b\u0003"+
		"\b\u0004\u0000\u001b!\u0001\u0000\u0000\u0000\u001c\u001d\u0005\u0001"+
		"\u0000\u0000\u001d\u001e\u0003\u0004\u0002\u0000\u001e\u001f\u0005\u0002"+
		"\u0000\u0000\u001f!\u0001\u0000\u0000\u0000 \u0018\u0001\u0000\u0000\u0000"+
		" \u001c\u0001\u0000\u0000\u0000!*\u0001\u0000\u0000\u0000\"#\n\u0002\u0000"+
		"\u0000#$\u0005\u000b\u0000\u0000$)\u0003\u0004\u0002\u0003%&\n\u0001\u0000"+
		"\u0000&\'\u0005\f\u0000\u0000\')\u0003\u0004\u0002\u0002(\"\u0001\u0000"+
		"\u0000\u0000(%\u0001\u0000\u0000\u0000),\u0001\u0000\u0000\u0000*(\u0001"+
		"\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000+\u0005\u0001\u0000\u0000"+
		"\u0000,*\u0001\u0000\u0000\u0000-2\u0005\u001a\u0000\u0000./\u0005\u0018"+
		"\u0000\u0000/1\u0005\u001a\u0000\u00000.\u0001\u0000\u0000\u000014\u0001"+
		"\u0000\u0000\u000020\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u0000"+
		"3\u0007\u0001\u0000\u0000\u000042\u0001\u0000\u0000\u000056\u0005\r\u0000"+
		"\u00006F\u0005\u0010\u0000\u000078\u0005\n\u0000\u00008F\u0003\n\u0005"+
		"\u00009:\u0005\u000e\u0000\u0000:F\u0005\u000f\u0000\u0000;<\u0005\u0006"+
		"\u0000\u0000<F\u0007\u0000\u0000\u0000=>\u0005\b\u0000\u0000>F\u0007\u0000"+
		"\u0000\u0000?@\u0005\u0007\u0000\u0000@F\u0007\u0000\u0000\u0000AB\u0005"+
		"\t\u0000\u0000BF\u0007\u0000\u0000\u0000CD\u0005\u0005\u0000\u0000DF\u0007"+
		"\u0001\u0000\u0000E5\u0001\u0000\u0000\u0000E7\u0001\u0000\u0000\u0000"+
		"E9\u0001\u0000\u0000\u0000E;\u0001\u0000\u0000\u0000E=\u0001\u0000\u0000"+
		"\u0000E?\u0001\u0000\u0000\u0000EA\u0001\u0000\u0000\u0000EC\u0001\u0000"+
		"\u0000\u0000F\t\u0001\u0000\u0000\u0000GH\u0005\u0001\u0000\u0000HM\u0003"+
		"\f\u0006\u0000IJ\u0005\u0017\u0000\u0000JL\u0003\f\u0006\u0000KI\u0001"+
		"\u0000\u0000\u0000LO\u0001\u0000\u0000\u0000MK\u0001\u0000\u0000\u0000"+
		"MN\u0001\u0000\u0000\u0000NP\u0001\u0000\u0000\u0000OM\u0001\u0000\u0000"+
		"\u0000PQ\u0005\u0002\u0000\u0000Q\u000b\u0001\u0000\u0000\u0000RS\u0007"+
		"\u0002\u0000\u0000S\r\u0001\u0000\u0000\u0000\u0007\u0015 (*2EM";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}