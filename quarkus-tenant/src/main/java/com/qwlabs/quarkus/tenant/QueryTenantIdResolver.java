package com.qwlabs.quarkus.tenant;

import com.qwlabs.cdi.Dispatchable;
import com.qwlabs.lang.C2;
import com.qwlabs.lang.S2;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.Nullable;

@ApplicationScoped
public class QueryTenantIdResolver implements TenantIdResolver, Dispatchable<String> {

    @Override
    public String resolve(RoutingContext context, TenantConfig config) {
        var request = context.request();
        return C2.stream(config.queryNames())
            .map(request::getParam)
            .filter(S2::isNotBlank)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean dispatchable(@Nullable String context) {
        return TenantConfig.SOURCE_QUERY.equalsIgnoreCase(context);
    }

}
