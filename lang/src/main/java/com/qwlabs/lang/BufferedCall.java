package com.qwlabs.lang;

import com.google.common.collect.Iterators;
import com.google.common.collect.Streams;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Stream;


public class BufferedCall<E, R> {
    private static final int DEFAULT_BUFFER_SIZE = 1000;
    private final int bufferSize;
    private Stream<E> buffer;
    private final ReentrantLock lock;

    private BufferedCall(int bufferSize) {
        this.bufferSize = bufferSize;
        this.lock = new ReentrantLock();
        this.rest();
    }

    public BufferedCall<E, R> add(E element) {
        return F2.ifPresent(element, () -> add(Stream.of(element)));
    }

    public BufferedCall<E, R> add(E... elements) {
        return F2.ifPresent(elements, () -> add(Stream.of(elements)));
    }

    public BufferedCall<E, R> add(List<E> elements) {
        return F2.ifPresent(elements, () -> add(elements.stream()));
    }

    public BufferedCall<E, R> add(Set<E> elements) {
        return F2.ifPresent(elements, () -> add(elements.stream()));
    }

    public BufferedCall<E, R> add(Collection<E> elements) {
        return F2.ifPresent(elements, () -> add(elements.stream()));
    }

    public synchronized BufferedCall<E, R> add(Stream<E> elements) {
        if (Objects.isNull(elements)) {
            return this;
        }
        this.lock.lock();
        try {
            this.buffer = Stream.concat(this.buffer, elements);
        } finally {
            this.lock.unlock();
        }
        return this;
    }

    public Stream<R> call(Function<List<E>, R> function) {
        return call(function, false);
    }

    public Stream<R> callParallel(Function<List<E>, R> function) {
        return call(function, true);
    }

    public Stream<R> call(Function<List<E>, R> function, boolean parallel) {
        this.lock.lock();
        try {
            var partition = Streams.stream(Iterators.partition(this.buffer.iterator(), bufferSize));
            var result = Streams2.parallel(partition, parallel).map(function::apply);
            this.rest();
            return result;
        } finally {
            this.lock.unlock();
        }
    }

    private void rest() {
        this.buffer = Stream.empty();
    }

    public static <E, R> BufferedCall<E, R> of() {
        return of(DEFAULT_BUFFER_SIZE);
    }

    public static <E, R> BufferedCall<E, R> of(int size) {
        return new BufferedCall<>(size);
    }
}
