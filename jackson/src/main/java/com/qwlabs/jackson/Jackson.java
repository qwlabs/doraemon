package com.qwlabs.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.qwlabs.cdi.CDI2;
import jakarta.annotation.Nullable;
import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public final class Jackson {
    private static ObjectMapper objectMapper;

    private Jackson() {
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        Jackson.objectMapper = objectMapper;
    }

    private static ObjectMapper get() {
        if (objectMapper == null) {
            Jackson.objectMapper = CDI2.select(ObjectMapper.class)
                .map(Provider::get)
                .orElseGet(ObjectMapper::new);
        }
        return Jackson.objectMapper;
    }

    public static ObjectMapper getMapper() {
        return get();
    }

    public static ObjectNode createObjectNode() {
        return get().createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return get().createArrayNode();
    }

    public static <T> Optional<String> write(T object) {
        try {
            return Optional.ofNullable(get().writeValueAsString(object));
        } catch (Exception e) {
            LOGGER.warn("can not write object {}.", object);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> read(String json, TypeReference<T> typeReference) {
        try {
            return Optional.ofNullable(get().readValue(json, typeReference));
        } catch (Exception e) {
            LOGGER.warn("can not read json: \"{}\" to type:{}.", json, typeReference);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> read(String json, JavaType javaType) {
        try {
            return Optional.ofNullable(get().readValue(json, javaType));
        } catch (Exception e) {
            LOGGER.warn("can not read json: \"{}\" to type:{}.", json, javaType);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> read(String json, Class<T> type) {
        try {
            return Optional.ofNullable(get().readValue(json, type));
        } catch (Exception e) {
            LOGGER.warn("can not read json: \"{}\" to type:{}.", json, type);
            return Optional.empty();
        }
    }

    public static Optional<JsonNode> read(String json) {
        try {
            return Optional.ofNullable(get().readTree(json));
        } catch (Exception e) {
            LOGGER.warn("can not read json: \"{}\" to json node.", json);
            return Optional.empty();
        }
    }

    public static <T> List<T> asList(@Nullable JsonNode node, Function<JsonNode, T> mapper) {
        if (Objects.isNull(node)) {
            return null;
        }
        if (!node.isArray()) {
            return null;
        }
        List<T> result = Lists.newArrayList();
        node.iterator().forEachRemaining(element -> result.add(mapper.apply(element)));
        return result;
    }

    public static <T> List<T> asList(@Nullable JsonNode node, @Nullable String propertyName, Function<JsonNode, T> mapper) {
        return Optional.ofNullable(node)
            .map(n -> n.get(propertyName))
            .map(n -> Jackson.asList(n, mapper))
            .orElse(null);
    }

    public static Integer asInteger(@Nullable JsonNode node) {
        return asInteger(node, null);
    }

    public static Integer asInteger(@Nullable JsonNode node, @Nullable String propertyName) {
        return Optional.ofNullable(asText(node, propertyName))
            .map(Ints::tryParse)
            .orElse(null);
    }

    public static Boolean asBoolean(@Nullable JsonNode node) {
        return asBoolean(node, null);
    }

    public static Boolean asBoolean(@Nullable JsonNode node, @Nullable String propertyName) {
        return Optional.ofNullable(asText(node, propertyName))
            .map(value -> {
                if (value.equals(Boolean.TRUE.toString())) {
                    return Boolean.TRUE;
                }
                if (value.equals(Boolean.FALSE.toString())) {
                    return Boolean.FALSE;
                }
                return null;
            })
            .orElse(null);
    }

    public static String asText(@Nullable JsonNode node, @Nullable String propertyName) {
        if (propertyName == null) {
            return asText(node);
        }
        return Optional.ofNullable(node.get(propertyName))
            .map(Jackson::asText)
            .orElse(null);
    }

    public static String asText(@Nullable JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        try {
            return node.asText();
        } catch (Exception e) {
            LOGGER.error("JsonNode asText() error.", e);
            return null;
        }
    }
}
