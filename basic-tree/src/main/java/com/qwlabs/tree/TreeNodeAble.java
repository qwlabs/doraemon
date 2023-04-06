package com.qwlabs.tree;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public interface TreeNodeAble<N> {
    default void forEach(BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer) {
        forEach(consumer, Location.root());
    }

    void forEach(BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer, Location<TreeNode<N>> parentLocation);

    default Optional<Location<TreeNode<N>>> find(BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter) {
        return find(filter, Location.root());
    }

    Optional<Location<TreeNode<N>>> find(BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter, Location<TreeNode<N>> parentLocation);

    default <R> List<R> all(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper) {
        return all(mapper, Location.root());
    }

    <R> List<R> all(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper, Location<TreeNode<N>> parentLocation);
}
