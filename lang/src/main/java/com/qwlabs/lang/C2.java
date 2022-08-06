package com.qwlabs.lang;

import com.google.common.collect.Maps;
import com.google.common.collect.Streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class C2 {
    private C2() {
    }

    public static <E> boolean isEmpty(Collection<E> c) {
        return c == null || c.isEmpty();
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <E> boolean isEmpty(E[] array) {
        return array == null || array.length <= 0;
    }

    public static <E> boolean isNotEmpty(Collection<E> c) {
        return !isEmpty(c);
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    public static <E> boolean isNotEmpty(E[] array) {
        return !isEmpty(array);
    }

    public static <E> void ifEmpty(Collection<E> c, EmptyConsumer consumer) {
        if (isEmpty(c)) {
            consumer.accept();
        }
    }

    public static <K, V> void ifEmpty(Map<K, V> map, EmptyConsumer consumer) {
        if (isEmpty(map)) {
            consumer.accept();
        }
    }

    public static <E, C extends Collection<E>, R> Optional<R> ifEmpty(C c, Supplier<R> supplier) {
        if (isEmpty(c)) {
            return Optional.ofNullable(supplier.get());
        }
        return Optional.empty();
    }

    public static <K, V, M extends Map<K, V>, R> Optional<R> ifEmpty(M map, Supplier<R> supplier) {
        if (isEmpty(map)) {
            return Optional.ofNullable(supplier.get());
        }
        return Optional.empty();
    }

    public static <E> void ifNotEmpty(Collection<E> c, EmptyConsumer consumer) {
        if (isNotEmpty(c)) {
            consumer.accept();
        }
    }

    public static <K, V> void ifNotEmpty(Map<K, V> map, EmptyConsumer consumer) {
        if (isNotEmpty(map)) {
            consumer.accept();
        }
    }

    public static <E, C extends Collection<E>> void ifNotEmpty(C c, Consumer<C> consumer) {
        if (isNotEmpty(c)) {
            consumer.accept(c);
        }
    }

    public static <K, V, M extends Map<K, V>> void ifNotEmpty(M map, Consumer<M> consumer) {
        if (isNotEmpty(map)) {
            consumer.accept(map);
        }
    }

    public static <E, C extends Collection<E>, R> Optional<R> mapNotEmpty(C c, Supplier<R> supplier) {
        if (isNotEmpty(c)) {
            return Optional.ofNullable(supplier.get());
        }
        return Optional.empty();
    }

    public static <K, V, M extends Map<K, V>, R> Optional<R> mapNotEmpty(M map, Supplier<R> supplier) {
        if (isNotEmpty(map)) {
            return Optional.ofNullable(supplier.get());
        }
        return Optional.empty();
    }

    public static <E, C extends Collection<E>, R> Optional<R> mapNotEmpty(C c, Function<C, R> mapper) {
        if (isNotEmpty(c)) {
            return Optional.ofNullable(mapper.apply(c));
        }
        return Optional.empty();
    }

    public static <K, V, M extends Map<K, V>, R> Optional<R> mapNotEmpty(M map, Function<M, R> mapper) {
        if (isNotEmpty(map)) {
            return Optional.ofNullable(mapper.apply(map));
        }
        return Optional.empty();
    }

    public static <E> Stream<E> stream(E[] input) {
        if (input == null) {
            return Stream.empty();
        }
        return Arrays.stream(input);
    }

    public static <E> Stream<E> stream(Iterable<E> input) {
        if (input == null) {
            return Stream.empty();
        }
        return Streams.stream(input);
    }

    public static <E> Set<E> set(E[] input) {
        return set(input, Function.identity());
    }

    public static <E, R> Set<R> set(E[] input, Function<E, R> mapper) {
        return set(input, mapper, v -> true);
    }

    public static <E> Set<E> set(E[] input, Predicate<E> filter) {
        return set(input, Function.identity(), filter);
    }

    public static <E, R> Set<R> set(E[] input,
                                    Function<E, R> mapper,
                                    Predicate<R> filter) {
        Objects.requireNonNull(mapper, "Mapper can not be null.");
        Objects.requireNonNull(filter, "Filter can not be null.");
        if (input == null) {
            return Collections.emptySet();
        }
        return doSet(stream(input), mapper, filter);
    }

    public static <E> Set<E> set(Iterable<E> input) {
        return set(input, Function.identity());
    }

    public static <E, R> Set<R> set(Iterable<E> input, Function<E, R> mapper) {
        return set(input, mapper, v -> true);
    }

    public static <E> Set<E> set(Iterable<E> input, Predicate<E> filter) {
        return set(input, Function.identity(), filter);
    }

    public static <E, R> Set<R> set(Iterable<E> input,
                                    Function<E, R> mapper,
                                    Predicate<R> filter) {
        Objects.requireNonNull(mapper, "Mapper can not be null.");
        Objects.requireNonNull(filter, "Filter can not be null.");
        if (input == null) {
            return Collections.emptySet();
        }
        return doSet(stream(input), mapper, filter);
    }

    public static <E> Set<E> set(Stream<E> input) {
        return set(input, Function.identity(), v -> true);
    }

    public static <E, R> Set<R> set(Stream<E> input, Function<E, R> mapper) {
        return set(input, mapper, v -> true);
    }

    public static <E> Set<E> set(Stream<E> input, Predicate<E> filter) {
        return set(input, Function.identity(), filter);
    }

    public static <E, R> Set<R> set(Stream<E> input,
                                    Function<E, R> mapper,
                                    Predicate<R> filter) {
        Objects.requireNonNull(mapper, "Mapper can not be null.");
        Objects.requireNonNull(filter, "Filter can not be null.");
        if (input == null) {
            return Collections.emptySet();
        }
        return doSet(input, mapper, filter);
    }

    private static <E, R> Set<R> doSet(Stream<E> input,
                                       Function<E, R> mapper,
                                       Predicate<R> predicate) {
        return input.map(mapper)
                .filter(predicate)
                .collect(Collectors.toSet());
    }

    public static <E> List<E> list(Stream<E> input) {
        return list(input, Function.identity());
    }

    public static <E> List<E> list(E[] input) {
        return list(input, Function.identity());
    }

    public static <E, R> List<R> list(E[] input, Function<E, R> mapper) {
        return list(input, mapper, v -> true);
    }

    public static <E> List<E> list(E[] input, Predicate<E> filter) {
        return list(input, Function.identity(), filter);
    }

    public static <E, R> List<R> list(E[] input,
                                      Function<E, R> mapper,
                                      Predicate<R> filter) {
        Objects.requireNonNull(mapper, "Mapper can not be null.");
        Objects.requireNonNull(filter, "Filter can not be null.");
        if (input == null) {
            return Collections.emptyList();
        }
        return doList(Arrays.stream(input), mapper, filter);
    }

    public static <E> List<E> list(Iterable<E> input) {
        return list(input, Function.identity());
    }

    public static <E, R> List<R> list(Iterable<E> input, Function<E, R> mapper) {
        return list(input, mapper, v -> true);
    }

    public static <E> List<E> list(Iterable<E> input, Predicate<E> filter) {
        return list(input, Function.identity(), filter);
    }

    public static <E, R> List<R> list(Iterable<E> input,
                                      Function<E, R> mapper,
                                      Predicate<R> filter) {
        Objects.requireNonNull(mapper, "Mapper can not be null.");
        Objects.requireNonNull(filter, "Filter can not be null.");
        if (input == null) {
            return Collections.emptyList();
        }
        return doList(Streams.stream(input), mapper, filter);
    }

    public static <E, R> List<R> list(Stream<E> input, Function<E, R> mapper) {
        return list(input, mapper, v -> true);
    }

    public static <E> List<E> list(Stream<E> input, Predicate<E> filter) {
        return list(input, Function.identity(), filter);
    }

    public static <E, R> List<R> list(Stream<E> input,
                                      Function<E, R> mapper,
                                      Predicate<R> filter) {
        Objects.requireNonNull(mapper, "Mapper can not be null.");
        Objects.requireNonNull(filter, "Filter can not be null.");
        if (input == null) {
            return Collections.emptyList();
        }
        return doList(input, mapper, filter);
    }

    private static <E, R> List<R> doList(Stream<E> input,
                                         Function<E, R> mapper,
                                         Predicate<R> predicate) {
        return input.map(mapper)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static <E, K, V> Map<K, V> map(Iterable<E> input,
                                          Function<? super E, ? extends K> keyMapper,
                                          Supplier<V> valueSupplier,
                                          BiConsumer<? super V, ? super E> mergeConsumer) {
        Objects.requireNonNull(keyMapper, "key mapper can not be null.");
        Objects.requireNonNull(valueSupplier, "value supplier can not be null.");
        Objects.requireNonNull(mergeConsumer, "merge consumer can not be null.");
        if (Objects.isNull(input)) {
            return Collections.emptyMap();
        }
        Map<K, V> map = Maps.newHashMap();
        Iterator<E> iterator = input.iterator();
        while (iterator.hasNext()) {
            E element = iterator.next();
            K key = keyMapper.apply(element);
            V value = map.computeIfAbsent(key, k -> valueSupplier.get());
            mergeConsumer.accept(value, element);
        }
        return map;
    }

    public static <E, K, V> Map<K, V> map(Stream<E> input,
                                          Function<? super E, ? extends K> keyMapper,
                                          Function<? super E, ? extends V> valueMapper) {
        return map(input, keyMapper, valueMapper, (v1, v2) -> v1);
    }

    public static <E, K, V> Map<K, V> map(Iterable<E> input,
                                          Function<? super E, ? extends K> keyMapper,
                                          Function<? super E, ? extends V> valueMapper) {
        return map(input, keyMapper, valueMapper, (v1, v2) -> v1);
    }

    public static <E, K, V> Map<K, V> map(Iterable<E> input,
                                          Function<? super E, ? extends K> keyMapper,
                                          Function<? super E, ? extends V> valueMapper,
                                          BinaryOperator<V> mergeFunction) {
        return F2.ifPresent(input, () -> map(Streams.stream(input), keyMapper, valueMapper, mergeFunction));
    }

    public static <E, K, V> Map<K, V> map(Stream<E> input,
                                          Function<? super E, ? extends K> keyMapper,
                                          Function<? super E, ? extends V> valueMapper,
                                          BinaryOperator<V> mergeFunction) {
        Objects.requireNonNull(keyMapper, "key mapper can not be null.");
        Objects.requireNonNull(valueMapper, "value mapper can not be null.");
        Objects.requireNonNull(mergeFunction, "merge function can not be null.");
        if (input == null) {
            return Map.of();
        }
        Map<K, V> map = new HashMap<>();
        input.forEach(element -> {
            K key = keyMapper.apply(element);
            if (key == null) {
                return;
            }
            V value = valueMapper.apply(element);
            if (value == null) {
                return;
            }
            map.merge(key, value, mergeFunction);
        });
        return map;
    }

    public static <E, K, V> Map<K, List<V>> listMap(Iterable<E> input,
                                                    Function<? super E, ? extends K> keyMapper,
                                                    Function<? super E, ? extends V> valueMapper) {
        return F2.ifPresent(input,
                () -> listMap(Streams.stream(input), keyMapper, valueMapper),
                Collections.emptyMap());
    }

    public static <E, K, V> Map<K, List<V>> listMap(Stream<E> input,
                                                    Function<? super E, ? extends K> keyMapper,
                                                    Function<? super E, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "key mapper can not be null.");
        Objects.requireNonNull(valueMapper, "value mapper can not be null.");
        return F2.ifPresent(input,
                () -> input.collect(Collectors.groupingBy(keyMapper,
                        Collectors.mapping(valueMapper, Collectors.toList()))),
                Collections.emptyMap());
    }

    public static <E, K, V> Map<K, Set<V>> setMap(Iterable<E> input,
                                                  Function<? super E, ? extends K> keyMapper,
                                                  Function<? super E, ? extends V> valueMapper) {
        return F2.ifPresent(input,
                () -> setMap(Streams.stream(input), keyMapper, valueMapper),
                Collections.emptyMap());
    }

    public static <E, K, V> Map<K, Set<V>> setMap(Stream<E> input,
                                                  Function<? super E, ? extends K> keyMapper,
                                                  Function<? super E, ? extends V> valueMapper) {
        Objects.requireNonNull(keyMapper, "key mapper can not be null.");
        Objects.requireNonNull(valueMapper, "value mapper can not be null.");
        return F2.ifPresent(input,
                () -> input.collect(Collectors.groupingBy(keyMapper,
                        Collectors.mapping(valueMapper, Collectors.toSet()))),
                Collections.emptyMap());
    }

}
