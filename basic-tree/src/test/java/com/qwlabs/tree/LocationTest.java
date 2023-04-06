package com.qwlabs.tree;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationTest {
    @Test
    void should_root() {
        var root = Location.<BomNode>root();
        assertTrue(root.tail().isEmpty());
        assertTrue(root.parent().isEmpty());
        assertTrue(root.isRoot());
        assertTrue(root.map(BomNode::getValue).isRoot());
    }

    @Test
    void should_single_path() {
        var location = Location.of("a");
        assertFalse(location.tail().isEmpty());
        assertThat(location.tail().get(), is("a"));
        assertFalse(location.parent().isEmpty());
        assertFalse(location.isRoot());
        assertTrue(location.parent().get().isRoot());
        assertThat(location.map(n -> n + "dd"), is(Location.of("add")));
    }

    @Test
    void should_multiple_path() {
        var location = Location.of("a", "b");
        assertFalse(location.tail().isEmpty());
        assertThat(location.tail().get(), is("b"));
        assertFalse(location.parent().isEmpty());
        assertFalse(location.isRoot());
        assertFalse(location.parent().get().isRoot());
        assertThat(location.map(n -> n + "dd"), is(Location.of("add", "bdd")));
    }
}