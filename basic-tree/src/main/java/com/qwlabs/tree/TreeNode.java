package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@Getter
@Setter
public class TreeNode<N> implements TreeNodeAble<N> {

    @JsonUnwrapped
    private N node;

    private TreeNodes<N> children;

    private TreeNodes<N> safeChildren() {
        return Optional.ofNullable(children).orElseGet(TreeNodes::empty);
    }

    @Override
    public void forEach(BiConsumer<Location<TreeNode<N>>, TreeNode<N>> consumer, Location<TreeNode<N>> parentLocation) {
        consumer.accept(parentLocation, this);
        var location = parentLocation.child(this);
        safeChildren().forEach(consumer, location);
    }

    @Override
    public Optional<Location<TreeNode<N>>> find(BiPredicate<Location<TreeNode<N>>, TreeNode<N>> filter, Location<TreeNode<N>> parentLocation) {
        var location = parentLocation.child(this);
        if (filter.test(parentLocation, this)) {
            return Optional.of(location);
        }
        return safeChildren().find(filter, location);
    }

    @Override
    public <R> List<R> all(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper, Location<TreeNode<N>> parentLocation) {
        var location = parentLocation.child(this);
        List<R> results = Lists.newArrayList(safeChildren().all(mapper, location));
        results.add(0, mapper.apply(parentLocation, this));
        return results;
    }

    public <R> TreeNode<R> map(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper) {
        return map(mapper, Location.root());
    }

    public <R> TreeNode<R> map(BiFunction<Location<TreeNode<N>>, TreeNode<N>, R> mapper, Location<TreeNode<N>> parentLocation) {
        R newNode = mapper.apply(parentLocation, this);
        var newTreeNode = new TreeNode<R>();
        newTreeNode.setNode(newNode);
        newTreeNode.setChildren(safeChildren().map(mapper, parentLocation));
        return newTreeNode;
    }

    public TreeNode<N> addChildren(TreeNode<N>... addChildren) {
        TreeNodes<N> children = Optional.ofNullable(getChildren())
            .orElseGet(TreeNodes::of);
        children.addAll(List.of(addChildren));
        setChildren(children);
        return this;
    }

    public static <N> TreeNode<N> of(N node) {
        var treeNode = new TreeNode<N>();
        treeNode.setNode(node);
        return treeNode;
    }
}
