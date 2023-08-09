package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.qwlabs.lang.Streams2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeNodes<N> extends ArrayList<TreeNode<N>> implements TreeNodeAble<N> {

    private static final TreeNodes EMPTY = new TreeNodes(0);

    @JsonCreator
    public TreeNodes() {
    }

    public TreeNodes(int initialCapacity) {
        super(initialCapacity);
    }

    public TreeNodes(Collection<? extends TreeNode<N>> c) {
        super(c);
    }

    @Override
    public void forEach(BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer,
                        Location<TreeNode<N>> parentLocation,
                        boolean parallel) {
        Streams2.parallel(this.stream(), parallel).forEach(node -> node.forEach(consumer, parentLocation));
    }

    @Override
    public <E> Optional<TreeNode<N>> find(List<E> path,
                                          BiPredicate<TreeNode<N>, E> filter,
                                          boolean parallel) {
        if (path.isEmpty()) {
            return Optional.empty();
        }

        return Streams2.parallel(this.stream(), parallel)
            .map(node -> node.find(path, filter))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
    }

    @Override
    public Optional<Location<TreeNode<N>>> find(BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter,
                                                Location<TreeNode<N>> parentLocation,
                                                boolean parallel) {
        return Streams2.parallel(this.stream(), parallel)
            .map(node -> node.find(filter, parentLocation))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
    }

    @Override
    public <R> Stream<R> all(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper, Location<TreeNode<N>> parentLocation) {
        return stream().flatMap(node -> node.all(mapper, parentLocation));
    }

    public <R> TreeNodes<R> map(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper) {
        return map(mapper, Location.root(), false);
    }

    public <R> TreeNodes<R> mapParallel(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper) {
        return map(mapper, Location.root(), true);
    }

    public <R> TreeNodes<R> map(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper,
                                boolean parallel) {
        return map(mapper, Location.root(), parallel);
    }

    public <R> TreeNodes<R> map(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper,
                                Location<TreeNode<N>> parentLocation) {
        return map(mapper, parentLocation, false);
    }

    public <R> TreeNodes<R> mapParallel(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper,
                                Location<TreeNode<N>> parentLocation) {
        return map(mapper, parentLocation, true);
    }

    public <R> TreeNodes<R> map(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper,
                                Location<TreeNode<N>> parentLocation,
                                boolean parallel) {
        return new TreeNodes<>(Streams2.parallel(this.stream(), parallel)
            .map(node -> node.map(mapper, parentLocation))
            .collect(Collectors.toList()));
    }

    public <R> List<R> map(Function<N, R> mapper) {
        return map(mapper, false);
    }

    public <R> List<R> mapParallel(Function<N, R> mapper) {
        return map(mapper, true);
    }

    public <R> List<R> map(Function<N, R> mapper, boolean parallel) {
        return Streams2.parallel(this.stream(), parallel).map(node -> node.map(mapper)).collect(Collectors.toList());
    }

    public <R> List<R> map(TreeNodeFunction<N, R> mapper) {
        return map(mapper, Location.root(), false);
    }

    public <R> List<R> mapParallel(TreeNodeFunction<N, R> mapper) {
        return map(mapper, Location.root(), true);
    }

    public <R> List<R> map(TreeNodeFunction<N, R> mapper, boolean parallel) {
        return map(mapper, Location.root(), parallel);
    }

    public <R> List<R> map(TreeNodeFunction<N, R> mapper,
                           Location<TreeNode<N>> parentLocation) {
        return map(mapper, parentLocation, false);
    }

    public <R> List<R> mapParallel(TreeNodeFunction<N, R> mapper,
                           Location<TreeNode<N>> parentLocation) {
        return map(mapper, parentLocation, true);
    }

    public <R> List<R> map(TreeNodeFunction<N, R> mapper,
                           Location<TreeNode<N>> parentLocation,
                           boolean parallel) {
        return Streams2.parallel(this.stream(), parallel)
            .map(node -> node.map(mapper, parentLocation, parallel)).collect(Collectors.toList());
    }


    public Optional<TreeNode<N>> first() {
        return stream().findFirst();
    }

    public boolean isSingle() {
        return size() == 1;
    }

    public boolean isMultiple() {
        return size() > 1;
    }

    public static <N> TreeNodes<N> of(TreeNode<N>... nodes) {
        if (nodes == null || nodes.length == 0) {
            return new TreeNodes<>();
        }
        return new TreeNodes<>(Arrays.asList(nodes));
    }

    public static <N> TreeNodes<N> empty() {
        return (TreeNodes<N>) EMPTY;
    }
}
