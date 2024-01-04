package com.qwlabs.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qwlabs.cdi.SafeCDI;
import jakarta.annotation.Nullable;
import jakarta.enterprise.inject.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

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
            Jackson.objectMapper = SafeCDI.current()
                .map(cdi -> cdi.select(ObjectMapper.class))
                .map(Instance::get)
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
