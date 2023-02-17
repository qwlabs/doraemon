package com.qwlabs.security;

import io.quarkus.security.identity.SecurityIdentity;

public interface AnonymousCallerProvider {
    Caller get(SecurityIdentity identity);
}
