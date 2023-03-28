package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("HiddenField")
public final class Tree<S> extends TreeNode<S> {
    private static final Tree EMPTY_TREE = new Tree<>(null);
    private final Function<S, Object> identityFunction;
    private final Supplier<Map<Object, S>> parentMapping = Suppliers.memoize(this::loadParentMapping);

    public Tree(Function<S, Object> identityFunction) {
        this.identityFunction = identityFunction;
    }

    @Override
    @JsonIgnore
    public S getSource() {
        return super.getSource();
    }

    @Override
    @JsonValue
    public List<TreeNode<S>> getChildren() {
        return super.getChildren();
    }

    public <I> List<S> path(@NotNull S source) {
        List<S> parents = Lists.newArrayList();
        S maxParent = source;
        do {
            parents.add(maxParent);
//            find parent's parent
            maxParent = parentMapping.get().get(identityFunction.apply(maxParent));
        } while (maxParent != null);
        Collections.reverse(parents);
        return parents;
    }

    @Override
    public <R> List<R> mapSource(Function<S, R> mapper, boolean withSelf) {
        return super.mapSource(mapper, false);
    }

    private Map<Object, S> loadParentMapping() {
        Map<Object, S> parentMapping = Maps.newHashMap();
        getChildren().forEach(child -> {
            parentMapping.put(identityFunction.apply(child.getSource()), null);
            child.accept((parent, c) -> {
                parentMapping.put(identityFunction.apply(c), parent);
            });
        });
        return parentMapping;
    }

    public static <S, I extends Comparable<I>> Tree<S> toTree(@NotNull Stream<S> sortedSources,
                                                              @NotNull Function<S, I> identityFunction,
                                                              @NotNull Function<S, I> parentFunction) {
        return toTree(sortedSources.iterator(), identityFunction, parentFunction);
    }

    public static <S, I extends Comparable<I>> Tree<S> toTree(@NotNull Iterable<S> sortedSources,
                                                              @NotNull Function<S, I> identityFunction,
                                                              @NotNull Function<S, I> parentFunction) {
        return toTree(sortedSources.iterator(), identityFunction, parentFunction);
    }

    public static <S, I extends Comparable<I>> Tree<S> toTree(@NotNull Iterator<S> sortedSources,
                                                              @NotNull Function<S, I> identityFunction,
                                                              @NotNull Function<S, I> parentFunction) {
        Tree<S> tree = new Tree<>(identityFunction::apply);
        Map<I, TreeNode<S>> nodeMapping = Maps.newHashMap();
        sortedSources.forEachRemaining(source -> nodeMapping.put(identityFunction.apply(source), of(source)));
        nodeMapping.forEach((sourceIdentity, node) -> {
            I parentIdentity = parentFunction.apply(node.getSource());
            if (parentIdentity == null) {
                tree.add(node);
            } else {
                TreeNode<S> parentNode = nodeMapping.get(parentIdentity);
                parentNode.add(node);
            }
        });
        return tree;
    }

    public static <S> Tree<S> empty() {
        return EMPTY_TREE;
    }
}
