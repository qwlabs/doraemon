package com.qwlabs.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class Gql {
    private static final String INPUT = "input";
    private static final String DEFAULT_VARIABLES = "{}";
    private static ObjectMapper objectMapper;
    private final String operationName;
    private String query;
    private Object variables;

    public Gql(String operationName) {
        this.operationName = operationName;
    }

    public Gql query(String query) {
        this.query = query;
        return this;
    }

    public Gql variables(Map<String, Object> variables) {
        this.variables = variables;
        return this;
    }

    public Gql input(Object input) {
        return variables(Map.of(INPUT, input));
    }

    public String build() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("operationName", operationName);
        result.put("query", query);
        Optional.ofNullable(variables).ifPresent(vars -> result.put("variables", variables));
        try {
            return initObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void objectMapper(@NotNull ObjectMapper objectMapper) {
        Gql.objectMapper = objectMapper;
    }

    private static ObjectMapper initObjectMapper() {
        if (Gql.objectMapper == null) {
            Gql.objectMapper = new ObjectMapper();
        }
        return Gql.objectMapper;
    }

    public static Gql of(@NotNull String operationName) {
        return new Gql(operationName);
    }
}
