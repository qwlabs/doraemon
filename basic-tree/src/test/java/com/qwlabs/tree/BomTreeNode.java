package com.qwlabs.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BomTreeNode extends HashMap<String, Object> {
    public BomTreeNode(Map<? extends String, ?> m) {
        super(m);
    }

    public static BomTreeNode of(TreeNode<BomNode> node, List<BomTreeNode> children) {
        BomTreeNode treeNode = new BomTreeNode(node.getNode().getAttributes());
        treeNode.put("children", children);
        return treeNode;
    }
}
