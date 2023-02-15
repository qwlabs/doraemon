package com.qwlabs.quarkus.tenant;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Set;

@ConfigMapping(prefix = "quarkus.tenant")
public interface TenantConfig {
    String SOURCE_QUERY = "query";
    String SOURCE_HEADER = "header";
    String SOURCE_COOKIE = "cookie";

    @WithDefault("false")
    boolean enabled();

    @WithDefault("query,header,cookie")
    List<String> sources();

    @WithDefault("X-TENANT-ID")
    Set<String> cookieNames();

    @WithDefault("X-TENANT-ID")
    Set<String> headerNames();

    @WithDefault("tenant_id")
    Set<String> queryNames();
}
