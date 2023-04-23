package com.qwlabs.tree;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Function;

public class TreeGenerator {
    private static final String DEFAULT_INDENT = "#";
    private static final String INDENT_DELIMITER = " ";
    private final String indent;

    public TreeGenerator(String indent) {
        this.indent = indent;
    }

    public TreeNodes<String> generate(String graph) {
        var lines = toLines(graph);
        if (lines.isEmpty()) {
            return TreeNodes.empty();
        }
        return generate(lines);
    }

    private TreeNodes<String> generate(List<Line> lines) {
        Map<Line, Line> parentMapping = Maps.newHashMap();
        Stack<Line> stack = new Stack<>();
        Line parent = null;
        stack.push(parent);
        for (Line line : lines) {
            while (!stack.isEmpty()) {
                var mayParent = stack.peek();
                if (mayParent == null) {
                    break;
                }
                if (mayParent.layer >= line.layer) {
                    stack.pop();
                    continue;
                }
                parent = stack.peek();
                break;
            }
            stack.push(line);
            parentMapping.put(line, parent);
        }
        return Tree.of(lines, Function.identity(), parentMapping::get)
            .map((location, node) -> node.getNode().value);
    }


    private List<Line> toLines(String graph) {
        List<String> rawLines = Splitter.on("\n")
            .trimResults()
            .omitEmptyStrings()
            .splitToList(graph);
        List<Line> lines = Lists.newArrayList();
        for (int row = 0; row <= rawLines.size() - 1; row++) {
            lines.add(Line.of(indent, row, rawLines.get(row)));
        }
        return lines;
    }

    private int deep(String line) {
        if (line == null) {
            return 0;
        }
        return line.substring(0, line.lastIndexOf(this.indent)).length();
    }


    public static class Line implements Comparable<Line> {
        private final int row;
        private final String raw;
        private final int layer;
        private final String value;

        public Line(String indent, int row, String raw) {
            this.row = row;
            this.raw = raw;
            int indentDelimiterIndex = raw.lastIndexOf(indent) + 1;
            this.layer = calculateLayer(raw, indentDelimiterIndex);
            this.value = calculateValue(raw, indentDelimiterIndex);
        }

        private String calculateValue(String raw, int indentDelimiterIndex) {
            return raw.substring(indentDelimiterIndex).trim();
        }

        private int calculateLayer(String raw, int indentDelimiterIndex) {
            return raw.substring(0, indentDelimiterIndex).trim().length();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Line line = (Line) o;
            return row == line.row && Objects.equals(raw, line.raw);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, raw);
        }

        @Override
        public int compareTo(Line o) {
            return row - o.row;
        }

        @Override
        public String toString() {
            return "%d--%s".formatted(row, raw);
        }

        public static Line of(String indent, int row, String raw) {
            return new Line(indent, row, raw);
        }
    }


    public static TreeNodes<String> nodes(String graph) {
        return nodes(DEFAULT_INDENT, graph);
    }

    public static TreeNodes<String> nodes(String indent, String graph) {
        return on(indent).generate(graph);
    }

    public static TreeNode<String> node(String graph) {
        return node(DEFAULT_INDENT, graph);
    }

    public static TreeNode<String> node(String indent, String graph) {
        return on(indent).generate(graph)
            .first().orElseThrow();
    }

    public static TreeGenerator on() {
        return new TreeGenerator(DEFAULT_INDENT);
    }

    public static TreeGenerator on(String indent) {
        return new TreeGenerator(indent);
    }
}
