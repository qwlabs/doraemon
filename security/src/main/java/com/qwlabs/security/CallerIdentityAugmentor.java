package com.qwlabs.security;

import com.qwlabs.cdi.DispatchInstance;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class CallerIdentityAugmentor implements SecurityIdentityAugmentor {
    private final CallerPrincipalLoader principalLoader;
    private final DispatchInstance<Caller, CallerPermissionsLoader> permissionsLoader;
    private final DispatchInstance<String, CallerAttributeLoader<?>> attributeLoader;

    @Inject
    public CallerIdentityAugmentor(Instance<CallerPrincipalLoader> principalLoader,
                                   Instance<CallerPermissionsLoader> permissionsLoader,
                                   Instance<CallerAttributeLoader<?>> attributeLoader) {
        this.principalLoader = principalLoader.get();
        this.permissionsLoader = DispatchInstance.of(permissionsLoader);
        this.attributeLoader = DispatchInstance.of(attributeLoader);
    }

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        return context.runBlocking(() -> doAugment(identity));
    }

    private SecurityIdentity doAugment(SecurityIdentity identity) {
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
        Caller caller = identity.isAnonymous() ? AnonymousCaller.INSTANCE : buildCurrentCaller(identity);
        Caller.set(builder, caller);
        return builder.build();
    }

    private Caller buildCurrentCaller(SecurityIdentity identity) {
        CallerPrincipal callerPrincipal = principalLoader.load(identity);
        return AuthenticatedCaller.builder()
                .id(callerPrincipal.id())
                .type(callerPrincipal.type())
                .securityIdentity(identity)
                .attributeLoader(attributeLoader)
                .permissionsLoader(permissionsLoader)
                .build();
    }
}
