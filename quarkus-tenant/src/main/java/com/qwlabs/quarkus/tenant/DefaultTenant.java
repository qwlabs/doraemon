package com.qwlabs.quarkus.tenant;

import com.google.common.base.Suppliers;
import com.qwlabs.cdi.dispatch.DispatchInstance;
import com.qwlabs.lang.C2;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@RequestScoped
public class DefaultTenant implements Tenant {
    private static final DefaultTenantResolver DEFAULT_TENANT_RESOLVER = (tenant) -> null;
    private final RoutingContext routingContext;
    private final TenantConfig config;

    private final Supplier<String> idSupplier = Suppliers.memoize(this::resolveId);

    private final Instance<DefaultTenantResolver> defaultResolver;
    private final DispatchInstance<String, TenantIdResolver> idResolvers;
    private final DispatchInstance<String, TenantAttributeResolver<?>> attributeResolvers;

    @Inject
    public DefaultTenant(RoutingContext routingContext,
                         TenantConfig config,
                         Instance<DefaultTenantResolver> defaultResolver,
                         Instance<TenantIdResolver> idResolvers,
                         Instance<TenantAttributeResolver<?>> attributeResolvers) {
        this.routingContext = routingContext;
        this.config = config;
        this.defaultResolver = defaultResolver;
        this.idResolvers = DispatchInstance.of(idResolvers, true);
        this.attributeResolvers = DispatchInstance.of(attributeResolvers, true);
    }

    private String defaultTenantId() {
        if (defaultResolver.isResolvable()) {
            return defaultResolver.get().get(this);
        }
        return DEFAULT_TENANT_RESOLVER.get(this);
    }

    private String resolveId() {
        if (isSingle()) {
            return defaultTenantId();
        }
        return C2.stream(config.sources())
            .map(idResolvers::get)
            .map(resolver -> resolver.resolve(routingContext, config))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(this::defaultTenantId);
    }

    @Override
    @ActivateRequestContext
    public String tenantId() {
        return idSupplier.get();
    }

    @Override
    @ActivateRequestContext
    public <T> T attribute(String name) {
        return attributeResolvers.getOptional(name)
            .filter(resolver -> resolver.resolvable(this, name))
            .map(resolver -> (T) resolver.resolve(this))
            .orElse(null);
    }

    @Override
    public TenantConfig config() {
        return config;
    }
}
