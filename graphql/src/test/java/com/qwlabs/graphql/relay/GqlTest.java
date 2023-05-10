package com.qwlabs.graphql.relay;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class GqlTest {
    @Test
    void should_build() {
        var input = new CreateAsnInput();
        input.setName("2");

        var payload = Gql.of("createAsn")
            .query("""
                mutation createAsn($input:CreateAsnInput!){
                    createAsn(input:$input){
                        id
                        quantity
                    }
                }
                """)
            .input(input)
            .build();
        assertThat(payload, is("{\n    \"operationName\": \"createAsn\",\n    \"query\": \"mutation createAsn($input:CreateAsnInput!){\n    createAsn(input:$input){\n        id\n        quantity\n    }\n}\n\",\n    \"variables\": {\"input\":{\"name\":\"2\"}}\n}\n"));
    }

    @Getter
    @Setter
    private static class CreateAsnInput {
        private String name;
    }
}
