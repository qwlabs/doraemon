package com.qwlabs.tree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TreeGeneratorTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void should_gen1() throws JsonProcessingException {
        genAndCheck("""
                # A
                """,
            "{\"node\":\"A\",\"children\":[]}");
    }

    @Test
    void should_gen2() throws JsonProcessingException {
        genAndCheck("""
                # A
                ## B
                ## C
                """,
            "{\"node\":\"A\",\"children\":[{\"node\":\"B\",\"children\":[]},{\"node\":\"C\",\"children\":[]}]}");
    }

    @Test
    void should_gen3() throws JsonProcessingException {
        genAndCheck("""
                # A
                ## B
                ### C
                ### D
                ## E
                ### F
                ### G
                ### C
                """,
            "{\"node\":\"A\",\"children\":[{\"node\":\"B\",\"children\":[{\"node\":\"C\",\"children\":[]},{\"node\":\"D\",\"children\":[]}]},{\"node\":\"E\",\"children\":[{\"node\":\"F\",\"children\":[]},{\"node\":\"G\",\"children\":[]},{\"node\":\"C\",\"children\":[]}]}]}");
    }

    @Test
    void should_gen4() throws JsonProcessingException {
        genAndCheck("""
                # A
                ## B
                ### C
                #### D
                #### E
                #### H
                ### D
                ## C
                ### D
                ### E
                ### H
                ### I
                #### G
                #### K
                """,
            "{\"node\":\"A\",\"children\":[{\"node\":\"B\",\"children\":[{\"node\":\"C\",\"children\":[{\"node\":\"D\",\"children\":[]},{\"node\":\"E\",\"children\":[]},{\"node\":\"H\",\"children\":[]}]},{\"node\":\"D\",\"children\":[]}]},{\"node\":\"C\",\"children\":[{\"node\":\"D\",\"children\":[]},{\"node\":\"E\",\"children\":[]},{\"node\":\"H\",\"children\":[]},{\"node\":\"I\",\"children\":[{\"node\":\"G\",\"children\":[]},{\"node\":\"K\",\"children\":[]}]}]}]}");
    }

    void genAndCheck(String graph, String expected) throws JsonProcessingException {
        var node = TreeGenerator.node(graph);
        assertThat(objectMapper.writeValueAsString(node), is(expected));
    }
}
