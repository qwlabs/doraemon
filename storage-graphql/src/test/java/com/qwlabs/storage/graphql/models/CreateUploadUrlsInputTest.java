package com.qwlabs.storage.graphql.models;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateUploadUrlsInputTest {

    @Test
    void should_of_string_context() {
        var input = CreateUploadUrlsInput.builder()
                .businessType("bt")
                .context("{\"a\": \"1\"}")
                .build();

        var context = input.of();
        assertThat(context.getBusinessType(), is("bt"));
        assertNotNull(context.get());
        assertThat(context.get().size(), is(1));
        assertThat(context.getString("a").orElse(null), is("1"));
        assertThat(context.getInteger("a").orElse(null), is(1));
        assertThat(context.getLong("a").orElse(null), is(1L));
    }

    @Test
    void should_of_map_context() {
        var input = CreateUploadUrlsInput.builder()
                .businessType("bt")
                .context(Map.of("a", "1", "b", 1, "c", 1L))
                .build();

        var context = input.of();
        assertThat(context.getBusinessType(), is("bt"));
        assertNotNull(context.get());
        assertThat(context.get().size(), is(3));

        assertThat(context.getString("a").orElse(null), is("1"));
        assertThat(context.getString("b").orElse(null), is("1"));
        assertThat(context.getString("c").orElse(null), is("1"));

        assertThat(context.getInteger("a").orElse(null), is(1));
        assertThat(context.getInteger("b").orElse(null), is(1));
        assertThat(context.getInteger("c").orElse(null), is(1));

        assertThat(context.getLong("a").orElse(null), is(1L));
        assertThat(context.getLong("b").orElse(null), is(1L));
        assertThat(context.getLong("c").orElse(null), is(1L));

    }
}