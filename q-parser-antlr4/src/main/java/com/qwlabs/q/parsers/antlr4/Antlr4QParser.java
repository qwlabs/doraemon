package com.qwlabs.q.parsers.antlr4;

import com.google.common.base.Strings;
import com.qwlabs.cdi.Dispatchable;
import com.qwlabs.q.conditions.EmptyQCondition;
import com.qwlabs.q.conditions.QCondition;
import com.qwlabs.q.parsers.QParser;
import com.qwlabs.q.parsers.antlr4.g4.QueryExpressionLexer;
import com.qwlabs.q.parsers.antlr4.g4.QueryExpressionParser;
import io.quarkus.arc.Unremovable;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;

@Unremovable
@ApplicationScoped
public class Antlr4QParser implements Dispatchable<String>, QParser {
    private static final String PROVIDER = "antlr4";

    @Override
    public boolean dispatchable(String context) {
        return PROVIDER.equalsIgnoreCase(context);
    }

    @Override
    @NotNull
    public QCondition parse(@NotNull String query) {
        if (Strings.isNullOrEmpty(query)) {
            return EmptyQCondition.INSTANCE;
        }
        QueryExpressionLexer lexer = new QueryExpressionLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        QueryExpressionParser parser = new QueryExpressionParser(tokens);
        QueryExpressionParser.QueryContext queryContext = parser.query();
        QConditionsVisitor visitor = new QConditionsVisitor();
        return visitor.visit(queryContext);
    }
}
