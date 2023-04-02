package com.qwlabs.tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.qwlabs.jackson.Jackson;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TreeNodeTest {

    static final TypeReference<TreeNode<FromNode>> TYPE = new TypeReference<>() {
    };

    @Test
    void should_read_json() {
        var fromJson = """
                {
                    "source": {
                        "partCode": "p1",
                        "partVersion": "v1",
                        "numberOfUnit": 1
                    },
                    "children": [
                        {
                            "source": {
                                "partCode": "p2",
                                "partVersion": "v1",
                                "numberOfUnit": 3
                            },
                            "children": []
                        },
                        {
                            "source": {
                                "partCode": "p3",
                                "partVersion": "v1",
                                "numberOfUnit": 2
                            },
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


        var toNode = fromNode.map((location, node) -> {
            ToNode t = new ToNode();
            t.setName("%s-%s-%d".formatted(node.partCode, node.partVersion, node.numberOfUnit));
            return t;
        });

        var toJson = Jackson.write(toNode).get();

        assertThat(toJson, is("{\"source\":{\"name\":\"p1-v1-1\"},\"children\":[{\"source\":{\"name\":\"p2-v1-3\"},\"children\":[]},{\"source\":{\"name\":\"p3-v1-2\"},\"children\":[]}]}"));
    }


    @Test
    void should_eq() throws JsonProcessingException {
        var json = """ 
                {
                    "source": {
                        "partCode": "p1",
                        "partVersion": "v1",
                        "numberOfUnit": 1
                    },
                    "children": [
                        {
                            "source": {
                                "partCode": "p2",
                                "partVersion": "v1",
                                "numberOfUnit": 3
                            },
                            "children": []
                        },
                        {
                            "source": {
                                "partCode": "p3",
                                "partVersion": "v1",
                                "numberOfUnit": 2
                            },
                            "children": []
                        }
                    ]
                }              
                 """;
        var node1 = Jackson.read(json, TYPE).get();
        var node2 = Jackson.read(json, TYPE).get();

        assertEquals(node1, node2);

    }

    @Test
    void should_foreach() throws JsonProcessingException {
        var json = """ 
               {
                    "source": {
                        "partCode": "p1",
                        "partVersion": "v1",
                        "numberOfUnit": 1
                    },
                    "children": [
                        {
                            "source": {
                                "partCode": "p2",
                                "partVersion": "v1",
                                "numberOfUnit": 3
                            },
                            "children": []
                        },
                        {
                            "source": {
                                "partCode": "p3",
                                "partVersion": "v1",
                                "numberOfUnit": 2
                            },
                            "children": []
                        }
                    ]
                }
                """;
        var node = Jackson.read(json, TYPE).get();
        List<String> partCodes = Lists.newArrayList();
        node.forEach((location, n) -> partCodes.add(n.getSource().getPartCode()));

        assertThat(partCodes.toString(), is("[p1, p2, p3]"));
    }

    @Getter
    @Setter
    private static class FromNode {
        private String partCode;
        private String partVersion;
        private Integer numberOfUnit;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FromNode fromNode = (FromNode) o;
            return Objects.equals(partCode, fromNode.partCode)
                    && Objects.equals(partVersion, fromNode.partVersion)
                    && Objects.equals(numberOfUnit, fromNode.numberOfUnit);
        }

        @Override
        public int hashCode() {
            return Objects.hash(partCode, partVersion, numberOfUnit);
        }
    }

    @Getter
    @Setter
    private static class ToNode {
        private String name;
    }
}