package com.qwlabs.lang;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class C2Test {
    @Test
    void should_isEmpty() {
        assertTrue(C2.isEmpty((Collection<String>) null));
        assertTrue(C2.isEmpty((Map<String, String>) null));
        assertTrue(C2.isEmpty(Set.of()));
        assertTrue(C2.isEmpty(List.of()));
        assertTrue(C2.isEmpty(Map.of()));

        assertFalse(C2.isEmpty(Set.of("1")));
        assertFalse(C2.isEmpty(List.of("1")));
        assertFalse(C2.isEmpty(Map.of("1", "2")));
    }

    @Test
    void should_isNotEmpty() {
        assertFalse(C2.isNotEmpty((Collection<String>) null));
        assertFalse(C2.isNotEmpty((Map<String, String>) null));
        assertFalse(C2.isNotEmpty(Set.of()));
        assertFalse(C2.isNotEmpty(List.of()));
        assertFalse(C2.isNotEmpty(Map.of()));

        assertTrue(C2.isNotEmpty(Set.of("1")));
        assertTrue(C2.isNotEmpty(List.of("1")));
        assertTrue(C2.isNotEmpty(Map.of("1", "2")));
    }

    @Test
    void should_ifEmpty_empty_consumer() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);
        EmptyConsumer consumer3 = mock(EmptyConsumer.class);
        EmptyConsumer consumer4 = mock(EmptyConsumer.class);
        EmptyConsumer consumer5 = mock(EmptyConsumer.class);
        EmptyConsumer consumer6 = mock(EmptyConsumer.class);
        EmptyConsumer consumer7 = mock(EmptyConsumer.class);
        EmptyConsumer consumer8 = mock(EmptyConsumer.class);


        C2.ifEmpty((Collection<String>) null, consumer1);
        C2.ifEmpty((Map<String, String>) null, consumer2);
        C2.ifEmpty(Set.of(), consumer3);
        C2.ifEmpty(List.of(), consumer4);
        C2.ifEmpty(Map.of(), consumer5);

        C2.ifEmpty(Set.of("1"), consumer6);
        C2.ifEmpty(List.of("1"), consumer7);
        C2.ifEmpty(Map.of("1", "2"), consumer8);

        verify(consumer1).accept();
        verify(consumer2).accept();
        verify(consumer3).accept();
        verify(consumer4).accept();
        verify(consumer5).accept();
        verify(consumer6, never()).accept();
        verify(consumer7, never()).accept();
        verify(consumer8, never()).accept();
    }

    @Test
    void should_ifEmpty_supplier() {
        Supplier<String> supplier1 = mock(Supplier.class);
        Supplier<String> supplier2 = mock(Supplier.class);
        Supplier<String> supplier3 = mock(Supplier.class);
        Supplier<String> supplier4 = mock(Supplier.class);
        Supplier<String> supplier5 = mock(Supplier.class);
        Supplier<String> supplier6 = mock(Supplier.class);
        Supplier<String> supplier7 = mock(Supplier.class);
        Supplier<String> supplier8 = mock(Supplier.class);

        Set<String> set = Set.of("1");
        List<String> list = List.of("1");
        Map<String, String> map = Map.of("1", "2");

        when(supplier1.get()).thenReturn("1");
        when(supplier2.get()).thenReturn("2");
        when(supplier3.get()).thenReturn("3");
        when(supplier4.get()).thenReturn("4");
        when(supplier5.get()).thenReturn("5");
        when(supplier6.get()).thenReturn("6");


        Optional<String> result1 = C2.ifEmpty((Collection<String>) null, supplier1);
        Optional<String> result2 = C2.ifEmpty((Map<String, String>) null, supplier2);
        Optional<String> result3 = C2.ifEmpty(Set.of(), supplier3);
        Optional<String> result4 = C2.ifEmpty(List.of(), supplier4);
        Optional<String> result5 = C2.ifEmpty(Map.of(), supplier5);

        Optional<String> result6 = C2.ifEmpty(set, supplier6);
        Optional<String> result7 = C2.ifEmpty(list, supplier7);
        Optional<String> result8 = C2.ifEmpty(map, supplier8);


        assertThat(result1.get(), is("1"));
        assertThat(result2.get(), is("2"));
        assertThat(result3.get(), is("3"));
        assertThat(result4.get(), is("4"));
        assertThat(result5.get(), is("5"));

        assertTrue(result6.isEmpty());
        assertTrue(result7.isEmpty());
        assertTrue(result8.isEmpty());

        verify(supplier1).get();
        verify(supplier2).get();
        verify(supplier3).get();
        verify(supplier4).get();
        verify(supplier5).get();
        verify(supplier6, never()).get();
        verify(supplier7, never()).get();
        verify(supplier8, never()).get();
    }


    @Test
    void should_ifNotEmpty_empty_customer() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);
        EmptyConsumer consumer3 = mock(EmptyConsumer.class);
        EmptyConsumer consumer4 = mock(EmptyConsumer.class);
        EmptyConsumer consumer5 = mock(EmptyConsumer.class);
        EmptyConsumer consumer6 = mock(EmptyConsumer.class);
        EmptyConsumer consumer7 = mock(EmptyConsumer.class);
        EmptyConsumer consumer8 = mock(EmptyConsumer.class);


        C2.ifNotEmpty((Collection<String>) null, consumer1);
        C2.ifNotEmpty((Map<String, String>) null, consumer2);
        C2.ifNotEmpty(Set.of(), consumer3);
        C2.ifNotEmpty(List.of(), consumer4);
        C2.ifNotEmpty(Map.of(), consumer5);

        C2.ifNotEmpty(Set.of("1"), consumer6);
        C2.ifNotEmpty(List.of("1"), consumer7);
        C2.ifNotEmpty(Map.of("1", "2"), consumer8);

        verify(consumer1, never()).accept();
        verify(consumer2, never()).accept();
        verify(consumer3, never()).accept();
        verify(consumer4, never()).accept();
        verify(consumer5, never()).accept();
        verify(consumer6).accept();
        verify(consumer7).accept();
        verify(consumer8).accept();
    }

    @Test
    void should_ifNotEmpty_customer() {
        Consumer<Collection<String>> consumer1 = mock(Consumer.class);
        Consumer<Map<String, String>> consumer2 = mock(Consumer.class);
        Consumer<Set<String>> consumer3 = mock(Consumer.class);
        Consumer<List<String>> consumer4 = mock(Consumer.class);
        Consumer<Map<String, String>> consumer5 = mock(Consumer.class);
        Consumer<Set<String>> consumer6 = mock(Consumer.class);
        Consumer<List<String>> consumer7 = mock(Consumer.class);
        Consumer<Map<String, String>> consumer8 = mock(Consumer.class);
        Set<String> set = Set.of("1");
        List<String> list = List.of("1");
        Map<String, String> map = Map.of("1", "2");


        C2.ifNotEmpty((Collection<String>) null, consumer1);
        C2.ifNotEmpty((Map<String, String>) null, consumer2);
        C2.ifNotEmpty(Set.of(), consumer3);
        C2.ifNotEmpty(List.of(), consumer4);
        C2.ifNotEmpty(Map.of(), consumer5);

        C2.ifNotEmpty(set, consumer6);
        C2.ifNotEmpty(list, consumer7);
        C2.ifNotEmpty(map, consumer8);

        verify(consumer1, never()).accept(any());
        verify(consumer2, never()).accept(any());
        verify(consumer3, never()).accept(any());
        verify(consumer4, never()).accept(any());
        verify(consumer5, never()).accept(any());
        verify(consumer6).accept(set);
        verify(consumer7).accept(list);
        verify(consumer8).accept(map);
    }

    @Test
    void should_ifNotEmpty_function() {
        Function<Collection<String>, String> mapper1 = mock(Function.class);
        Function<Map<String, String>, String> mapper2 = mock(Function.class);
        Function<Set<String>, String> mapper3 = mock(Function.class);
        Function<List<String>, String> mapper4 = mock(Function.class);
        Function<Map<String, String>, String> mapper5 = mock(Function.class);
        Function<Set<String>, String> mapper6 = mock(Function.class);
        Function<List<String>, String> mapper7 = mock(Function.class);
        Function<Map<String, String>, String> mapper8 = mock(Function.class);
        Set<String> set = Set.of("1");
        List<String> list = List.of("1");
        Map<String, String> map = Map.of("1", "2");

        when(mapper6.apply(set)).thenReturn("6");
        when(mapper7.apply(list)).thenReturn("7");
        when(mapper8.apply(map)).thenReturn("8");


        Optional<String> result1 = C2.mapNotEmpty((Collection<String>) null, mapper1);
        Optional<String> result2 = C2.mapNotEmpty((Map<String, String>) null, mapper2);
        Optional<String> result3 = C2.mapNotEmpty(Set.of(), mapper3);
        Optional<String> result4 = C2.mapNotEmpty(List.of(), mapper4);
        Optional<String> result5 = C2.mapNotEmpty(Map.of(), mapper5);

        Optional<String> result6 = C2.mapNotEmpty(set, mapper6);
        Optional<String> result7 = C2.mapNotEmpty(list, mapper7);
        Optional<String> result8 = C2.mapNotEmpty(map, mapper8);

        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
        assertTrue(result3.isEmpty());
        assertTrue(result4.isEmpty());
        assertTrue(result5.isEmpty());

        assertThat(result6.get(), is("6"));
        assertThat(result7.get(), is("7"));
        assertThat(result8.get(), is("8"));

        verify(mapper1, never()).apply(any());
        verify(mapper2, never()).apply(any());
        verify(mapper3, never()).apply(any());
        verify(mapper4, never()).apply(any());
        verify(mapper5, never()).apply(any());
        verify(mapper6).apply(set);
        verify(mapper7).apply(list);
        verify(mapper8).apply(map);
    }

    @Test
    void should_ifNotEmpty_supplier() {
        Supplier<String> supplier1 = mock(Supplier.class);
        Supplier<String> supplier2 = mock(Supplier.class);
        Supplier<String> supplier3 = mock(Supplier.class);
        Supplier<String> supplier4 = mock(Supplier.class);
        Supplier<String> supplier5 = mock(Supplier.class);
        Supplier<String> supplier6 = mock(Supplier.class);
        Supplier<String> supplier7 = mock(Supplier.class);
        Supplier<String> supplier8 = mock(Supplier.class);

        Set<String> set = Set.of("1");
        List<String> list = List.of("1");
        Map<String, String> map = Map.of("1", "2");

        when(supplier6.get()).thenReturn("6");
        when(supplier7.get()).thenReturn("7");
        when(supplier8.get()).thenReturn("8");


        Optional<String> result1 = C2.mapNotEmpty((Collection<String>) null, supplier1);
        Optional<String> result2 = C2.mapNotEmpty((Map<String, String>) null, supplier2);
        Optional<String> result3 = C2.mapNotEmpty(Set.of(), supplier3);
        Optional<String> result4 = C2.mapNotEmpty(List.of(), supplier4);
        Optional<String> result5 = C2.mapNotEmpty(Map.of(), supplier5);

        Optional<String> result6 = C2.mapNotEmpty(set, supplier6);
        Optional<String> result7 = C2.mapNotEmpty(list, supplier7);
        Optional<String> result8 = C2.mapNotEmpty(map, supplier8);

        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
        assertTrue(result3.isEmpty());
        assertTrue(result4.isEmpty());
        assertTrue(result5.isEmpty());

        assertThat(result6.get(), is("6"));
        assertThat(result7.get(), is("7"));
        assertThat(result8.get(), is("8"));

        verify(supplier1, never()).get();
        verify(supplier2, never()).get();
        verify(supplier3, never()).get();
        verify(supplier4, never()).get();
        verify(supplier5, never()).get();
        verify(supplier6).get();
        verify(supplier7).get();
        verify(supplier8).get();
    }

    @Test
    void should_map() {
        Map<String, String> result1 = C2.map(Set.of("1", "2"), Function.identity(), v -> null);
        Map<String, String> result2 = C2.map(Set.of("1", "2"), Function.identity(), v -> v + "-");
        Map<String, String> result3 = C2.map(List.of("1", "2", "1"), Function.identity(), v -> v + "-");
        assertThat(result1.size(), is(0));

        assertThat(result2.size(), is(2));
        assertThat(result2.get("1"), is("1-"));
        assertThat(result2.get("2"), is("2-"));

        assertThat(result3.size(), is(2));
        assertThat(result3.get("1"), is("1-"));
        assertThat(result3.get("2"), is("2-"));
    }

    @Test
    void should_set() {
        assertThat(C2.set((String[]) null), is(Set.of()));
        assertThat(C2.set((Set<String>) null), is(Set.of()));
        assertThat(C2.set((Stream<String>) null), is(Set.of()));

        assertThat(C2.set(new String[]{"1", null, "2"}, Function.identity(), Objects::nonNull), is(Set.of("1", "2")));
        assertThat(C2.set(Sets.newHashSet("1", "2", null), Function.identity(), Objects::nonNull), is(Set.of("1", "2")));
        assertThat(C2.set(Stream.of("1", "2", null), Function.identity(), Objects::nonNull), is(Set.of("1", "2")));
    }

    @Test
    void should_list() {
        assertThat(C2.list((String[]) null), is(List.of()));
        assertThat(C2.list((Set<String>) null), is(List.of()));
        assertThat(C2.list((Stream<String>) null), is(List.of()));

        assertThat(C2.list(new String[]{"1", null, "2"}, Function.identity(), Objects::nonNull), is(List.of("1", "2")));
        assertThat(C2.list(Sets.newHashSet("1", "2", null), Function.identity(), Objects::nonNull), is(List.of("1", "2")));
        assertThat(C2.list(Stream.of("1", "2", null), Function.identity(), Objects::nonNull), is(List.of("1", "2")));
    }
}
