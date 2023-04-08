package com.qwlabs.tree;

import java.util.List;

@FunctionalInterface
public interface TreeNodeFunction<N, R> {
    R apply(Location<TreeNode<N>> location, TreeNode<N> node, List<R> children);
}
