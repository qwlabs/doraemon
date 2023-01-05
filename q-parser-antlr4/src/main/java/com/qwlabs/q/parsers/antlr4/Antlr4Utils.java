package com.qwlabs.q.parsers.antlr4;

import com.qwlabs.lang.S2;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigDecimal;
import java.util.Optional;

public final class Antlr4Utils {
    private Antlr4Utils() {
    }

    public static String nodeToString(TerminalNode node) {
        return Optional.ofNullable(node)
                .map(ParseTree::getText)
                .map(value -> S2.removeStartsWith(value, "\"", "'"))
                .map(value -> S2.removeEndsWith(value, "\"", "'"))
                .orElse(null);
    }

    public static BigDecimal nodeToNumber(TerminalNode node) {
        return Optional.ofNullable(node)
                .map(ParseTree::getText)
                .map(BigDecimal::new)
                .orElse(null);
    }
}
