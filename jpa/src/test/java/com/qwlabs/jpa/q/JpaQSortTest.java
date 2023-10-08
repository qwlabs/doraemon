package com.qwlabs.jpa.q;

import com.qwlabs.q.sort.QSort;
import com.qwlabs.q.sort.QSortPredicates;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JpaQSortTest {
    @Test
    void should_format() {
        assertThat(JpaQSort.of(null).format(), is(""));
        assertThat(JpaQSort.of(null).format(QSort.empty()), is(""));
        assertThat(JpaQSort.of(null).format("createdAt+"), is("createdAt asc"));
        assertThat(JpaQSort.of(null).format("createdAt-"), is("createdAt desc"));
        assertThat(JpaQSort.of(null).format("createdAt-,updatedAt+"), is("createdAt desc,updatedAt asc"));

        assertThat(JpaQSort.of(QSort.of("updatedAt+")).format(), is("updatedAt asc"));
        assertThat(JpaQSort.of(QSort.of("updatedAt+")).format(QSort.empty()), is("updatedAt asc"));
        assertThat(JpaQSort.of(QSort.of("updatedAt+")).format("createdAt-"), is("updatedAt asc"));
        assertThat(JpaQSort.of(QSort.of("updatedAt-")).format("createdAt-"), is("updatedAt desc"));
        assertThat(JpaQSort.of(QSort.of("updatedAt-,createdAt+")).format("createdAt-"), is("updatedAt desc,createdAt asc"));

        assertThat(JpaQSort.of(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g1"), "p1").with(QSortPredicates.includesStartWith("g2"), "p2").format("createdAt-"), is("p1.g1_1 desc,p1.g1_2 asc,p2.g2_1 desc,p2.g2_2 asc"));
        assertThat(JpaQSort.of(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("createdAt-"), is("createdAt desc"));
        assertThat(JpaQSort.of(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("p1.createdAt-"), is("p1.createdAt desc"));
    }

    @Test
    void should_format_native() {
        assertThat(JpaQSort.ofNative(null).format(), is(""));
        assertThat(JpaQSort.ofNative(null).format(QSort.empty()), is(""));
        assertThat(JpaQSort.ofNative(null).format("createdAt+"), is("created_at asc"));
        assertThat(JpaQSort.ofNative(null).format("createdAt-"), is("created_at desc"));
        assertThat(JpaQSort.ofNative(null).format("createdAt-,updatedAt+"), is("created_at desc,updated_at asc"));

        assertThat(JpaQSort.ofNative(QSort.of("updatedAt+")).format(), is("updated_at asc"));
        assertThat(JpaQSort.ofNative(QSort.of("updatedAt+")).format(QSort.empty()), is("updated_at asc"));
        assertThat(JpaQSort.ofNative(QSort.of("updatedAt+")).format("createdAt-"), is("updated_at asc"));
        assertThat(JpaQSort.ofNative(QSort.of("updatedAt-")).format("createdAt-"), is("updated_at desc"));
        assertThat(JpaQSort.ofNative(QSort.of("updatedAt-,createdAt+")).format("createdAt-"), is("updated_at desc,created_at asc"));

        assertThat(JpaQSort.ofNative(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g1"), "p1").with(QSortPredicates.includesStartWith("g2"), "p2").format("createdAt-"), is("p1.g1_1 desc,p1.g1_2 asc,p2.g2_1 desc,p2.g2_2 asc"));
        assertThat(JpaQSort.ofNative(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("createdAt-"), is("created_at desc"));
        assertThat(JpaQSort.ofNative(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("p1.createdAt-"), is("p1.created_at desc"));
    }
}
