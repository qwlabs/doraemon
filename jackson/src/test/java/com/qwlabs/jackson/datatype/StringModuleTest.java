package com.qwlabs.jackson.datatype;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class StringModuleTest {
    private String leftEmpty = """
        {
            "name": "  d",
            "value": 3
        }
        """;

    private String rightEmpty = """
        {
            "name": "d ",
            "value": 3
        }
        """;

    private String allEmpty = """
        {
            "name": "  d ",
            "value": 3
        }
        """;

    @Test
    void should_trim() throws JsonProcessingException {
        var objectMapper = withModule();

        assertThat(objectMapper.readValue(leftEmpty, TestClass.class).name, is("d"));
        assertThat(objectMapper.readValue(rightEmpty, TestClass.class).name, is("d"));
        assertThat(objectMapper.readValue(allEmpty, TestClass.class).name, is("d"));
    }

    @Test
    void should_not_trim() throws JsonProcessingException {
        var objectMapper = withoutModule();

        assertThat(objectMapper.readValue(leftEmpty, TestClass.class).name, is("  d"));
        assertThat(objectMapper.readValue(rightEmpty, TestClass.class).name, is("d "));
        assertThat(objectMapper.readValue(allEmpty, TestClass.class).name, is("  d "));
    }

    private ObjectMapper withoutModule() {
        return new ObjectMapper();
    }

    private ObjectMapper withModule() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new StringProcessModule());
        return objectMapper;
    }

    @Getter
    @Setter
    public static class TestClass {
        @Trimmed
        private String name;
        private Integer value;
    }
}
