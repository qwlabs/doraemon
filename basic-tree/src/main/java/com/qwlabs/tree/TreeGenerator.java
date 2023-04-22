package com.qwlabs.tree;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class TreeGenerator {
    private static final String LINE_SEPARATOR = "==";
    private static final String DEFAULT_INDENT = "#";
    private final String indent;

    public TreeGenerator(String indent) {
        this.indent = indent;
    }

    public TreeNodes<String> generate(String graph) {
        var lines = readLines(graph);
        Map<String, String> codeParent = Maps.newHashMap();
        Stack<String> stack = new Stack<>();
        String parent;
        for (String line : lines) {
            int lineDeep = deep(line);
            while (true) {
                var mayParent = stack.empty() ? null : stack.peek();
                var parentDeep = deep(mayParent);
                if (parentDeep >= lineDeep) {
                    stack.pop();
                    continue;
                }
                parent = mayParent;
                stack.push(line);
                break;
            }
            codeParent.put(line, parent);
        }
        return Tree.of(lines, Function.identity(), codeParent::get)
            .map((location, node) -> toNode(node.getNode()));
    }

    private String toNode(String code) {
        code = code.substring(code.indexOf(LINE_SEPARATOR) + LINE_SEPARATOR.length());
        return code.replace(indent, "").trim();
    }

    private List<String> readLines(String graph) {
        List<String> values = Splitter.on("\n")
            .omitEmptyStrings()
            .splitToList(graph);
        List<String> liens = Lists.newArrayList();
        for (int index = 0; index <= values.size() - 1; index++) {
            var line = values.get(index);
            liens.add("%d%s%s".formatted(index, LINE_SEPARATOR, line));
        }
        return liens;
    }

    private int deep(String line) {
        if (line == null) {
            return 0;
        }
        return line.substring(0, line.lastIndexOf(this.indent)).length();
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
