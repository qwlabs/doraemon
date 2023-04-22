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
    void should_gen() throws JsonProcessingException {
        var node = TreeGenerator.node("""
            # A
            """);
        assertThat(objectMapper.writeValueAsString(node), is("{\"node\":\"A\",\"children\":[]}"));

        node = TreeGenerator.node("""
            # A
            ## B
            ## C
            """);
        assertThat(objectMapper.writeValueAsString(node), is("{\"node\":\"A\",\"children\":[{\"node\":\"B\",\"children\":[]},{\"node\":\"C\",\"children\":[]}]}"));


        node = TreeGenerator.node("""
            # A
            ## B
            ### C
            ### D
            ## E
            ### F
            ### G
            ### C
            """);
        assertThat(objectMapper.writeValueAsString(node), is("{\"node\":\"A\",\"children\":[{\"node\":\"B\",\"children\":[{\"node\":\"C\",\"children\":[]},{\"node\":\"D\",\"children\":[]}]},{\"node\":\"E\",\"children\":[{\"node\":\"G\",\"children\":[]},{\"node\":\"F\",\"children\":[]},{\"node\":\"C\",\"children\":[]}]}]}"));


        node = TreeGenerator.node("""
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
            """);
        assertThat(objectMapper.writeValueAsString(node), is("{\"node\":\"A\",\"children\":[{\"node\":\"C\",\"children\":[{\"node\":\"E\",\"children\":[{\"node\":\"I\",\"children\":[{\"node\":\"K\",\"children\":[]},{\"node\":\"G\",\"children\":[]}]},{\"node\":\"H\",\"children\":[]}]},{\"node\":\"D\",\"children\":[]}]},{\"node\":\"B\",\"children\":[{\"node\":\"D\",\"children\":[]},{\"node\":\"C\",\"children\":[{\"node\":\"D\",\"children\":[]},{\"node\":\"E\",\"children\":[]},{\"node\":\"H\",\"children\":[]}]}]}]}"));
    }
}
