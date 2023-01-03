package com.qwlabs.security;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class CallerIdentityAugmentor implements SecurityIdentityAugmentor {
    private final CallerPrincipalLoader callerPrincipalLoader;

    @Inject
    public CallerIdentityAugmentor(CallerPrincipalLoader callerPrincipalLoader) {
        this.callerPrincipalLoader = callerPrincipalLoader;
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
        CallerPrincipal callerPrincipal = callerPrincipalLoader.load(identity);
        return AuthenticatedCaller.builder()
                .id(callerPrincipal.id())
                .type(callerPrincipal.type())
                .build();
    }
}
