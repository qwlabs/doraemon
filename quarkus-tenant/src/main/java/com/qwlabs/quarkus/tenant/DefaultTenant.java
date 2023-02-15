package com.qwlabs.quarkus.tenant;

import com.google.common.base.Suppliers;
import com.qwlabs.cdi.DispatchInstance;
import com.qwlabs.lang.C2;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.function.Supplier;

@RequestScoped
public class DefaultTenant implements Tenant {
    private final RoutingContext routingContext;
    private final TenantConfig config;

    private final Supplier<String> idSupplier = Suppliers.memoize(this::resolveId);

    private final DefaultTenantProvider defaultTenantProvider;
    private final DispatchInstance<String, TenantIdResolver> idResolvers;
    private final DispatchInstance<String, TenantAttributeResolver<?>> attributeResolvers;

    @SuppressWarnings("checkstyle:WhitespaceAfter")
    @Inject
    public DefaultTenant(
        RoutingContext routingContext,
        TenantConfig config,
        DefaultTenantProvider defaultTenantProvider,
        Instance<TenantIdResolver> resolvers,
        Instance<TenantAttributeResolver<?>> attributeResolvers) {
        this.routingContext = routingContext;
        this.config = config;
        this.defaultTenantProvider = defaultTenantProvider;
        this.idResolvers = DispatchInstance.of(resolvers, true);
        this.attributeResolvers = DispatchInstance.of(attributeResolvers, true);
    }

    private String resolveId() {
        return C2.stream(config.sources())
            .map(idResolvers::get)
            .map(resolver -> resolver.resolve(routingContext, config))
            .findFirst()
            .orElseGet(defaultTenantProvider);
    }

    @Override
    @ActivateRequestContext
    public String tenantId() {
        return idSupplier.get();
    }

    @Override
    public <T> T attribute(String name) {
        return attributeResolvers.getOptional(name)
            .map(attributeResolver -> (T) attributeResolver.resolve(this))
            .orElse(null);
    }

    @Override
    public TenantConfig config() {
        return config;
    }
}
