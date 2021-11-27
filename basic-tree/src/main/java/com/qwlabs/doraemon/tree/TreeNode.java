package com.qwlabs.doraemon.tree;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TreeNode<S> implements Iterable<TreeNode<S>> {
    @JsonUnwrapped
    private final S source;
    private final List<TreeNode<S>> children;

    public TreeNode(S source) {
        this.source = source;
        this.children = new ArrayList<>(0);
    }

    public static <S> TreeNode<S> of(S source) {
        return new TreeNode<>(source);
    }

    public S getSource() {
        return source;
    }

    public List<TreeNode<S>> getChildren() {
        return children;
    }

    public int size() {
        return children.size();
    }

    public TreeNode<S> get(int index) {
        return children.get(index);
    }

    @Override
    public void forEach(Consumer<? super TreeNode<S>> action) {
        children.forEach(action);
    }

    @Override
    public Spliterator<TreeNode<S>> spliterator() {
        return children.spliterator();
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

    public void acceptDeep(BiConsumer<S, S> consumer) {
        children.forEach(child -> {
            consumer.accept(source, child.getSource());
            child.acceptDeep(consumer);
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

    public <R> List<R> mapDeep(Function<S, R> mapper) {
        return mapDeep(mapper, false);
    }

    public <R> List<R> mapDeep(Function<S, R> mapper, boolean withSelf) {
        List<R> result = Lists.newArrayList();
        if (withSelf) {
            result.add(mapper.apply(source));
        }
        children.forEach(child -> {
            result.addAll(child.mapDeep(mapper, true));
        });
        return result;
    }
}

