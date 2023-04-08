package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

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
        var location = parentLocation.child(this);
        newTreeNode.setNode(newNode);
        newTreeNode.setChildren(safeChildren().map(mapper, location));
        return newTreeNode;
    }

    public <R> R map(TreeNodeFunction<N, R> mapper) {
        return map(mapper, Location.root());
    }

    public <R> R map(TreeNodeFunction<N, R> mapper,
                     Location<TreeNode<N>> parentLocation) {
        var location = parentLocation.child(this);
        var children = safeChildren().map(mapper, location);
        return mapper.apply(parentLocation, this, children);
    }

    public <R> R map(Function<N, R> mapper) {
        return mapper.apply(node);
    }

    public TreeNode<N> addChildren(TreeNode<N>... addChildren) {
        if (addChildren != null && addChildren.length > 0) {
            TreeNodes<N> children = Optional.ofNullable(getChildren())
                .orElseGet(TreeNodes::of);
            children.addAll(List.of(addChildren));
            setChildren(children);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(node, treeNode.node) && Objects.equals(children, treeNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, children);
    }

    public static <N> TreeNode<N> of(N node) {
        var treeNode = new TreeNode<N>();
        treeNode.setNode(node);
        return treeNode;
    }

    public static <N> TreeNode<N> of(N node, TreeNode<N>... children) {
        var treeNode = new TreeNode<N>();
        treeNode.setNode(node);
        treeNode.addChildren(children);
        return treeNode;
    }

    public static <N> TreeNode<N> of(N node, TreeNodes<N> children) {
        var treeNode = new TreeNode<N>();
        treeNode.setNode(node);
        treeNode.setChildren(children);
        return treeNode;
    }
}
