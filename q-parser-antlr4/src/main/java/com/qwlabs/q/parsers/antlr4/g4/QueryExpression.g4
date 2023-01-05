grammar QueryExpression;

query
    : conditions
    ;

conditions
    : conditions condition
    |
    ;


condition
    : conditionLeft conditionRight
    | OPEN_PAREN condition CLOSE_PAREN
    | condition AND condition
    | condition OR condition
    ;


conditionLeft: IDENTIFIER (DOT IDENTIFIER)*;
conditionRight
    : LIKE STRING
    | IN inExpr
    | IS NULL
    | GT (NUMBER | DATETIME)
    | GTE (NUMBER | DATETIME)
    | LT (NUMBER  | DATETIME)
    | LTE (NUMBER | DATETIME)
    | EQ (NUMBER | STRING | DATETIME)
    ;

OPEN_PAREN: '(';
CLOSE_PAREN: ')';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';

EQ: '=';
GT: '>';
LT: '<';
GTE: '>=';
LTE: '<=';
IN: ESCAPE I N ESCAPE;
AND: ESCAPE A N D ESCAPE;
OR: ESCAPE O R ESCAPE;
LIKE: ESCAPE L I K E ESCAPE;
IS: ESCAPE I S ESCAPE;
NULL: N U L L;

inExpr: '(' inItem (',' inItem)*  ')';
STRING: DOUBLE_QUOTED_STRING | SINGLE_QUOTED_STRING;
NUMBER: '-'? INT ('.' [0-9] +)? EXP?;
DATE
    : SINGLE_QUOTE ISO8601_DATE SINGLE_QUOTE
    | DOUBLE_QUOTE ISO8601_DATE DOUBLE_QUOTE
    ;

TIME
    : SINGLE_QUOTE ISO8601_TIME SINGLE_QUOTE
    | DOUBLE_QUOTE ISO8601_TIME DOUBLE_QUOTE
    ;

DATETIME
    : SINGLE_QUOTE ISO8601_DATE_TIME SINGLE_QUOTE
    | DOUBLE_QUOTE ISO8601_DATE_TIME DOUBLE_QUOTE
    ;

OPEN_RANGE: (OPEN_BRACKET | OPEN_PAREN);
CLOSE_RANGE: (CLOSE_BRACKET | CLOSE_PAREN);

inItem
    : STRING
    | NUMBER
    ;
COMMA: ',';
DOT: '.';
WS: [ \t]+ -> skip;


IDENTIFIER: LETTER LETTER_OR_DIGIT*;

DOUBLE_QUOTED_STRING: DOUBLE_QUOTE (ESC | SAFECODEPOINT)* DOUBLE_QUOTE;
SINGLE_QUOTED_STRING: SINGLE_QUOTE (ESC | SAFECODEPOINT)* SINGLE_QUOTE;

// ---------- ISO8601 Date/Time values ----------

fragment ISO8601_DATE
    : YEAR MONTH DAY
    | YEAR '-' MONTH '-' DAY
    ;
fragment ISO8601_TIME
    : HOUR MINUTE SECOND ('.' MICROSECOND)? TIMEZONE?
    | HOUR ':' MINUTE ':' SECOND ('.' MICROSECOND)? TIMEZONE?
    ;
fragment ISO8601_DATE_TIME
    : YEAR MONTH DAY ('T' HOUR MINUTE SECOND ('.' MICROSECOND)? TIMEZONE?)?
    | YEAR '-' MONTH '-' DAY ('T' HOUR ':' MINUTE ':' SECOND ('.' MICROSECOND)? TIMEZONE?)?
    ;
fragment MICROSECOND: [0-9][0-9][0-9] ;

fragment TIMEZONE: 'Z' | [+-] HOUR ( ':'? MINUTE )? ;  // hour offset, e.g. `+09:30`, or else literal `Z` indicating +0000.
fragment YEAR: [0-9][0-9][0-9][0-9] ; // Year in ISO8601:2004 is 4 digits with 0-filling as needed
fragment MONTH: ( [0][1-9] | [1][0-2] ) ;  // month in year
fragment DAY: ( [0][1-9] | [12][0-9] | [3][0-1] ) ; // day in month
fragment HOUR: ( [01][0-9] | [2][0-3] ) ; // hour in 24 hour clock
fragment MINUTE: [0-5][0-9] ; // minutes
fragment SECOND: [0-5][0-9] ; // seconds


fragment LETTER_OR_DIGIT: LETTER| [0-9];
fragment LETTER: [a-zA-Z$_];
fragment SINGLE_QUOTE: '\'';
fragment DOUBLE_QUOTE: '"';

fragment INT: '0' | [1-9] [0-9]*;
fragment EXP: [Ee] [+\-]? INT;
fragment ESC: '\\' (['"\\/bfnrt] | UNICODE);
fragment SAFECODEPOINT: ~ ['"\\\u0000-\u001F];
fragment UNICODE: 'u' HEX HEX HEX HEX;
fragment HEX: [0-9a-fA-F];
fragment ESCAPE: ' ';
fragment DEC_DIGIT: [0-9];

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];
