package com.qwlabs.quarkus.tenant;

import io.vertx.ext.web.RoutingContext;

public interface TenantIdResolver {
    String resolve(RoutingContext context, TenantConfig config);
}
