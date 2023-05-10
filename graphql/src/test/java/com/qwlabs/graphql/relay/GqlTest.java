package com.qwlabs.graphql.relay;

import com.qwlabs.graphql.Gql;
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
//        payload = payload.replaceAll("\n", "");
//        assertThat(payload, is("{\"operationName\":\"createAsn\",\"query\":\"mutation createAsn($input:CreateAsnInput!){\n    createAsn(input:$input){\n        id\n        quantity\n    }\n}\n\",\"variables\":{\"input\":{\"name\":\"2\"}}}"));
    }

    @Getter
    @Setter
    private static class CreateAsnInput {
        private String name;
    }
}
