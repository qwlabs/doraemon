package com.qwlabs.tree;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qwlabs.jackson.Jackson;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class TreeNodeTest {

    static final TypeReference<TreeNode<BomNode>> TYPE = new TypeReference<>() {
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
        TreeNode<BomNode> node = Jackson.read(fromJson, TYPE).get();
        assertThat(node.getSource().partCode, is("p1"));
        assertThat(node.getSource().partVersion, is("v1"));
        assertThat(node.getSource().numberOfUnit, is(1));
        assertThat(node.getChildren().size(), is(2));


        assertThat(node.getChildren().get(0).getSource().partCode, is("p2"));
        assertThat(node.getChildren().get(0).getSource().partVersion, is("v1"));
        assertThat(node.getChildren().get(0).getSource().numberOfUnit, is(3));
        assertThat(node.getChildren().get(0).getChildren().size(), is(0));

        assertThat(node.getChildren().get(1).getSource().partCode, is("p3"));
        assertThat(node.getChildren().get(1).getSource().partVersion, is("v1"));
        assertThat(node.getChildren().get(1).getSource().numberOfUnit, is(2));
        assertThat(node.getChildren().get(0).getChildren().size(), is(0));


        var toJson = Jackson.write(node).get();

        assertThat(toJson, is("{\"partCode\":\"p1\",\"partVersion\":\"v1\",\"numberOfUnit\":1,\"children\":[{\"partCode\":\"p2\",\"partVersion\":\"v1\",\"numberOfUnit\":3,\"children\":[]},{\"partCode\":\"p3\",\"partVersion\":\"v1\",\"numberOfUnit\":2,\"children\":[]}]}"));
    }


    @Getter
    @Setter
    private static class BomNode {
        private String partCode;
        private String partVersion;
        private Integer numberOfUnit;
    }
}