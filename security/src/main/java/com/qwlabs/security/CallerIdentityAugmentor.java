package com.qwlabs.security;

import com.qwlabs.cdi.DispatchInstance;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CallerIdentityAugmentor implements SecurityIdentityAugmentor {
    private final CallerPrincipalLoader principalLoader;
    private final Instance<AnonymousCallerProvider> anonymousCallerProvider;
    private final DispatchInstance<Caller, CallerPermissionsLoader> permissionsLoader;
    private final DispatchInstance<String, CallerAttributeLoader<?, ?>> attributeLoader;
    private final CallerIdentityRolesProvider identityRolesProvider;

    @Inject
    public CallerIdentityAugmentor(Instance<CallerPrincipalLoader> principalLoader,
                                   Instance<AnonymousCallerProvider> anonymousCallerProvider,
                                   Instance<CallerPermissionsLoader> permissionsLoader,
                                   Instance<CallerAttributeLoader<?, ?>> attributeLoader,
                                   CallerIdentityRolesProvider identityRolesProvider) {
        this.principalLoader = principalLoader.get();
        this.anonymousCallerProvider = anonymousCallerProvider;
        this.permissionsLoader = DispatchInstance.of(permissionsLoader);
        this.attributeLoader = DispatchInstance.of(attributeLoader);
        this.identityRolesProvider = identityRolesProvider;
    }

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        return context.runBlocking(() -> doAugment(identity));
    }

    private SecurityIdentity doAugment(SecurityIdentity identity) {
        Caller caller = identity.isAnonymous() ? buildAnonymousCaller(identity) : buildAuthenticatedCaller(identity);
        var newIdentity = buildSecurityIdentity(identity, caller);
        caller.identity(newIdentity);
        return newIdentity;
    }

    private SecurityIdentity buildSecurityIdentity(SecurityIdentity oldIdentity, Caller caller) {
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(oldIdentity);
        var roles = identityRolesProvider.get(caller);
        builder.addRoles(roles);
        builder.addAttribute(Caller.class.getName(), caller);
        return builder.build();
    }

    private Caller buildAnonymousCaller(SecurityIdentity identity) {
        if (anonymousCallerProvider.isUnsatisfied()) {
            return AnonymousCaller.INSTANCE;
        }
        return anonymousCallerProvider.get().get(identity);
    }

    private Caller buildAuthenticatedCaller(SecurityIdentity identity) {
        CallerPrincipal callerPrincipal = principalLoader.load(identity);
        return AuthenticatedCaller.builder()
                .id(callerPrincipal.id())
                .type(callerPrincipal.type())
                .attributeLoader(attributeLoader)
                .permissionsLoader(permissionsLoader)
                .build();
    }
}
