package com.qwlabs.security;

import com.qwlabs.cdi.dispatch.DispatchInstance;
import io.quarkus.security.identity.SecurityIdentity;

public interface AnonymousCallerProvider {
    Caller get(SecurityIdentity identity,
               DispatchInstance<Caller, CallerPermissionsLoader> permissionsLoader,
               DispatchInstance<String, CallerAttributeLoader<?, ?>> attributeLoader);
}
