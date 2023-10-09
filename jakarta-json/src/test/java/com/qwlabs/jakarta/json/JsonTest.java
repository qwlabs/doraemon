package com.qwlabs.jakarta.json;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JsonTest {
    private JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(null);
    private String leftEmpty = """
        {
            "name": "  d",
            "value": 3
        }
        """;

    private String rightEmpty = """
        {
            "name": "d ",
            "value": 3
        }
        """;

    private String allEmpty = """
        {
            "name": "  d ",
            "value": 3
        }
        """;

    @Test
    void should_trim() {
        assertThat(read(leftEmpty).getString("name"), is("d"));
        assertThat(read(rightEmpty).getString("name"), is("d"));
        assertThat(read(allEmpty).getString("name"), is("d"));
    }


    private JsonObject read(String body) {
        try (StringReader bodyReader = new StringReader(body);
             JsonReader jsonReader = jsonReaderFactory.createReader(bodyReader)) {
            return jsonReader.readObject();
        }
    }
}
