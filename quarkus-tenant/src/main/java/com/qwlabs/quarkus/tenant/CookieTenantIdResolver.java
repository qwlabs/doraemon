package com.qwlabs.quarkus.tenant;

import com.qwlabs.cdi.dispatch.Dispatchable;
import com.qwlabs.lang.C2;
import com.qwlabs.lang.S2;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.Nullable;

import java.util.Objects;

@ApplicationScoped
public class CookieTenantIdResolver implements TenantIdResolver, Dispatchable<String> {

    @Override
    public String resolve(RoutingContext context, TenantConfig config) {
        var request = context.request();
        return C2.stream(config.cookieNames())
                .map(request::getCookie)
                .filter(Objects::nonNull)
                .map(Cookie::getValue)
                .filter(S2::isNotBlank)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean dispatchable(@Nullable String context) {
        return TenantConfig.SOURCE_COOKIE.equalsIgnoreCase(context);
    }

}
