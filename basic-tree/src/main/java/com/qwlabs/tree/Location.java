package com.qwlabs.tree;

import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Location<N> {
    private static final RootLocation ROOT = new RootLocation<>();
    @NotNull
    private final List<N> path;

    public Location(List<N> path) {
        this.path = path;
    }

    @NotNull
    public Optional<Location<N>> parent() {
        return parent(path).map(Location::of);
    }

    public boolean isRoot() {
        return path.isEmpty();
    }

    public <R> Location<R> map(Function<N, R> mapper) {
        return Location.of(mapPath(mapper));
    }

    public <R> List<R> mapPath(Function<N, R> mapper) {
        return path.stream().map(mapper)
            .collect(Collectors.toList());
    }

    public Location<N> child(N node) {
        List<N> childPath = Lists.newArrayList(path);
        childPath.add(node);
        return Location.of(childPath);
    }

    public Optional<N> tail() {
        return isRoot() ? Optional.empty() : Optional.of(path.get(path.size() - 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location<?> location = (Location<?>) o;
        return Objects.equals(path, location.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    public static <N> Location<N> root() {
        return (Location<N>) ROOT;
    }

    public static <N> Location<N> of(List<N> path) {
        return new Location<>(path);
    }

    public static <N> Location<N> of(N... path) {
        return of(List.of(path));
    }

    public static <N> Optional<List<N>> parent(List<N> path) {
        return Optional.ofNullable(path)
            .filter(p -> !p.isEmpty())
            .map(p -> p.subList(0, p.size() - 1));
    }

    public static class RootLocation<N extends TreeNode<N>> extends Location<N> {
        public RootLocation() {
            super(List.of());
        }
    }
}
