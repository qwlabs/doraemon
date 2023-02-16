package com.qwlabs.quarkus.test.graphql;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.eclipse.microprofile.config.ConfigProvider;

public interface RestAssuredGraphqlAware {

    default Response given(String body) {
        return given(body, null);
    }

    default Response given(String body, String accessToken) {
        Integer assignedPort = ConfigProvider.getConfig().getValue("quarkus.http.test-port", Integer.class);
        var spec = RestAssured.given()
            .port(assignedPort)
            .body(body);
        if (accessToken != null) {
            spec.auth().oauth2(accessToken);
        }
        return spec.when()
            .accept("application/json")
            .contentType("application/json")
            .post("/graphql");
    }
}
