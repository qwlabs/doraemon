package com.qwlabs.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JacksonTest {

    @Mock
    ObjectMapper objectMapper;
    private TypeReference<TestJacksonClass> typeReference;
    private JavaType javaType;

    @BeforeEach
    void setUp() {
        typeReference = new TypeReference<>() {
        };
        javaType = new ObjectMapper().getTypeFactory().constructType(typeReference);
        Jackson.setObjectMapper(null);
    }

    @Test
    void should_return_when_marshal_success() throws JsonProcessingException {
        TestJacksonClass data = new TestJacksonClass();
        data.setValue("v1");

        String json = Jackson.write(data).get();

        assertThat(json, is("{\"value\":\"v1\"}"));
    }

    @Test
    void should_return_empty_when_marshal_fail(@Mock JsonProcessingException exception) throws JsonProcessingException {
        Jackson.setObjectMapper(objectMapper);
        TestJacksonClass jacksonClass = new TestJacksonClass();
        when(objectMapper.writeValueAsString(jacksonClass)).thenThrow(exception);

        Optional<String> mayResult = Jackson.write(jacksonClass);
        Assertions.assertFalse(mayResult.isPresent());

        verify(objectMapper).writeValueAsString(jacksonClass);
    }

    @Test
    void should_return_when_unmarshal_success_with_TypeReference() {
        var mayResult = Jackson.read("{\"value\":\"v1\"}", typeReference);
        Assertions.assertTrue(mayResult.isPresent());
        assertThat(mayResult.get().getValue(), is("v1"));
    }

    @Test
    void should_return_empty_when_unmarshal_fail_with_TypeReference(@Mock JsonProcessingException exception) throws JsonProcessingException {
        var mayResult = Jackson.read("{\"value\": v1}", typeReference);
        Assertions.assertTrue(mayResult.isEmpty());
    }

    @Test
    void should_return_when_unmarshal_success_with_class() {
        var mayResult = Jackson.read("{\"value\":\"v1\"}", TestJacksonClass.class);
        Assertions.assertTrue(mayResult.isPresent());
        assertThat(mayResult.get().getValue(), is("v1"));
    }

    @Test
    void should_return_empty_when_unmarshal_fail_with_class() {
        var mayResult = Jackson.read("{\"value\": v1}", TestJacksonClass.class);
        Assertions.assertTrue(mayResult.isEmpty());
    }

    @Test
    void should_return_when_unmarshal_success_with_java_type() {
        var mayResult = Jackson.read("{\"value\":\"v1\"}", javaType);
        Assertions.assertTrue(mayResult.isPresent());
        assertThat(((TestJacksonClass) mayResult.get()).getValue(), is("v1"));
    }


    @Test
    void should_return_empty_when_unmarshal_fail_with_java_type() {
        var mayResult = Jackson.read("json", javaType);
        Assertions.assertTrue(mayResult.isEmpty());
    }


    @Test
    void should_return_when_unmarshal_success() throws JsonProcessingException {
        var node = Jackson.createObjectNode();
        node.set("name", new TextNode("value"));
        var mayResult = Jackson.read(Jackson.write(node).orElse(null));
        Assertions.assertTrue(mayResult.isPresent());
        assertThat(mayResult.get().get("name").asText(), is("value"));
    }

    @Test
    void should_return_empty_when_unmarshal_fail(@Mock JsonProcessingException exception) throws JsonProcessingException {
        var mayResult = Jackson.read("{\"value\": v1}");
        Assertions.assertTrue(mayResult.isEmpty());
    }

    @Getter
    @Setter
    private static class TestJacksonClass {
        private String value;
    }
}
