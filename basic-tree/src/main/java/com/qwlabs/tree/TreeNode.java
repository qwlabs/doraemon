package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
@Setter
public class TreeNode<S> implements Iterable<TreeNode<S>> {
    @JsonUnwrapped
    @NotNull
    private S source;
    private List<TreeNode<S>> children;

    @JsonCreator
    public TreeNode() {
        this(null, new ArrayList<>(0));
    }

    public TreeNode(S source, List<TreeNode<S>> children) {
        this.source = source;
        this.children = children;
    }

    public int size() {
        return children.size();
    }

    public TreeNode<S> get(int index) {
        return children.get(index);
    }

    @NotNull
    @Override
    public Iterator<TreeNode<S>> iterator() {
        return children.iterator();
    }

    public Stream<TreeNode<S>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public TreeNode<S> add(@Nullable TreeNode<S> child) {
        Optional.ofNullable(child)
                .filter(n -> Objects.nonNull(n.getSource()))
                .ifPresent(children::add);
        return this;
    }

    public TreeNode<S> addAll(@Nullable Iterable<@NotNull TreeNode<S>> children) {
        if (children == null) {
            return this;
        }
        children.forEach(child -> {
            if (Objects.nonNull(child.getSource())) {
                this.children.add(child);
            }
        });
        return this;
    }

    public void accept(BiConsumer<Location<S>, TreeNode<S>> consumer) {
        accept(consumer, Location.root());
    }

    private void accept(BiConsumer<Location<S>, TreeNode<S>> consumer,
                        Location<S> parentLocation) {
        consumer.accept(parentLocation, this);
        var location = parentLocation.child(source);
        children.forEach(child -> child.accept(consumer, location));
    }

    public void acceptSource(BiConsumer<Location<S>, S> consumer) {
        acceptSource(consumer, Location.root());
    }

    private void acceptSource(BiConsumer<Location<S>, S> consumer,
                              Location<S> parentLocation) {
        consumer.accept(parentLocation, source);
        var location = parentLocation.child(source);
        children.forEach(child -> child.acceptSource(consumer, location));
    }

    @NotNull
    public LocationWithNode<S> find(Predicate<S> filter) {
        return find(filter, Location.root());
    }

    @NotNull
    private LocationWithNode<S> find(Predicate<S> filter,
                                     Location<S> parentLocation) {
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

    private <R> TreeNode<R> map(BiFunction<Location<S>, S, R> mapper,
                                Location<S> parentLocation) {
        var newNode = TreeNode.of(mapper.apply(parentLocation, source));
        var location = parentLocation.child(source);
        children.forEach(child -> newNode.add(child.map(mapper, location)));
        return newNode;
    }

    public <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper) {
        return mapSource(mapper, Location.root(), Objects::nonNull);
    }


    public <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper,
                                 Predicate<R> filter) {
        return mapSource(mapper, Location.root(), filter);
    }

    private <R> List<R> mapSource(BiFunction<Location<S>, S, R> mapper,
                                  Location<S> parentLocation,
                                  Predicate<R> filter) {
        List<R> result = Lists.newArrayList();
        var r = mapper.apply(parentLocation, source);
        if (filter.test(r)) {
            result.add(r);
        }
        var location = parentLocation.child(source);
        children.forEach(child -> result.addAll(child.mapSource(mapper, location, filter)));
        return result;
    }

    public static <S> TreeNode<S> of(S source) {
        return of(source, new ArrayList<>(0));
    }

    public static <S> TreeNode<S> of(S source, TreeNode<S>... children) {
        return of(source, Lists.newArrayList(children));
    }

    public static <S> TreeNode<S> of(S source, List<TreeNode<S>> children) {
        return new TreeNode<>(source, children);
    }

}

