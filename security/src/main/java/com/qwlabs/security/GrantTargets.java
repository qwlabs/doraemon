package com.qwlabs.security;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Set;

@Getter
public class GrantTargets {
    private static final GrantTargets NOTHING = GrantTargets.of();

    private final boolean all;
    @NotNull
    private final Set<@NotNull String> targets;

    private GrantTargets(boolean all, Set<String> targets) {
        this.all = all;
        this.targets = targets == null ? Set.of() : targets;
    }

    public static GrantTargets all() {
        return new GrantTargets(true, null);
    }

    public static GrantTargets of() {
        return NOTHING;
    }

    public static GrantTargets of(Set<String> targets) {
        return new GrantTargets(true, targets);
    }
}
