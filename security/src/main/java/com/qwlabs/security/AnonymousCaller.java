package com.qwlabs.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Nullable;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public class AnonymousCaller implements Caller {
    public static final AnonymousCaller INSTANCE = new AnonymousCaller();
    private static final CallerAttributes ATTRIBUTES = new CallerAttributes() {
        @Override
        public <A, C> A get(String name, C context) {
            return null;
        }
    };

    private static final CallerPermissions PERMISSIONS = new CallerPermissions() {
        @Override
        public Set<@NotNull String> raw() {
            return Set.of();
        }

        @Override
        public Set<@NotNull String> raw(String scope) {
            return Set.of();
        }

        @Override
        public boolean has(String permission) {
            return false;
        }

        @Override
        public boolean has(String scope, String permission) {
            return false;
        }

        @Override
        public GrantTargets targets(String permission, String targetType) {
            return GrantTargets.none();
        }
    };

    private static final CallerFeatures FEATURES = new CallerFeatures() {

        @Override
        public @NotNull boolean has(@NotNull String feature) {
            return false;
        }
    };

    @Override
    public @Nullable String id() {
        return null;
    }

    @Override
    public @Nullable String type() {
        return null;
    }

    @Override
    public CallerAttributes attributes() {
        return ATTRIBUTES;
    }

    @Override
    public CallerPermissions permissions() {
        return PERMISSIONS;
    }

    @Override
    public @NotNull CallerFeatures features() {
        return FEATURES;
    }

    @Override
    public SecurityIdentity identity() {
        return null;
    }

    @Override
    public boolean authenticated() {
        return false;
    }
}
