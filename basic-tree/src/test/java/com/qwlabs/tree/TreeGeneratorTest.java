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
            "{\"children\":[],\"node\":\"A\"}");
    }

    @Test
    void should_gen2() throws JsonProcessingException {
        genAndCheck("""
                # A
                ## B
                ## C
                """,
            "{\"children\":[{\"children\":[],\"node\":\"B\"},{\"children\":[],\"node\":\"C\"}],\"node\":\"A\"}");
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
            "{\"children\":[{\"children\":[{\"children\":[],\"node\":\"C\"},{\"children\":[],\"node\":\"D\"}],\"node\":\"B\"},{\"children\":[{\"children\":[],\"node\":\"F\"},{\"children\":[],\"node\":\"G\"},{\"children\":[],\"node\":\"C\"}],\"node\":\"E\"}],\"node\":\"A\"}");
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
            "{\"children\":[{\"children\":[{\"children\":[{\"children\":[],\"node\":\"D\"},{\"children\":[],\"node\":\"E\"},{\"children\":[],\"node\":\"H\"}],\"node\":\"C\"},{\"children\":[],\"node\":\"D\"}],\"node\":\"B\"},{\"children\":[{\"children\":[],\"node\":\"D\"},{\"children\":[],\"node\":\"E\"},{\"children\":[],\"node\":\"H\"},{\"children\":[{\"children\":[],\"node\":\"G\"},{\"children\":[],\"node\":\"K\"}],\"node\":\"I\"}],\"node\":\"C\"}],\"node\":\"A\"}");
    }

    void genAndCheck(String graph, String expected) throws JsonProcessingException {
        var node = TreeGenerator.node(graph);
        assertThat(objectMapper.writeValueAsString(node), is(expected));
    }
}
