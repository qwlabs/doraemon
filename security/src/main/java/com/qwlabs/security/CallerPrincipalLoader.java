package com.qwlabs.security;

import io.quarkus.security.identity.SecurityIdentity;

public interface CallerPrincipalLoader {
    CallerPrincipal load(SecurityIdentity identity);
}
