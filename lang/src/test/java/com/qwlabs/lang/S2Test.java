package com.qwlabs.lang;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class S2Test {
    @Test
    void should_isEmpty() {
        assertTrue(S2.isEmpty(null));
        assertTrue(S2.isEmpty(""));
        assertFalse(S2.isEmpty(" "));
        assertFalse(S2.isEmpty("a"));
    }

    @Test
    void should_isNotEmpty() {
        assertFalse(S2.isNotEmpty(null));
        assertFalse(S2.isNotEmpty(""));
        assertTrue(S2.isNotEmpty(" "));
        assertTrue(S2.isNotEmpty("a"));
    }

    @Test
    void should_isBlank() {
        assertTrue(S2.isBlank(null));
        assertTrue(S2.isBlank(""));
        assertTrue(S2.isBlank(" "));
        assertFalse(S2.isBlank("a"));
    }

    @Test
    void should_isNotBlank() {
        assertFalse(S2.isNotBlank(null));
        assertFalse(S2.isNotBlank(""));
        assertFalse(S2.isNotBlank(" "));
        assertTrue(S2.isNotBlank("a"));
    }

    @Test
    void should_ifEmpty() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);
        EmptyConsumer consumer3 = mock(EmptyConsumer.class);
        EmptyConsumer consumer4 = mock(EmptyConsumer.class);

        S2.ifEmpty(null, consumer1);
        S2.ifEmpty("", consumer2);
        S2.ifEmpty(" ", consumer3);
        S2.ifEmpty("a", consumer4);

        verify(consumer1).accept();
        verify(consumer2).accept();
        verify(consumer3, never()).accept();
        verify(consumer4, never()).accept();
    }

    @Test
    void should_ifNotEmpty_empty_customer() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);
        EmptyConsumer consumer3 = mock(EmptyConsumer.class);
        EmptyConsumer consumer4 = mock(EmptyConsumer.class);

        S2.ifNotEmpty(null, consumer1);
        S2.ifNotEmpty("", consumer2);
        S2.ifNotEmpty(" ", consumer3);
        S2.ifNotEmpty("a", consumer4);

        verify(consumer1, never()).accept();
        verify(consumer2, never()).accept();
        verify(consumer3).accept();
        verify(consumer4).accept();
    }

    @Test
    void should_ifNotEmpty_customer() {
        Consumer<String> consumer1 = mock(Consumer.class);
        Consumer<String> consumer2 = mock(Consumer.class);
        Consumer<String> consumer3 = mock(Consumer.class);
        Consumer<String> consumer4 = mock(Consumer.class);

        S2.ifNotEmpty(null, consumer1);
        S2.ifNotEmpty("", consumer2);
        S2.ifNotEmpty(" ", consumer3);
        S2.ifNotEmpty("a", consumer4);

        verify(consumer1, never()).accept(any());
        verify(consumer2, never()).accept(any());
        verify(consumer3).accept(" ");
        verify(consumer4).accept("a");
    }


    @Test
    void should_ifBlank() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);
        EmptyConsumer consumer3 = mock(EmptyConsumer.class);
        EmptyConsumer consumer4 = mock(EmptyConsumer.class);

        S2.ifBlank(null, consumer1);
        S2.ifBlank("", consumer2);
        S2.ifBlank(" ", consumer3);
        S2.ifBlank("a", consumer4);

        verify(consumer1).accept();
        verify(consumer2).accept();
        verify(consumer3).accept();
        verify(consumer4, never()).accept();
    }

    @Test
    void should_ifNotBlank_empty_customer() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);
        EmptyConsumer consumer3 = mock(EmptyConsumer.class);
        EmptyConsumer consumer4 = mock(EmptyConsumer.class);

        S2.ifNotBlank(null, consumer1);
        S2.ifNotBlank("", consumer2);
        S2.ifNotBlank(" ", consumer3);
        S2.ifNotBlank("a", consumer4);

        verify(consumer1, never()).accept();
        verify(consumer2, never()).accept();
        verify(consumer3, never()).accept();
        verify(consumer4).accept();
    }

    @Test
    void should_ifNotBlank_customer() {
        Consumer<String> consumer1 = mock(Consumer.class);
        Consumer<String> consumer2 = mock(Consumer.class);
        Consumer<String> consumer3 = mock(Consumer.class);
        Consumer<String> consumer4 = mock(Consumer.class);

        S2.ifNotBlank(null, consumer1);
        S2.ifNotBlank("", consumer2);
        S2.ifNotBlank(" ", consumer3);
        S2.ifNotBlank("a", consumer4);

        verify(consumer1, never()).accept(any());
        verify(consumer2, never()).accept(any());
        verify(consumer3, never()).accept(any());
        verify(consumer4).accept("a");
    }

    @Test
    void should_remove_starts_with() {
        assertNull(S2.removeStartsWith(null, (String) null));
        assertThat(S2.removeStartsWith("null", (String) null), is("null"));
        assertThat(S2.removeStartsWith("a", "a"), is(""));
        assertThat(S2.removeStartsWith(" a", "a"), is(" a"));
        assertThat(S2.removeStartsWith("a ", "a"), is(" "));
        assertThat(S2.removeStartsWith("ab", "a"), is("b"));
        assertThat(S2.removeStartsWith("ba", "a"), is("ba"));
        assertThat(S2.removeStartsWith("a", "aaa"), is("a"));

        assertThat(S2.removeStartsWith("aiyad", "aiya"), is("d"));
        assertThat(S2.removeStartsWith("aiyadddd", "aiya"), is("dddd"));
        assertThat(S2.removeStartsWith("aiyadddd", "a", "b"), is("iyadddd"));
        assertThat(S2.removeStartsWith("biyadddd", "a", "b"), is("iyadddd"));
    }

    @Test
    void should_remove_ends_with() {
        assertNull(S2.removeEndsWith(null, (String) null));
        assertThat(S2.removeEndsWith("null", (String) null), is("null"));
        assertThat(S2.removeEndsWith("a", "a"), is(""));
        assertThat(S2.removeEndsWith(" a", "a"), is(" "));
        assertThat(S2.removeEndsWith("a ", "a"), is("a "));
        assertThat(S2.removeEndsWith("ab", "a"), is("ab"));
        assertThat(S2.removeEndsWith("ba", "a"), is("b"));
        assertThat(S2.removeEndsWith("a", "aaaa"), is("a"));


        assertThat(S2.removeEndsWith("daiya", "aiya"), is("d"));
        assertThat(S2.removeEndsWith("ddddaiya", "aiya"), is("dddd"));
        assertThat(S2.removeEndsWith("ddddaiya", "a", "b"), is("ddddaiy"));
        assertThat(S2.removeEndsWith("ddddbiyb", "a", "b"), is("ddddbiy"));
    }
}
