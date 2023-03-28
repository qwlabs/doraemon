package com.qwlabs.tree;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qwlabs.jackson.Jackson;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class TreeNodeTest {

    static final TypeReference<TreeNode<FromNode>> TYPE = new TypeReference<>() {
    };

    @Test
    void should_read_json() {
        var fromJson = """ 
                {
                    "partCode": "p1",
                    "partVersion": "v1",
                    "numberOfUnit": 1,
                    "children": [
                        {
                            "partCode": "p2",
                            "partVersion": "v1",
                            "numberOfUnit": 3
                        },
                        {
                            "partCode": "p3",
                            "partVersion": "v1",
                            "numberOfUnit": 2,
                            "children": []
                        }
                    ]
                }
                """;
        TreeNode<FromNode> fromNode = Jackson.read(fromJson, TYPE).get();
        assertThat(fromNode.getSource().partCode, is("p1"));
        assertThat(fromNode.getSource().partVersion, is("v1"));
        assertThat(fromNode.getSource().numberOfUnit, is(1));
        assertThat(fromNode.getChildren().size(), is(2));


        assertThat(fromNode.getChildren().get(0).getSource().partCode, is("p2"));
        assertThat(fromNode.getChildren().get(0).getSource().partVersion, is("v1"));
        assertThat(fromNode.getChildren().get(0).getSource().numberOfUnit, is(3));
        assertThat(fromNode.getChildren().get(0).getChildren().size(), is(0));

        assertThat(fromNode.getChildren().get(1).getSource().partCode, is("p3"));
        assertThat(fromNode.getChildren().get(1).getSource().partVersion, is("v1"));
        assertThat(fromNode.getChildren().get(1).getSource().numberOfUnit, is(2));
        assertThat(fromNode.getChildren().get(0).getChildren().size(), is(0));


        var toNode = fromNode.map(node -> {
            ToNode t = new ToNode();
            t.setName("%s-%s-%d".formatted(node.partCode, node.partVersion, node.numberOfUnit));
            return t;
        });

        var toJson = Jackson.write(toNode).get();

        assertThat(toJson, is("{\"name\":\"p1-v1-1\",\"children\":[{\"name\":\"p2-v1-3\",\"children\":[]},{\"name\":\"p3-v1-2\",\"children\":[]}]}"));
    }

    @Getter
    @Setter
    private static class FromNode {
        private String partCode;
        private String partVersion;
        private Integer numberOfUnit;
    }

    @Getter
    @Setter
    private static class ToNode {
        private String name;
    }
}