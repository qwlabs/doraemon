package com.qwlabs.graphql.relay;

import com.qwlabs.graphql.Gql;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;


class GqlTest {
    @Test
    void should_build() {
        var input = new CreateAsnInput();
        input.setName("2");

        var payload = Gql.of("""
                mutation createAsn($input:CreateAsnInput!){
                    createAsn(input:$input){
                        id
                        quantity
                    }
                }
                """)
            .input(input)
            .build();
//        edges{node{id readFlag status updatedAt announcement{title content org{id name code}}}}}}
//        assertThat(payload, is("{\"query\":\"mutation createAsn($input:CreateAsnInput!){\n    createAsn(input:$input){\n        id\n        quantity\n    }\n}\n\",\"variables\":{\"input\":{\"name\":\"2\"}}}"));
    }

    @Getter
    @Setter
    private static class CreateAsnInput {
        private String name;
    }
}
