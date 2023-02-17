package com.qwlabs.quarkus.test.graphql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.eclipse.microprofile.config.ConfigProvider;

public class GraphQLRequest {
    public static final String DEFAULT_TENANT_HEADER_NAME = "X-TENANT-ID";
    public static final String DEFAULT_GRAPHQL_ENDPOINT = "/graphql";

    private final RequestSpecification request;

    public GraphQLRequest() {
        this(RestAssured.given());
    }

    public GraphQLRequest(RequestSpecification request) {
        this.request = request;
        setup();
    }

    private void setup() {
        request.port(ConfigProvider.getConfig()
                        .getValue("quarkus.http.test-port", Integer.class))
                .contentType(ContentType.JSON).accept(ContentType.JSON);
    }

    public ValidatableResponse then() {
        return then(DEFAULT_GRAPHQL_ENDPOINT);
    }

    public ValidatableResponse then(String endpoint) {
        return request.post()
                .then()
                .log()
                .all()
                .and();
    }

    public GraphQLRequest body(Object body) {
        request.body(body);
        return this;
    }

    public GraphQLRequest tenant(String tenantId) {
        if (tenantId != null) {
            request.header(DEFAULT_TENANT_HEADER_NAME, tenantId);
        }
        return this;
    }

    public GraphQLRequest token(String accessToken) {
        if (accessToken != null) {
            request.auth().oauth2(accessToken);
        }
        return this;
    }

    public static GraphQLRequest given() {
        return given(null);
    }

    public static GraphQLRequest given(String body) {
        return given(body, null);
    }

    public static GraphQLRequest given(String body, String accessToken) {
        return new GraphQLRequest().body(body).token(accessToken);
    }
}
