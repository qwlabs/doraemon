package com.qwlabs.security;

import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import io.quarkus.security.credential.Credential;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;

import java.security.Permission;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class LazyRolesSecurityIdentity implements SecurityIdentity {
    private final Principal principal;
    private final Set<String> roles;
    private final Supplier<Set<String>> lazyRoles;
    private final Set<Credential> credentials;
    private final Map<String, Object> attributes;
    private final List<Function<Permission, Uni<Boolean>>> permissionCheckers;
    private final boolean anonymous;
    private final Supplier<Set<String>> finalRoles = Suppliers.memoize(this::loadFinalRoles);

    private Set<String> loadFinalRoles() {
        if (Objects.isNull(lazyRoles)) {
            return roles;
        }
        if (roles.isEmpty()) {
            return lazyRoles.get();
        }
        Set<String> result = Sets.newHashSet(roles);
        result.addAll(lazyRoles.get());
        return result;
    }

    private LazyRolesSecurityIdentity(Builder builder) {
        this.principal = builder.principal;
        this.roles = Collections.unmodifiableSet(builder.roles);
        this.lazyRoles = builder.lazyRoles;
        this.credentials = Collections.unmodifiableSet(builder.credentials);
        this.attributes = Collections.unmodifiableMap(builder.attributes);
        this.permissionCheckers = Collections.unmodifiableList(builder.permissionCheckers);
        this.anonymous = builder.anonymous;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAnonymous() {
        return anonymous;
    }

    @Override
    public Set<String> getRoles() {
        return finalRoles.get();
    }

    @Override
    public boolean hasRole(String role) {
        return getRoles().contains(role);
    }

    @Override
    public <T extends Credential> T getCredential(Class<T> credentialType) {
        for (Credential i : credentials) {
            if (credentialType.isAssignableFrom(i.getClass())) {
                return (T) i;
            }
        }
        return null;
    }

    @Override
    public Set<Credential> getCredentials() {
        return credentials;
    }

    @Override
    public <T> T getAttribute(String name) {
        return (T) attributes.get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Uni<Boolean> checkPermission(Permission permission) {
        if (permissionCheckers.isEmpty()) {
            return Uni.createFrom().item(false);
        }
        List<Uni<Boolean>> results = new ArrayList<>(permissionCheckers.size());
        for (Function<Permission, Uni<Boolean>> checker : permissionCheckers) {
            Uni<Boolean> res = checker.apply(permission);
            if (res != null) {
                results.add(res);
            }
        }
        if (results.isEmpty()) {
            return Uni.createFrom().item(false);
        }
        if (results.size() == 1) {
            return results.get(0);
        }

        return Uni.combine().all().unis(results).with((objects) -> {
            Boolean result = null;
            for (Object i : objects) {
                if (i != null) {
                    boolean val = (boolean) i;
                    if (val) {
                        return true;
                    }
                    result = false;
                }
            }
            return result;
        });

    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(SecurityIdentity identity) {
        return new Builder()
            .addAttributes(identity.getAttributes())
            .addCredentials(identity.getCredentials())
            .addRoles(identity.getRoles())
            .addPermissionChecker(identity::checkPermission)
            .setPrincipal(identity.getPrincipal())
            .setAnonymous(identity.isAnonymous());
    }

    public static class Builder {

        Principal principal;
        Set<String> roles = new HashSet<>();
        Supplier<Set<String>> lazyRoles;
        Set<Credential> credentials = new HashSet<>();
        Map<String, Object> attributes = new HashMap<>();
        List<Function<Permission, Uni<Boolean>>> permissionCheckers = new ArrayList<>();
        private boolean anonymous;
        boolean built = false;

        public Builder setPrincipal(Principal principal) {
            if (built) {
                throw new IllegalStateException();
            }
            this.principal = principal;
            return this;
        }

        public Builder setLazyRoles(Supplier<Set<String>> lazyRoles) {
            if (built) {
                throw new IllegalStateException();
            }
            this.lazyRoles = lazyRoles;
            return this;
        }

        public Builder addRole(String role) {
            if (built) {
                throw new IllegalStateException();
            }
            this.roles.add(role);
            return this;
        }

        public Builder addRoles(Set<String> roles) {
            if (built) {
                throw new IllegalStateException();
            }
            this.roles.addAll(roles);
            return this;
        }

        public Builder addCredential(Credential credential) {
            if (built) {
                throw new IllegalStateException();
            }
            credentials.add(credential);
            return this;
        }

        public Builder addCredentials(Set<Credential> credentials) {
            if (built) {
                throw new IllegalStateException();
            }
            this.credentials.addAll(credentials);
            return this;
        }

        public Builder addAttribute(String key, Object value) {
            if (built) {
                throw new IllegalStateException();
            }
            attributes.put(key, value);
            return this;
        }

        public Builder addAttributes(Map<String, Object> attributes) {
            if (built) {
                throw new IllegalStateException();
            }
            this.attributes.putAll(attributes);
            return this;
        }

        public Builder addPermissionChecker(Function<Permission, Uni<Boolean>> function) {
            if (built) {
                throw new IllegalStateException();
            }
            permissionCheckers.add(function);
            return this;
        }

        public Builder addPermissionCheckers(List<Function<Permission, Uni<Boolean>>> functions) {
            if (built) {
                throw new IllegalStateException();
            }
            if (functions != null) {
                permissionCheckers.addAll(functions);
            }
            return this;
        }

        public Builder setAnonymous(boolean anonymous) {
            if (built) {
                throw new IllegalStateException();
            }
            this.anonymous = anonymous;
            return this;
        }

        public LazyRolesSecurityIdentity build() {
            if (principal == null && !anonymous) {
                throw new IllegalStateException("Principal is null but anonymous status is false");
            }

            built = true;
            return new LazyRolesSecurityIdentity(this);
        }
    }
}
