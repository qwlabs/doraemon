package com.qwlabs.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TreeNodes<S> extends ArrayList<TreeNode<S>> implements ITreeNode<S> {
    public TreeNodes(int initialCapacity) {
        super(initialCapacity);
    }

    public TreeNodes() {
    }

    public TreeNodes(Collection<? extends TreeNode<S>> c) {
        super(c);
    }

    public Optional<TreeNode<S>> toNode() {
        if (isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(get(0));
    }


    public static <S> TreeNodes<S> empty() {
        return new TreeNodes<>(0);
    }

    public static <S> TreeNodes<S> of(TreeNode<S>... nodes) {
        return of(Arrays.asList(nodes));
    }

    public static <S> TreeNodes<S> of(Collection<? extends TreeNode<S>> c) {
        return new TreeNodes<>(c);
    }

    @Override
    public void accept(BiConsumer<Location<S>, TreeNode<S>> consumer) {
        Location<S> location = Location.root();
        forEach(node -> node.accept(consumer, location));
    }

    @Override
    public void acceptSource(BiConsumer<Location<S>, S> consumer) {
        Location<S> location = Location.root();
        forEach(node -> node.acceptSource(consumer, location));
    }

    @Override
    public LocationWithNode<S> find(Predicate<S> filter) {
        Location<S> location = Location.root();
        return stream()
                .map(node -> node.find(filter, location))
                .filter(LocationWithNode::isFound)
                .findAny()
                .orElseGet(LocationWithNode::notFound);
    }

    public <R> TreeNodes<R> map(BiFunction<Location<S>, S, R> mapper) {
        return map(mapper, Location.root());
    }

    protected <R> TreeNodes<R> map(BiFunction<Location<S>, S, R> mapper,
                                   Location<S> parentLocation) {
        return TreeNodes.of(stream()
                .map(node -> node.map(mapper, parentLocation))
                .collect(Collectors.toList()));
    }

    @Override
    public <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper) {
        return mapSource(mapper, Location.root(), Objects::nonNull);
    }

    @Override
    public <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper, Predicate<R> filter) {
        return mapSource(mapper, Location.root(), filter);
    }

    protected <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper,
                                    Location<S> parentLocation,
                                    Predicate<R> filter) {
        return stream()
                .map(node -> node.mapSource(mapper, parentLocation, filter))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
