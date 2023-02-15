package com.qwlabs.quarkus.tenant;

import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.inject.Instance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTenantTest {
    @Mock
    RoutingContext routingContext;
    @Mock
    TenantConfig config;
    @Mock
    DefaultTenantResolver defaultTenantResolver;
    @Mock
    Instance<TenantIdResolver> idResolvers;
    @Mock
    Instance<TenantAttributeResolver<?>> attributeResolvers;

    Tenant tenant;

    @BeforeEach
    void setUp() {
        tenant = new DefaultTenant(routingContext, config,
                defaultTenantResolver,
                idResolvers,
                attributeResolvers);
    }

    @Test
    void should_default_when_single_and_called_once() {
        when(config.enabled()).thenReturn(false);
        when(defaultTenantResolver.get(tenant)).thenReturn("mock");

        assertThat(tenant.tenantId(), is("mock"));
        assertThat(tenant.tenantId(), is("mock"));

        verify(config).enabled();
        verify(defaultTenantResolver).get(tenant);
    }
}