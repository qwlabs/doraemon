package com.qwlabs.tree;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public interface TreeNodeAble<N> {
    default void forEach(@NotNull BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer) {
        forEach(consumer, Location.root(), false);
    }

    default void forEach(@NotNull BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer, boolean parallel) {
        forEach(consumer, Location.root(), parallel);
    }

    default void forEach(@NotNull BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer,
                 @NotNull Location<TreeNode<N>> parentLocation){
        forEach(consumer, parentLocation, false);
    }

    void forEach(@NotNull BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer,
                 @NotNull Location<TreeNode<N>> parentLocation, boolean parallel);

    default Optional<Location<TreeNode<N>>> find(@NotNull BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter) {
        return find(filter, Location.root());
    }

    default  <E> Optional<TreeNode<N>> find(@NotNull List<E> path,
                                   @NotNull BiPredicate<TreeNode<N>, E> filter){
        return find(path, filter, false);
    }

    <E> Optional<TreeNode<N>> find(@NotNull List<E> path,
                                   @NotNull BiPredicate<TreeNode<N>, E> filter,
                                   boolean parallel);

    default Optional<Location<TreeNode<N>>> find(@NotNull BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter,
                                         @NotNull Location<TreeNode<N>> parentLocation){
        return find(filter, parentLocation, false);
    }

    Optional<Location<TreeNode<N>>> find(@NotNull BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter,
                                         @NotNull Location<TreeNode<N>> parentLocation, boolean parallel);

    default <R> Stream<R> all(@NotNull BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper) {
        return all(mapper, Location.root());
    }

    <R> Stream<R> all(@NotNull BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper,
                    @NotNull Location<TreeNode<N>> parentLocation);
}
