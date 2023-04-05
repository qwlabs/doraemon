package com.qwlabs.tree;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public interface TreeNode<N extends TreeNode<N>> extends TreeNodeAble<N> {

    default N unwarp() {
        return (N) this;
    }

    N newInstance();

    TreeNodes<N> getChildren();

    void setChildren(TreeNodes<N> children);

    @Override
    default void forEach(BiConsumer<Location<N>, N> consumer, Location<N> parentLocation) {
        var self = unwarp();
        consumer.accept(parentLocation, self);
        var location = parentLocation.child(self);
        getChildren().forEach(consumer, location);
    }

    @Override
    default Optional<Location<N>> find(BiPredicate<Location<N>, N> filter, Location<N> parentLocation) {
        var self = unwarp();
        var location = parentLocation.child(self);
        if (filter.test(parentLocation, self)) {
            return Optional.of(location);
        }
        return getChildren().find(filter, location);
    }

    @Override
    default <R> List<R> all(BiFunction<Location<N>, N, R> mapper, Location<N> parentLocation) {
        var self = unwarp();
        var location = parentLocation.child(self);
        List<R> results = Lists.newArrayList(getChildren().all(mapper, location));
        results.add(0, mapper.apply(parentLocation, self));
        return results;
    }

    default <R extends TreeNode<R>> R map(BiFunction<Location<N>, N, R> mapper) {
        return map(mapper, Location.root());
    }

    default <R extends TreeNode<R>> R map(BiFunction<Location<N>, N, R> mapper, Location<N> parentLocation) {
        var self = unwarp();
        var newNode = mapper.apply(parentLocation, self);
        var r = newNode.newInstance();
        r.setChildren(getChildren().map(mapper, parentLocation));
        return r;
    }

    default N addChildren(N... addChildren) {
        TreeNodes<N> children = Optional.ofNullable(getChildren())
                .orElseGet(TreeNodes::of);
        children.addAll(List.of(addChildren));
        setChildren(children);
        return unwarp();
    }
}
