package com.qwlabs.security;

import java.util.Set;

public interface CallerIdentityRolesProvider {
    Set<String> get(Caller caller);
}
