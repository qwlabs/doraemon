package com.qwlabs.tree;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public interface TreeNodeAble<N extends TreeNode<N>> extends Serializable {
    default void forEach(BiConsumer<Location<N>, N> consumer) {
        forEach(consumer, Location.root());
    }

    void forEach(BiConsumer<Location<N>, N> consumer, Location<N> parentLocation);

    default Optional<Location<N>> find(BiPredicate<Location<N>, N> filter) {
        return find(filter, Location.root());
    }

    Optional<Location<N>> find(BiPredicate<Location<N>, N> filter, Location<N> parentLocation);

    default <R> List<R> all(BiFunction<Location<N>, N, R> mapper) {
        return all(mapper, Location.root());
    }

    <R> List<R> all(BiFunction<Location<N>, N, R> mapper, Location<N> parentLocation);
}
