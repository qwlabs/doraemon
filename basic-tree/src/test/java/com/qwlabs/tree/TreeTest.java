package com.qwlabs.tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

class TreeTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Stream<TestTreeNode> treeNodes = Stream.of(TestTreeNode.of("a"), TestTreeNode.of("ab"), TestTreeNode.of("c"));
    private final String rawNodes = "[{\"children\":[{\"children\":null,\"value\":\"ab\"}],\"value\":\"a\"},{\"children\":null,\"value\":\"c\"}]";
    private final TypeReference<TreeNodes<TestTreeNode>> type = new TypeReference<>() {
    };

    private final JavaType javaType = objectMapper.constructType(type);

    @Test
    void should_of_stream() throws JsonProcessingException {
        var nodes = Tree.of(treeNodes,
                TestTreeNode::getValue,
                TestTreeNode::getParent);
        assertThat(objectMapper.writeValueAsString(nodes), is(rawNodes));
        TreeNodes<TestTreeNode> decodeNodes = objectMapper.readValue(rawNodes, javaType);
        assertThat(decodeNodes.size(), is(2));
        assertThat(decodeNodes.get(0).getValue(), is("a"));
        assertThat(decodeNodes.get(0).getChildren().size(), is(1));
        assertThat(decodeNodes.get(0).getChildren().get(0).getValue(), is("ab"));

        assertThat(decodeNodes.get(1).getValue(), is("c"));
        assertNull(decodeNodes.get(1).getChildren());

    }

    @Test
    void should_of_iterable() throws JsonProcessingException {
        var nodes = Tree.of(treeNodes.collect(Collectors.toList()),
                TestTreeNode::getValue,
                TestTreeNode::getParent);
        assertThat(objectMapper.writeValueAsString(nodes), is(rawNodes));

        TreeNodes<TestTreeNode> decodeNodes = objectMapper.readValue(rawNodes, type);
        assertThat(decodeNodes.size(), is(2));
        assertThat(decodeNodes.get(0).getValue(), is("a"));
        assertThat(decodeNodes.get(0).getChildren().size(), is(1));
        assertThat(decodeNodes.get(0).getChildren().get(0).getValue(), is("ab"));

        assertThat(decodeNodes.get(1).getValue(), is("c"));
        assertNull(decodeNodes.get(1).getChildren());
    }

    @Test
    void should_of_iterator() throws JsonProcessingException {
        var nodes = Tree.of(treeNodes.iterator(),
                TestTreeNode::getValue,
                TestTreeNode::getParent);
        assertThat(objectMapper.writeValueAsString(nodes), is(rawNodes));

        TreeNodes<TestTreeNode> decodeNodes = objectMapper.readValue(rawNodes, javaType);
        assertThat(decodeNodes.size(), is(2));
        assertThat(decodeNodes.get(0).getValue(), is("a"));
        assertThat(decodeNodes.get(0).getChildren().size(), is(1));
        assertThat(decodeNodes.get(0).getChildren().get(0).getValue(), is("ab"));

        assertThat(decodeNodes.get(1).getValue(), is("c"));
        assertNull(decodeNodes.get(1).getChildren());
    }
}