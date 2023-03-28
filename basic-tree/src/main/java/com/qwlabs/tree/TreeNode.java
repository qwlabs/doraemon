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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
@Setter
public class TreeNode<S> implements Iterable<TreeNode<S>> {
    @JsonUnwrapped
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

    public static <S> TreeNode<S> of(S source) {
        return of(source, new ArrayList<>(0));
    }

    public static <S> TreeNode<S> of(S source, TreeNode<S>... children) {
        return of(source, Lists.newArrayList(children));
    }

    public static <S> TreeNode<S> of(S source, List<TreeNode<S>> children) {
        return new TreeNode<>(source, children);
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

    public void accept(BiConsumer<S, S> consumer) {
        children.forEach(child -> {
            consumer.accept(source, child.getSource());
            child.accept(consumer);
        });
    }

    public Optional<TreeNode<S>> findNode(Predicate<S> filter) {
        if (source != null && filter.test(source)) {
            return Optional.of(this);
        }
        for (TreeNode<S> child : children) {
            Optional<TreeNode<S>> mayNode = child.findNode(filter);
            if (mayNode.isPresent()) {
                return mayNode;
            }
        }
        return Optional.empty();
    }

    public <R> TreeNode<R> map(Function<S, R> mapper) {
        var newNode = TreeNode.of(mapper.apply(source));
        children.forEach(child -> newNode.add(child.map(mapper)));
        return newNode;
    }

    public <R> List<R> mapSource(Function<S, R> mapper) {
        return mapSource(mapper, false);
    }

    public <R> List<R> mapSource(Function<S, R> mapper, boolean withSelf) {
        List<R> result = Lists.newArrayList();
        if (withSelf) {
            result.add(mapper.apply(source));
        }
        children.forEach(child -> {
            result.addAll(child.mapSource(mapper, true));
        });
        return result;
    }
}

