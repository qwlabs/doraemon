package com.qwlabs.q.sort;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class QSortTest {
    @Test
    void should_string() {
        assertThat(QSort.of(null).format(), is(""));
        assertThat(QSort.of(null).format("1"), is("1"));
        assertThat(QSort.of(null).format(",", "asc", "desc"), is(""));
        assertThat(QSort.of(null).format(",", "asc", "desc", "1"), is("1"));
        assertThat(QSort.of(null).format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(QSort.of(null).format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));


        assertThat(QSort.of("").format(), is(""));
        assertThat(QSort.of("").format("1"), is("1"));
        assertThat(QSort.of("").format(",", "asc", "desc"), is(""));
        assertThat(QSort.of("").format(",", "asc", "desc", "1"), is("1"));
        assertThat(QSort.of("").format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(QSort.of("").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));


        assertThat(QSort.of(" ").format(), is(""));
        assertThat(QSort.of(" ").format("1"), is("1"));
        assertThat(QSort.of(" ").format(",", "asc", "desc"), is(""));
        assertThat(QSort.of(" ").format(",", "asc", "desc", "1"), is("1"));
        assertThat(QSort.of(" ").format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(QSort.of(" ").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));


        assertThat(QSort.of("a").format(), is(""));
        assertThat(QSort.of("a").format("1"), is("1"));
        assertThat(QSort.of("a").format(",", "asc", "desc"), is(""));
        assertThat(QSort.of("a").format(",", "asc", "desc", "1"), is("1"));
        assertThat(QSort.of("a").format(",", (field) -> field + "asc", (field) -> field + "desc"), is(""));
        assertThat(QSort.of("a").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1"));

        assertThat(QSort.of("a+").format(), is("a asc"));
        assertThat(QSort.of("a+").format("1"), is("a asc"));
        assertThat(QSort.of("a+").format(",", "asc", "desc"), is("a asc"));
        assertThat(QSort.of("a+").format(",", "asc", "desc", "1"), is("a asc"));
        assertThat(QSort.of("a+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("aasc"));
        assertThat(QSort.of("a+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("aasc"));

        assertThat(QSort.of("a-").format(), is("a desc"));
        assertThat(QSort.of("a-").format("1"), is("a desc"));
        assertThat(QSort.of("a-").format(",", "asc", "desc"), is("a desc"));
        assertThat(QSort.of("a-").format(",", "asc", "desc", "1"), is("a desc"));
        assertThat(QSort.of("a-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("adesc"));
        assertThat(QSort.of("a-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("adesc"));


        assertThat(QSort.of("a1+").format(), is("a1 asc"));
        assertThat(QSort.of("a1+").format("1"), is("a1 asc"));
        assertThat(QSort.of("a1+").format(",", "asc", "desc"), is("a1 asc"));
        assertThat(QSort.of("a1+").format(",", "asc", "desc", "1"), is("a1 asc"));
        assertThat(QSort.of("a1+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("a1asc"));
        assertThat(QSort.of("a1+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("a1asc"));

        assertThat(QSort.of("a1-").format(), is("a1 desc"));
        assertThat(QSort.of("a1-").format("1"), is("a1 desc"));
        assertThat(QSort.of("a1-").format(",", "asc", "desc"), is("a1 desc"));
        assertThat(QSort.of("a1-").format(",", "asc", "desc", "1"), is("a1 desc"));
        assertThat(QSort.of("a1-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("a1desc"));
        assertThat(QSort.of("a1-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("a1desc"));


        assertThat(QSort.of("1a+").format(), is("1a asc"));
        assertThat(QSort.of("1a+").format("1"), is("1a asc"));
        assertThat(QSort.of("1a+").format(",", "asc", "desc"), is("1a asc"));
        assertThat(QSort.of("1a+").format(",", "asc", "desc", "1"), is("1a asc"));
        assertThat(QSort.of("1a+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("1aasc"));
        assertThat(QSort.of("1a+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1aasc"));

        assertThat(QSort.of("1a-").format(), is("1a desc"));
        assertThat(QSort.of("1a-").format("1"), is("1a desc"));
        assertThat(QSort.of("1a-").format(",", "asc", "desc"), is("1a desc"));
        assertThat(QSort.of("1a-").format(",", "asc", "desc", "1"), is("1a desc"));
        assertThat(QSort.of("1a-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("1adesc"));
        assertThat(QSort.of("1a-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("1adesc"));


        assertThat(QSort.of("a+,a-").format(), is("a asc"));
        assertThat(QSort.of("a+,a-").format("1"), is("a asc"));
        assertThat(QSort.of("a+,a-").format(",", "asc", "desc"), is("a asc"));
        assertThat(QSort.of("a+,a-").format(",", "asc", "desc", "1"), is("a asc"));
        assertThat(QSort.of("a+,a-").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("aasc"));
        assertThat(QSort.of("a+,a-").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("aasc"));

        assertThat(QSort.of("a-,a+").format(), is("a desc"));
        assertThat(QSort.of("a-,a+").format("1"), is("a desc"));
        assertThat(QSort.of("a-,a+").format(",", "asc", "desc"), is("a desc"));
        assertThat(QSort.of("a-,a+").format(",", "asc", "desc", "1"), is("a desc"));
        assertThat(QSort.of("a-,a+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("adesc"));
        assertThat(QSort.of("a-,a+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("adesc"));

        assertThat(QSort.of("a-,b+").format(), is("a desc , b asc"));
        assertThat(QSort.of("a-,b+").format("1"), is("a desc , b asc"));
        assertThat(QSort.of("a-,b+").format(",", "asc", "desc"), is("a desc , b asc"));
        assertThat(QSort.of("a-,b+").format(",", "asc", "desc", "1"), is("a desc , b asc"));
        assertThat(QSort.of("a-,b+").format(",", (field) -> field + "asc", (field) -> field + "desc"), is("adesc , basc"));
        assertThat(QSort.of("a-,b+").format(",", (field) -> field + "asc", (field) -> field + "desc", "1"), is("adesc , basc"));

    }
}
