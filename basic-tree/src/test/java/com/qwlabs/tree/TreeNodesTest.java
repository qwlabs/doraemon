package com.qwlabs.tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TreeNodesTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void should_size() {
        TreeNodes<String> empty = TreeNodes.empty();
        TreeNodes<String> single = TreeNodes.of(TreeNode.of("1"));
        TreeNodes<String> multiple = TreeNodes.of(TreeNode.<String>of("1"), TreeNode.of("2"));

        assertTrue(empty.isEmpty());
        assertFalse(empty.isSingle());
        assertFalse(empty.isMultiple());

        assertFalse(single.isEmpty());
        assertTrue(single.isSingle());
        assertFalse(single.isMultiple());

        assertFalse(multiple.isEmpty());
        assertFalse(multiple.isSingle());
        assertTrue(multiple.isMultiple());
    }

    @Test
    void should_map_to_tree_node() {
        TreeNodes<String> nodes = TreeNodes.of(
            TreeNode.of("1", TreeNodes.of(TreeNode.of("3"), TreeNode.of("4"))),
            TreeNode.of("2", TreeNodes.of(TreeNode.of("5")))
        );
        TreeNodes<String> mappedNodes = nodes.map((location, node) -> "%s-%s".formatted(location.mapPath(TreeNode::getNode), node.getNode()));

        assertThat(mappedNodes.size(), is(2));
        assertThat(mappedNodes.get(0).getNode(), is("[]-1"));
        assertThat(mappedNodes.get(0).getChildren().size(), is(2));
        assertThat(mappedNodes.get(0).getChildren().get(0).getNode(), is("[1]-3"));
        assertThat(mappedNodes.get(0).getChildren().get(1).getNode(), is("[1]-4"));

        assertThat(mappedNodes.get(1).getNode(), is("[]-2"));
        assertThat(mappedNodes.get(1).getChildren().size(), is(1));
        assertThat(mappedNodes.get(1).getChildren().get(0).getNode(), is("[2]-5"));
    }

    @Test
    void should_map_to_customize() throws JsonProcessingException {
        TreeNodes<BomNode> nodes = TreeNodes.of(
            TreeNode.of(BomNode.of("a"), TreeNodes.of(TreeNode.of(BomNode.of("a1")), TreeNode.of(BomNode.of("a2")))),
            TreeNode.of(BomNode.of("b"), TreeNodes.of(TreeNode.of(BomNode.of("b1"))))
        );

        List<BomTreeNode> mappedNodes = nodes.map((location, node, children) -> BomTreeNode.of(node, children));

        assertThat(objectMapper.writeValueAsString(mappedNodes), is("[{\"value1\":\"a\",\"value\":\"a\",\"children\":[{\"value1\":\"a1\",\"value\":\"a1\",\"children\":[]},{\"value1\":\"a2\",\"value\":\"a2\",\"children\":[]}]},{\"value1\":\"b\",\"value\":\"b\",\"children\":[{\"value1\":\"b1\",\"value\":\"b1\",\"children\":[]}]}]"));
    }

    @Test
    void should_all() {
        TreeNodes<String> nodes = TreeNodes.of(
            TreeNode.of("1", TreeNodes.of(TreeNode.of("3"), TreeNode.of("4"))),
            TreeNode.of("2", TreeNodes.of(TreeNode.of("5")))
        );
        List<String> values = nodes.all((location, node) -> "%s-%s".formatted(location.mapPath(TreeNode::getNode), node.getNode()));

        assertThat(values, is(List.of("[]-1", "[1]-3", "[1]-4", "[]-2", "[2]-5")));
    }
}
