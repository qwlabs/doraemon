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

class TreeNodeTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Stream<BomNode> treeNodes = Stream.of(BomNode.of("a"), BomNode.of("ab"), BomNode.of("ac"));
    private final String rawNode = "{\"children\":[{\"children\":null,\"value1\":\"ab\",\"value\":\"ab\"},{\"children\":null,\"value1\":\"ac\",\"value\":\"ac\"}],\"value1\":\"a\",\"value\":\"a\"}";
    private final String decodedNode = "{\"children\":[{\"value1\":\"ab\",\"value\":\"ab\"},{\"value1\":\"ac\",\"value\":\"ac\"}],\"value1\":\"a\",\"value\":\"a\"}";
    private final TypeReference<TreeNode<BomNode>> type = new TypeReference<>() {
    };

    private final JavaType javaType = objectMapper.constructType(type);

    @Test
    void should_of_stream() throws JsonProcessingException {
        var rootNode = Tree.of(treeNodes,
            BomNode::getValue,
            BomNode::getParent).first().get();

        assertThat(objectMapper.writeValueAsString(rootNode), is(decodedNode));

        TreeNode<BomNode> decodeNode = objectMapper.readValue(rawNode, javaType);

        assertThat(decodeNode.getNode().getValue(), is("a"));
        assertThat(decodeNode.getChildren().size(), is(2));
        assertThat(decodeNode.getChildren().get(0).getNode().getValue(), is("ab"));
        assertThat(decodeNode.getChildren().get(1).getNode().getValue(), is("ac"));
    }

    @Test
    void should_of_iterable() throws JsonProcessingException {
        var rootNode = Tree.of(treeNodes.collect(Collectors.toList()),
            BomNode::getValue,
            BomNode::getParent).first().get();

        assertThat(objectMapper.writeValueAsString(rootNode), is(decodedNode));


        TreeNode<BomNode> decodeNode = objectMapper.readValue(rawNode, javaType);

        assertThat(decodeNode.getNode().getValue(), is("a"));
        assertThat(decodeNode.getChildren().size(), is(2));
        assertThat(decodeNode.getChildren().get(0).getNode().getValue(), is("ab"));
        assertThat(decodeNode.getChildren().get(1).getNode().getValue(), is("ac"));
    }

    @Test
    void should_of_iterator() throws JsonProcessingException {
        var rootNode = Tree.of(treeNodes.iterator(),
            BomNode::getValue,
            BomNode::getParent).first().get();

        assertThat(objectMapper.writeValueAsString(rootNode), is(decodedNode));

        TreeNode<BomNode> decodeNode = objectMapper.readValue(rawNode, javaType);

        assertThat(decodeNode.getNode().getValue(), is("a"));
        assertThat(decodeNode.getChildren().size(), is(2));
        assertThat(decodeNode.getChildren().get(0).getNode().getValue(), is("ab"));
        assertThat(decodeNode.getChildren().get(1).getNode().getValue(), is("ac"));
    }
}
