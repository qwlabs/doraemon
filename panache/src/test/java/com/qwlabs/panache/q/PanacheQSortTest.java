package com.qwlabs.panache.q;

import com.qwlabs.q.sort.QSort;
import com.qwlabs.q.sort.QSortPredicates;
import io.quarkus.panache.hibernate.common.runtime.PanacheJpaUtil;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class PanacheQSortTest {
    @Test
    void should_format() {
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(null).format()), is(""));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(null).format(QSort.empty())), is(""));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(null).format("createdAt+")), is(" ORDER BY createdAt"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(null).format("createdAt-")), is(" ORDER BY createdAt DESC"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(null).format("createdAt-,updatedAt+")), is(" ORDER BY createdAt DESC , updatedAt"));

        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("updatedAt+")).format()), is(" ORDER BY updatedAt"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("updatedAt+")).format(QSort.empty())), is(" ORDER BY updatedAt"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("updatedAt+")).format("createdAt-")), is(" ORDER BY updatedAt"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("updatedAt-")).format("createdAt-")), is(" ORDER BY updatedAt DESC"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("updatedAt-,createdAt+")).format("createdAt-")), is(" ORDER BY updatedAt DESC , createdAt"));

        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g1"), "p1").with(QSortPredicates.includesStartWith("g2"), "p2").format("createdAt-")), is(" ORDER BY p1.g1_1 DESC , p1.g1_2 , p2.g2_1 DESC , p2.g2_2"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("createdAt-")), is(" ORDER BY createdAt DESC"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.of(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("p1.createdAt-")), is(" ORDER BY p1.createdAt DESC"));
    }

    @Test
    void should_format_native() {
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(null).format()), is(""));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(null).format(QSort.empty())), is(""));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(null).format("createdAt+")), is(" ORDER BY created_at"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(null).format("createdAt-")), is(" ORDER BY created_at DESC"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(null).format("createdAt-,updatedAt+")), is(" ORDER BY created_at DESC , updated_at"));

        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("updatedAt+")).format()), is(" ORDER BY updated_at"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("updatedAt+")).format(QSort.empty())), is(" ORDER BY updated_at"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("updatedAt+")).format("createdAt-")), is(" ORDER BY updated_at"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("updatedAt-")).format("createdAt-")), is(" ORDER BY updated_at DESC"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("updatedAt-,createdAt+")).format("createdAt-")), is(" ORDER BY updated_at DESC , created_at"));

        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g1"), "p1").with(QSortPredicates.includesStartWith("g2"), "p2").format("createdAt-")), is(" ORDER BY p1.g1_1 DESC , p1.g1_2 , p2.g2_1 DESC , p2.g2_2"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("createdAt-")), is(" ORDER BY created_at DESC"));
        assertThat(PanacheJpaUtil.toOrderBy(PanacheQSort.ofNative(QSort.of("g1_1-,g1_2+,g2_1-,g2_2+")).with(QSortPredicates.includesStartWith("g3"), "p3").with(QSortPredicates.includesStartWith("g4"), "p4").format("p1.createdAt-")), is(" ORDER BY p1.created_at DESC"));
    }
}
