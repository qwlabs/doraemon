package com.qwlabs.security;

import com.qwlabs.lang.C2;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Set;

@Getter
public class GrantTargets {
    private static final GrantTargets ALL = new GrantTargets(true, null);
    private static final GrantTargets NONE = new GrantTargets(false, null);

    private final boolean all;
    @NotNull
    private final Set<@NotNull String> targets;

    private GrantTargets(boolean all, Set<String> targets) {
        this.all = all;
        this.targets = targets == null ? Set.of() : targets;
    }

    public boolean isNone() {
        return !isAll() && C2.isEmpty(targets);
    }

    public static GrantTargets all() {
        return ALL;
    }

    public static GrantTargets none() {
        return NONE;
    }

    public static GrantTargets of(Set<String> targets) {
        return new GrantTargets(false, targets);
    }
}
