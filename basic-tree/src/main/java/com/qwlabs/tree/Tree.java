package com.qwlabs.tree;

import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Tree {

    public static <S, I extends Comparable<I>> TreeNodes<S> of(@NotNull Stream<S> sortedSources,
                                                               @NotNull Function<S, I> identityFunction,
                                                               @NotNull Function<S, I> parentFunction) {
        return of(sortedSources.iterator(), identityFunction, parentFunction);
    }

    public static <S, I extends Comparable<I>> TreeNodes<S> of(@NotNull Iterable<S> sortedSources,
                                                               @NotNull Function<S, I> identityFunction,
                                                               @NotNull Function<S, I> parentFunction) {
        return of(sortedSources.iterator(), identityFunction, parentFunction);
    }

    public static <S, I extends Comparable<I>> TreeNodes<S> of(@NotNull Iterator<S> sortedSources,
                                                               @NotNull Function<S, I> identityFunction,
                                                               @NotNull Function<S, I> parentFunction) {
        TreeNodes<S> tree = new TreeNodes<>();
        Map<I, TreeNode<S>> nodeMapping = Maps.newHashMap();
        sortedSources.forEachRemaining(source -> nodeMapping.put(identityFunction.apply(source), TreeNode.of(source)));
        nodeMapping.forEach((sourceIdentity, node) -> {
            I parentIdentity = parentFunction.apply(node.getSource());
            if (parentIdentity == null) {
                tree.add(node);
            } else {
                TreeNode<S> parentNode = nodeMapping.get(parentIdentity);
                parentNode.getChildren().add(node);
            }
        });
        return tree;
    }
}
