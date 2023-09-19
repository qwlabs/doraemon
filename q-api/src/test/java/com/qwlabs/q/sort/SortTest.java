package com.qwlabs.q.sort;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SortTest {
    @Test
    void should_string() {
        assertThat(Sort.of(null).format(), is(""));
        assertThat(Sort.of(null).format("1"), is("1"));
        assertThat(Sort.of(null).format(",", "asc", "desc"), is(""));
        assertThat(Sort.of(null).format(",", "asc", "desc", "1"), is("1"));
        assertThat(Sort.of(null).format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(Sort.of(null).format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));


        assertThat(Sort.of("").format(), is(""));
        assertThat(Sort.of("").format("1"), is("1"));
        assertThat(Sort.of("").format(",", "asc", "desc"), is(""));
        assertThat(Sort.of("").format(",", "asc", "desc", "1"), is("1"));
        assertThat(Sort.of("").format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(Sort.of("").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));


        assertThat(Sort.of(" ").format(), is(""));
        assertThat(Sort.of(" ").format("1"), is("1"));
        assertThat(Sort.of(" ").format(",", "asc", "desc"), is(""));
        assertThat(Sort.of(" ").format(",", "asc", "desc", "1"), is("1"));
        assertThat(Sort.of(" ").format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(Sort.of(" ").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));


        assertThat(Sort.of("a").format(), is(""));
        assertThat(Sort.of("a").format("1"), is("1"));
        assertThat(Sort.of("a").format(",", "asc", "desc"), is(""));
        assertThat(Sort.of("a").format(",", "asc", "desc", "1"), is("1"));
        assertThat(Sort.of("a").format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(Sort.of("a").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));

        assertThat(Sort.of("a+").format(), is("a asc"));
        assertThat(Sort.of("a+").format("1"), is("a asc"));
        assertThat(Sort.of("a+").format(",", "asc", "desc"), is("a asc"));
        assertThat(Sort.of("a+").format(",", "asc", "desc", "1"), is("a asc"));
        assertThat(Sort.of("a+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("aasc"));
        assertThat(Sort.of("a+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("aasc"));

        assertThat(Sort.of("a-").format(), is("a desc"));
        assertThat(Sort.of("a-").format("1"), is("a desc"));
        assertThat(Sort.of("a-").format(",", "asc", "desc"), is("a desc"));
        assertThat(Sort.of("a-").format(",", "asc", "desc", "1"), is("a desc"));
        assertThat(Sort.of("a-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("adesc"));
        assertThat(Sort.of("a-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("adesc"));


        assertThat(Sort.of("a1+").format(), is("a1 asc"));
        assertThat(Sort.of("a1+").format("1"), is("a1 asc"));
        assertThat(Sort.of("a1+").format(",", "asc", "desc"), is("a1 asc"));
        assertThat(Sort.of("a1+").format(",", "asc", "desc", "1"), is("a1 asc"));
        assertThat(Sort.of("a1+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("a1asc"));
        assertThat(Sort.of("a1+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("a1asc"));

        assertThat(Sort.of("a1-").format(), is("a1 desc"));
        assertThat(Sort.of("a1-").format("1"), is("a1 desc"));
        assertThat(Sort.of("a1-").format(",", "asc", "desc"), is("a1 desc"));
        assertThat(Sort.of("a1-").format(",", "asc", "desc", "1"), is("a1 desc"));
        assertThat(Sort.of("a1-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("a1desc"));
        assertThat(Sort.of("a1-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("a1desc"));


        assertThat(Sort.of("1a+").format(), is("1a asc"));
        assertThat(Sort.of("1a+").format("1"), is("1a asc"));
        assertThat(Sort.of("1a+").format(",", "asc", "desc"), is("1a asc"));
        assertThat(Sort.of("1a+").format(",", "asc", "desc", "1"), is("1a asc"));
        assertThat(Sort.of("1a+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("1aasc"));
        assertThat(Sort.of("1a+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1aasc"));

        assertThat(Sort.of("1a-").format(), is("1a desc"));
        assertThat(Sort.of("1a-").format("1"), is("1a desc"));
        assertThat(Sort.of("1a-").format(",", "asc", "desc"), is("1a desc"));
        assertThat(Sort.of("1a-").format(",", "asc", "desc", "1"), is("1a desc"));
        assertThat(Sort.of("1a-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("1adesc"));
        assertThat(Sort.of("1a-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1adesc"));


        assertThat(Sort.of("a+,a-").format(), is("a asc"));
        assertThat(Sort.of("a+,a-").format("1"), is("a asc"));
        assertThat(Sort.of("a+,a-").format(",", "asc", "desc"), is("a asc"));
        assertThat(Sort.of("a+,a-").format(",", "asc", "desc", "1"), is("a asc"));
        assertThat(Sort.of("a+,a-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("aasc"));
        assertThat(Sort.of("a+,a-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("aasc"));

        assertThat(Sort.of("a-,a+").format(), is("a desc"));
        assertThat(Sort.of("a-,a+").format("1"), is("a desc"));
        assertThat(Sort.of("a-,a+").format(",", "asc", "desc"), is("a desc"));
        assertThat(Sort.of("a-,a+").format(",", "asc", "desc", "1"), is("a desc"));
        assertThat(Sort.of("a-,a+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("adesc"));
        assertThat(Sort.of("a-,a+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("adesc"));

        assertThat(Sort.of("a-,b+").format(), is("a desc , b asc"));
        assertThat(Sort.of("a-,b+").format("1"), is("a desc , b asc"));
        assertThat(Sort.of("a-,b+").format(",", "asc", "desc"), is("a desc , b asc"));
        assertThat(Sort.of("a-,b+").format(",", "asc", "desc", "1"), is("a desc , b asc"));
        assertThat(Sort.of("a-,b+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("adesc , basc"));
        assertThat(Sort.of("a-,b+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("adesc , basc"));

    }
}
