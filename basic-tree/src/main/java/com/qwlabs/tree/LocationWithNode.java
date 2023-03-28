package com.qwlabs.tree;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationWithNode<S> {
    private static final LocationWithNode NOT_FOUND = new NotFound<>();
    private final Location<S> location;
    private final TreeNode<S> node;

    public boolean isFound() {
        return node != null;
    }

    public static <S> LocationWithNode<S> of(Location<S> location, TreeNode<S> node) {
        return LocationWithNode.<S>builder().location(location).node(node).build();
    }

    public static <S> LocationWithNode<S> notFound() {
        return (LocationWithNode<S>) NOT_FOUND;
    }

    public static class NotFound<S> extends LocationWithNode<S> {
        NotFound() {
            super(Location.root(), null);
        }
    }
}
