package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class TreeNodes<N extends TreeNode<N>> extends ArrayList<N> implements TreeNodeAble<N> {

    @JsonCreator
    public TreeNodes() {
    }

    public TreeNodes(Collection<? extends N> c) {
        super(c);
    }

    public static <N extends TreeNode<N>> TreeNodes<N> of(N... nodes) {
        if (nodes == null || nodes.length == 0) {
            return new TreeNodes<>();
        }
        return new TreeNodes<>(Arrays.asList(nodes));
    }

    @Override
    public void forEach(BiConsumer<Location<N>, N> consumer, Location<N> parentLocation) {
        forEach(node -> consumer.accept(parentLocation, node));
    }

    @Override
    public Optional<Location<N>> find(BiPredicate<Location<N>, N> filter, Location<N> parentLocation) {
        return this.stream()
                .map(node -> node.find(filter, parentLocation))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    @Override
    public <R> List<R> all(BiFunction<Location<N>, N, R> mapper, Location<N> parentLocation) {
        return stream()
                .map(node -> node.all(mapper, parentLocation))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public <R extends TreeNode<R>> TreeNodes<R> map(BiFunction<Location<N>, N, R> mapper) {
        return map(mapper, Location.root());
    }

    public <R extends TreeNode<R>> TreeNodes<R> map(BiFunction<Location<N>, N, R> mapper, Location<N> parentLocation) {
        return new TreeNodes<>(stream()
                .map(node -> node.map(mapper, parentLocation))
                .collect(Collectors.toList()));
    }

    public Optional<N> first() {
        return stream().findFirst();
    }
}
