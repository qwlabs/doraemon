package com.qwlabs.tree;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseTreeNode<N extends TreeNode<N>> implements TreeNode<N> {
    private TreeNodes<N> children;
}
