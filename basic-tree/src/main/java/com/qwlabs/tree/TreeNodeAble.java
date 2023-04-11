package com.qwlabs.tree;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public interface TreeNodeAble<N> {
    default void forEach(@NotNull BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer) {
        forEach(consumer, Location.root());
    }

    void forEach(@NotNull BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer,
                 @NotNull Location<TreeNode<N>> parentLocation);

    <E> Optional<TreeNode<N>> find(@NotNull List<E> path,
                                   @NotNull BiPredicate<TreeNode<N>, E> filter);

    default Optional<Location<TreeNode<N>>> find(@NotNull BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter) {
        return find(filter, Location.root());
    }

    Optional<Location<TreeNode<N>>> find(@NotNull BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter,
                                         @NotNull Location<TreeNode<N>> parentLocation);

    default <R> List<R> all(@NotNull BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper) {
        return all(mapper, Location.root());
    }

    <R> List<R> all(@NotNull BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper,
                    @NotNull Location<TreeNode<N>> parentLocation);
}
