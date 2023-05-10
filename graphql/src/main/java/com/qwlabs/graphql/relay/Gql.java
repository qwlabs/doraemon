package com.qwlabs.graphql.relay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

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
        return """
            {
                "operationName": "%s",
                "query": "%s",
                "variables": %s
            }
            """.formatted(operationName, query, buildVariables());
    }

    private String buildVariables() {
        if (Objects.isNull(variables)) {
            return DEFAULT_VARIABLES;
        }
        try {
            return initObjectMapper().writeValueAsString(variables);
        } catch (JsonProcessingException e) {
            Throwables.throwIfUnchecked(e);
            return null;
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
