package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

@Getter
@Setter
public class TreeNode<S> implements ITreeNode<S>, Serializable {
    @JsonUnwrapped
    @NotNull
    private S source;
    private TreeNodes<S> children;

    @JsonCreator
    public TreeNode() {
        this(null, TreeNodes.empty());
    }

    public TreeNode(S source, TreeNodes<S> children) {
        this.source = source;
        this.children = children;
    }

    @Override
    public void accept(BiConsumer<Location<S>, TreeNode<S>> consumer) {
        accept(consumer, Location.root());
    }

    protected void accept(BiConsumer<Location<S>, TreeNode<S>> consumer, Location<S> parentLocation) {
        consumer.accept(parentLocation, this);
        var location = parentLocation.child(source);
        children.forEach(child -> child.accept(consumer, location));
    }

    @Override
    public void acceptSource(BiConsumer<Location<S>, S> consumer) {
        acceptSource(consumer, Location.root());
    }

    protected void acceptSource(BiConsumer<Location<S>, S> consumer, Location<S> parentLocation) {
        consumer.accept(parentLocation, source);
        var location = parentLocation.child(source);
        children.forEach(child -> child.acceptSource(consumer, location));
    }

    @NotNull
    @Override
    public LocationWithNode<S> find(Predicate<S> filter) {
        return find(filter, Location.root());
    }

    @NotNull
    protected LocationWithNode<S> find(Predicate<S> filter, Location<S> parentLocation) {
        if (source != null && filter.test(source)) {
            return LocationWithNode.of(parentLocation, this);
        }
        var location = parentLocation.child(source);
        for (TreeNode<S> child : children) {
            LocationWithNode<S> node = child.find(filter, location);
            if (node.isFound()) {
                return node;
            }
        }
        return LocationWithNode.notFound();
    }

    public <R> TreeNode<R> map(BiFunction<Location<S>, S, R> mapper) {
        return map(mapper, Location.root());
    }

    protected <R> TreeNode<R> map(BiFunction<Location<S>, S, R> mapper, Location<S> parentLocation) {
        var location = parentLocation.child(source);
        var newSource = mapper.apply(parentLocation, source);
        return TreeNode.of(newSource, children.map(mapper, location));
    }

    @NotNull
    @Override
    public <R> List<@NotNull R> mapSource(BiFunction<Location<S>, S, R> mapper) {
        return mapSource(mapper, Location.root(), Objects::nonNull);
    }

    @NotNull
    @Override
    public <R> List<@NotNull R> mapSource(BiFunction<Location<S>, S, R> mapper, Predicate<R> filter) {
        return mapSource(mapper, Location.root(), filter);
    }

    protected <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper, Location<S> parentLocation, Predicate<R> filter) {
        List<R> result = Lists.newArrayList();
        var r = mapper.apply(parentLocation, source);
        if (filter.test(r)) {
            result.add(r);
        }
        var location = parentLocation.child(source);
        result.addAll(children.mapSource(mapper, location, filter));
        return result;
    }

    @Override
    public void forEach(BiConsumer<Location<S>, TreeNode<S>> consumer) {
        forEach(consumer, Location.root());
    }

    protected void forEach(BiConsumer<Location<S>, TreeNode<S>> consumer, Location<S> parentLocation) {
        consumer.accept(Location.root(), this);
        var location = parentLocation.child(source);
        children.forEach(consumer, location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(source, treeNode.source) && Objects.equals(children, treeNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, children);
    }

    public static <S> TreeNode<S> of(S source) {
        return of(source, TreeNodes.empty());
    }

    public static <S> TreeNode<S> of(S source, TreeNode<S>... children) {
        return of(source, TreeNodes.of(children));
    }

    public static <S> TreeNode<S> of(S source, List<TreeNode<S>> children) {
        return new TreeNode<>(source, TreeNodes.of(children));
    }

}

