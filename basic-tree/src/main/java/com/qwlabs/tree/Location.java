package com.qwlabs.tree;

import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class Location<S> {
    private static final RootLocation ROOT = new RootLocation<>();
    @NotNull
    private final List<S> path;

    @Nullable
    public S parent() {
        return path.isEmpty() ? null : path.get(path.size() - 1);
    }

    public static <S> Location<S> root() {
        return (Location<S>) ROOT;
    }

    public <R> List<R> mapPath(Function<S, R> mapper) {
        return path.stream().map(mapper)
                .collect(Collectors.toList());
    }

    public Location<S> child(S source) {
        List<S> childPath = Lists.newArrayList(path);
        childPath.add(source);
        return Location.<S>builder()
                .path(childPath)
                .build();
    }

    public static class RootLocation<S> extends Location<S> {
        public RootLocation() {
            super(List.of());
        }
    }
}
