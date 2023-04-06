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

    private final Stream<BomNode> treeNodes = Stream.of(BomNode.of("a"), BomNode.of("ab"), BomNode.of("c"));
    private final String rawNodes = "[{\"value1\":\"a\",\"value\":\"a\",\"children\":[{\"value1\":\"ab\",\"value\":\"ab\",\"children\":null}]},{\"value1\":\"c\",\"value\":\"c\",\"children\":null}]";
    private final TypeReference<TreeNodes<BomNode>> type = new TypeReference<>() {
    };

    private final JavaType javaType = objectMapper.constructType(type);

    @Test
    void should_of_stream() throws JsonProcessingException {
        var nodes = Tree.of(treeNodes,
                BomNode::getValue,
                BomNode::getParent);
        assertThat(objectMapper.writeValueAsString(nodes), is(rawNodes));
        TreeNodes<BomNode> decodeNodes = objectMapper.readValue(rawNodes, javaType);
        assertThat(decodeNodes.size(), is(2));
        assertThat(decodeNodes.get(0).getNode().getValue(), is("a"));
        assertThat(decodeNodes.get(0).getChildren().size(), is(1));
        assertThat(decodeNodes.get(0).getChildren().get(0).getNode().getValue(), is("ab"));

        assertThat(decodeNodes.get(1).getNode().getValue(), is("c"));
        assertNull(decodeNodes.get(1).getChildren());

    }

    @Test
    void should_of_iterable() throws JsonProcessingException {
        var nodes = Tree.of(treeNodes.collect(Collectors.toList()),
                BomNode::getValue,
                BomNode::getParent);
        assertThat(objectMapper.writeValueAsString(nodes), is(rawNodes));

        TreeNodes<BomNode> decodeNodes = objectMapper.readValue(rawNodes, type);
        assertThat(decodeNodes.size(), is(2));
        assertThat(decodeNodes.get(0).getNode().getValue(), is("a"));
        assertThat(decodeNodes.get(0).getChildren().size(), is(1));
        assertThat(decodeNodes.get(0).getChildren().get(0).getNode().getValue(), is("ab"));

        assertThat(decodeNodes.get(1).getNode().getValue(), is("c"));
        assertNull(decodeNodes.get(1).getChildren());
    }

    @Test
    void should_of_iterator() throws JsonProcessingException {
        var nodes = Tree.of(treeNodes.iterator(),
                BomNode::getValue,
                BomNode::getParent);
        assertThat(objectMapper.writeValueAsString(nodes), is(rawNodes));

        TreeNodes<BomNode> decodeNodes = objectMapper.readValue(rawNodes, javaType);
        assertThat(decodeNodes.size(), is(2));
        assertThat(decodeNodes.get(0).getNode().getValue(), is("a"));
        assertThat(decodeNodes.get(0).getChildren().size(), is(1));
        assertThat(decodeNodes.get(0).getChildren().get(0).getNode().getValue(), is("ab"));

        assertThat(decodeNodes.get(1).getNode().getValue(), is("c"));
        assertNull(decodeNodes.get(1).getChildren());
    }
}
