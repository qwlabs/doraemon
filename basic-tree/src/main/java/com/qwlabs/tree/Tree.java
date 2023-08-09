package com.qwlabs.tree;

import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Tree {
    public static <N, I extends Comparable<I>> TreeNodes<N>
    of(@NotNull Stream<N> sortedSources,
       @NotNull Function<N, I> identityFunction,
       @NotNull Function<N, I> parentFunction) {
        return of(sortedSources.iterator(), identityFunction, parentFunction);
    }

    public static <N, I extends Comparable<I>> TreeNodes<N>
    of(@NotNull Iterable<N> sortedSources,
       @NotNull Function<N, I> identityFunction,
       @NotNull Function<N, I> parentFunction) {
        return of(sortedSources.iterator(), identityFunction, parentFunction);
    }

    public static <N, I extends Comparable<I>> TreeNodes<N>
    of(@NotNull Iterator<N> sortedSources,
       @NotNull Function<N, I> identityFunction,
       @NotNull Function<N, I> parentFunction) {
        TreeNodes<N> tree = new TreeNodes<>();
        Map<I, TreeNode<N>> nodeMapping = Maps.newLinkedHashMap();
        sortedSources.forEachRemaining(node -> nodeMapping.put(identityFunction.apply(node), TreeNode.of(node)));
        nodeMapping.forEach((sourceIdentity, node) -> {
            I parentIdentity = parentFunction.apply(node.getNode());
            if (parentIdentity == null) {
                tree.add(node);
            } else {
                TreeNode<N> parentNode = nodeMapping.get(parentIdentity);
                parentNode.addChildren(node);
            }
        });
        return tree;
    }
}
