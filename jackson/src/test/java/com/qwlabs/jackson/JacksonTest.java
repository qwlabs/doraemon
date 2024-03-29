package com.qwlabs.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.qwlabs.jackson.Jackson.asBoolean;
import static com.qwlabs.jackson.Jackson.asText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        assertFalse(mayResult.isPresent());

        verify(objectMapper).writeValueAsString(jacksonClass);
    }

    @Test
    void should_return_when_unmarshal_success_with_TypeReference() {
        var mayResult = Jackson.read("{\"value\":\"v1\"}", typeReference);
        assertTrue(mayResult.isPresent());
        assertThat(mayResult.get().getValue(), is("v1"));
    }

    @Test
    void should_return_empty_when_unmarshal_fail_with_TypeReference(@Mock JsonProcessingException exception) throws JsonProcessingException {
        var mayResult = Jackson.read("{\"value\": v1}", typeReference);
        assertTrue(mayResult.isEmpty());
    }

    @Test
    void should_return_when_unmarshal_success_with_class() {
        var mayResult = Jackson.read("{\"value\":\"v1\"}", TestJacksonClass.class);
        assertTrue(mayResult.isPresent());
        assertThat(mayResult.get().getValue(), is("v1"));
    }

    @Test
    void should_return_empty_when_unmarshal_fail_with_class() {
        var mayResult = Jackson.read("{\"value\": v1}", TestJacksonClass.class);
        assertTrue(mayResult.isEmpty());
    }

    @Test
    void should_return_when_unmarshal_success_with_java_type() {
        var mayResult = Jackson.read("{\"value\":\"v1\"}", javaType);
        assertTrue(mayResult.isPresent());
        assertThat(((TestJacksonClass) mayResult.get()).getValue(), is("v1"));
    }


    @Test
    void should_return_empty_when_unmarshal_fail_with_java_type() {
        var mayResult = Jackson.read("json", javaType);
        assertTrue(mayResult.isEmpty());
    }


    @Test
    void should_return_when_unmarshal_success() throws JsonProcessingException {
        var node = Jackson.createObjectNode();
        node.set("name", new TextNode("value"));
        var mayResult = Jackson.read(Jackson.write(node).orElse(null));
        assertTrue(mayResult.isPresent());
        assertThat(mayResult.get().get("name").asText(), is("value"));
    }

    @Test
    void should_return_empty_when_unmarshal_fail(@Mock JsonProcessingException exception) throws JsonProcessingException {
        var mayResult = Jackson.read("{\"value\": v1}");
        assertTrue(mayResult.isEmpty());
    }

    @Test
    void should_asText() {
        assertNull(asText(null));
        assertNull(asText(null, null));
        assertNull(asText(NullNode.getInstance()));
        assertNull(asText(NullNode.getInstance(), null));
        assertNull(asText(MissingNode.getInstance()));
        assertNull(asText(MissingNode.getInstance(), null));
        assertThat(asText(BooleanNode.FALSE), is("false"));
        assertThat(asText(BooleanNode.TRUE, null), is("true"));
        assertThat(asText(DecimalNode.valueOf(new BigDecimal("1.2222222222222222222222222222222222"))), is("1.2222222222222222222222222222222222"));
        assertThat(asText(DecimalNode.valueOf(new BigDecimal("1.2222222222222222222222222222222222")), null), is("1.2222222222222222222222222222222222"));
        assertThat(asText(Jackson.createObjectNode().put("a", "1.2222222222222222222222222222222222"), "a"), is("1.2222222222222222222222222222222222"));
    }

    @Test
    void should_asBoolean() {
        assertNull(asBoolean(null));
        assertNull(asBoolean(null, null));
        assertNull(asBoolean(NullNode.getInstance()));
        assertNull(asBoolean(NullNode.getInstance(), null));
        assertTrue(asBoolean(BooleanNode.TRUE));
        assertFalse(asBoolean(BooleanNode.FALSE));
        assertNull(asBoolean(DecimalNode.valueOf(new BigDecimal("1.2222222222222222222222222222222222"))));
        assertNull(asBoolean(DecimalNode.valueOf(new BigDecimal("1.2222222222222222222222222222222222")), null));
        assertNull(asBoolean(Jackson.createObjectNode().put("a", "1.2222222222222222222222222222222222"), "a"));
        assertTrue(asBoolean(Jackson.createObjectNode().put("a", "true"), "a"));
        assertFalse(asBoolean(Jackson.createObjectNode().put("a", "false"), "a"));
        assertTrue(asBoolean(Jackson.createObjectNode().put("a", Boolean.TRUE), "a"));
        assertFalse(asBoolean(Jackson.createObjectNode().put("a", Boolean.FALSE), "a"));
    }

    @Test
    void should_asList() {
        var node = Jackson.read("{\"a\": [\"1\",\"2\"], \"b\": [1, 2], \"c\": \"2\"}").get();

        assertNull(Jackson.asList(node, "c", Jackson::asText));
        assertThat(Jackson.asList(node, "a", Jackson::asText), is(List.of("1", "2")));
        assertThat(Jackson.asList(node, "b", Jackson::asInteger), is(List.of(1, 2)));
    }

    @Getter
    @Setter
    private static class TestJacksonClass {
        private String value;
    }
}
