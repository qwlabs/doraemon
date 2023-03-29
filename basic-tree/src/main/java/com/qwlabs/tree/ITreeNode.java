package com.qwlabs.tree;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface ITreeNode<S> {
    void accept(BiConsumer<Location<S>, TreeNode<S>> consumer);

    void acceptSource(BiConsumer<Location<S>, S> consumer);

    LocationWithNode<S> find(Predicate<S> filter);

    <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper);

    <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper, Predicate<R> filter);

    void forEach(BiConsumer<Location<S>, TreeNode<S>> consumer);
}
