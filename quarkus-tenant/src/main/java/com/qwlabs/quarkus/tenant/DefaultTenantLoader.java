package com.qwlabs.quarkus.tenant;

import java.util.function.Function;

public interface DefaultTenantLoader extends Function<Tenant, String> {
}
