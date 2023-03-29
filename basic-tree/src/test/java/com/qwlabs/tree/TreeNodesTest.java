package com.qwlabs.tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeNodesTest {
    static final TypeReference<TreeNodes<Node>> TYPE = new TypeReference<>() {
    };
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void should_read_write() throws JsonProcessingException {
        String fromJson = """
                [
                    {
                        "id": "1"
                    },
                    {
                        "id": "2"
                    },
                    {
                        "id": "3"
                    }
                ]
                """;

        var nodes = objectMapper.readValue(fromJson, TYPE);
        assertThat(nodes.get(0).getSource().id, is("1"));
        assertThat(nodes.get(1).getSource().id, is("2"));
        assertThat(nodes.get(2).getSource().id, is("3"));

        assertThat(objectMapper.writeValueAsString(nodes), is("[{\"id\":\"1\",\"children\":[]},{\"id\":\"2\",\"children\":[]},{\"id\":\"3\",\"children\":[]}]"));
    }

    @Test
    void should_eq() throws JsonProcessingException {
        String json = """
                [
                    {
                        "id": "1"
                    },
                    {
                        "id": "2"
                    },
                    {
                        "id": "3"
                    }
                ]
                """;
        var nodes1 = objectMapper.readValue(json, TYPE);
        var nodes2 = objectMapper.readValue(json, TYPE);

        assertEquals(nodes1, nodes2);

    }

    @Getter
    @Setter
    static class Node {
        private String id;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return Objects.equals(id, node.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}