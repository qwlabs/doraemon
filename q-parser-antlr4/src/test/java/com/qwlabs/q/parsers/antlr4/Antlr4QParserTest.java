package com.qwlabs.q.parsers.antlr4;

import com.qwlabs.q.formatters.RawQFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Antlr4QParserTest {
    private Antlr4QParser parser;

    @BeforeEach
    void setUp() {
        parser = new Antlr4QParser();
    }

    @Test
    void should_parse() {
        shouldParseRight("field1=1", "field1=1");
        shouldParseRight("field1.field2=1", "field1.field2=1");
        shouldParseRight("field1=\"1\"", "field1='1'");
        shouldParseRight("field_1='1'", "field_1='1'");
        shouldParseRight("age>2", "age=(2..+∞)");
        shouldParseRight("age>=2", "age=[2..+∞)");
        shouldParseRight("age < 2", "age=(-∞..2)");
        shouldParseRight("age<=2 ", "age=(-∞..2]");
        shouldParseRight("age like '2' ", "age like '2'");
        shouldParseRight("a='1' and  b=2", "and:[a='1', b=2]");
        shouldParseRight("a='1' and  b=2 or c=5", "or:[and:[a='1', b=2], c=5]");
        shouldParseRight("(a=\"1\" and b=1 and cd=5 AND d=4)", "and:[and:[and:[a='1', b=1], cd=5], d=4]");
        shouldParseRight("a=\"1\" and b=1 and cd=5 or d=4", "or:[and:[and:[a='1', b=1], cd=5], d=4]");
        shouldParseRight("(a=\"1\" and b=1) and cd=5 AND d=4", "and:[and:[and:[a='1', b=1], cd=5], d=4]");
        shouldParseRight("a=\"1\" and ( b=1 and cd=5) AND d=4", "and:[and:[a='1', and:[b=1, cd=5]], d=4]");
        shouldParseRight("a=\"1\" and b=1 and (cd=5 AND d=4)", "and:[and:[a='1', b=1], and:[cd=5, d=4]]");
        shouldParseRight("a=\"1\" and (b=1 and (cd=5 AND d=4))", "and:[a='1', and:[b=1, and:[cd=5, d=4]]]");
        shouldParseRight("a=\"1\" or b=1 or cd=5 or d=4", "or:[or:[or:[a='1', b=1], cd=5], d=4]");
        shouldParseRight("(a=\"1\" or b=1) or cd=5 or d=4", "or:[or:[or:[a='1', b=1], cd=5], d=4]");
        shouldParseRight("a=\"1\" or ( b=1 or cd=5) or d=4", "or:[or:[a='1', or:[b=1, cd=5]], d=4]");
        shouldParseRight("a=\"1\" or b=1 or (cd=5 or d=4)", "or:[or:[a='1', b=1], or:[cd=5, d=4]]");
        shouldParseRight("a=\"1\" and b=1 and cd=5", "and:[and:[a='1', b=1], cd=5]");
        shouldParseRight("a=\"1\" or b=1 and cd=5", "or:[a='1', and:[b=1, cd=5]]");
        shouldParseRight("a=\"1\" and b=1 or cd=5", "or:[and:[a='1', b=1], cd=5]");
        shouldParseRight("a=\"1\" OR b=1 or cd=5", "or:[or:[a='1', b=1], cd=5]");
        shouldParseRight("(a=\"1\" and b=1) or ( cd=5 and c=8 and e like '3') or d=7", "or:[or:[and:[a='1', b=1], and:[and:[cd=5, c=8], e like '3']], d=7]");
        shouldParseRight("(a=\"1\" and b=1) or ( cd=5 and c=8 or e like '3') or d=7", "or:[or:[and:[a='1', b=1], or:[and:[cd=5, c=8], e like '3']], d=7]");
        shouldParseRight("(a=\"1\" and b=1) or (( cd=5 and c=8) or d=7)", "or:[and:[a='1', b=1], or:[and:[cd=5, c=8], d=7]]");
        shouldParseRight("(a=\"1\" and b=1) or (( cd=5 and c=8) or d=7 or x=7)", "or:[and:[a='1', b=1], or:[or:[and:[cd=5, c=8], d=7], x=7]]");
        shouldParseRight("(a=\"1\" or b=1) and ( cd=5 and c=8) and d=7 or x=7", "or:[and:[and:[or:[a='1', b=1], and:[cd=5, c=8]], d=7], x=7]");
        shouldParseRight("(a=\"1\" or b=1) and (d=7 or x=7 and ( cd=5 and c=8))", "and:[or:[a='1', b=1], or:[d=7, and:[x=7, and:[cd=5, c=8]]]]");
        shouldParseRight("a=\"1\" or b=1 and (cd=5 and c=8) and d=7 or x=7", "or:[or:[a='1', and:[and:[b=1, and:[cd=5, c=8]], d=7]], x=7]");
        shouldParseRight("team.agree = 'Y' and company.agree = 'Y'", "and:[team.agree='Y', company.agree='Y']");
        shouldParseRight("team.agree = \"Y\" or company.agree = 'Y'", "or:[team.agree='Y', company.agree='Y']");

        // ## in query
        shouldParseRight("field1 in (\"efe\")", "field1 in (\"efe\")");
        shouldParseRight("field1 in (12,23)", "field1 in (12,23)");
        shouldParseRight("field1 in (\"abc\",\"efe\")", "field1 in (\"abc\",\"efe\")");
    }

    private void shouldParseRight(String raw, String expected) {
        var condition = parser.parse(raw);
        assertThat(RawQFormatter.INSTANCE.format(condition), is(expected));
    }

}