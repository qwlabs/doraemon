package com.qwlabs.quarkus.http;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import com.qwlabs.lang.S2;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CacheControl()
@Priority(1)
@Interceptor
public class CacheControlInterceptor {

    private RoutingContext routingContext;

    @Inject
    public CacheControlInterceptor(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        var cacheControl = context.getMethod().getAnnotation(CacheControl.class);
        of(cacheControl).ifPresent(content -> routingContext.response().putHeader(HttpHeaders.CACHE_CONTROL, content));
        return context.proceed();
    }

    private Optional<String> of(CacheControl cacheControl) {
        List<String> options = new ArrayList<>();
        if (cacheControl.isPublic()) {
            options.add("public");
        }
        if (cacheControl.isPrivate()) {
            options.add("private");
        }
        if (cacheControl.maxAge() > 0) {
            options.add("max-age=%d".formatted(cacheControl.maxAge()));
        }
        if (cacheControl.shareMaxAge() > 0) {
            options.add("s-maxage=%d".formatted(cacheControl.shareMaxAge()));
        }
        if (cacheControl.minFresh() > 0) {
            options.add("min-fresh=%d".formatted(cacheControl.minFresh()));
        }
        if (cacheControl.mustRevalidate()) {
            options.add("must-revalidate");
        }
        if (cacheControl.immutable()) {
            options.add("immutable");
        }
        return Optional.of(Joiner.on(",").join(options))
            .filter(S2::isNotBlank);
    }
}

