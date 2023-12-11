package com.qwlabs.cdi.partition;


import jakarta.interceptor.InvocationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartitionInterceptorTest {
    private PartitionInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new PartitionInterceptor();
    }

    @Test
    void should_call_twice(@Mock InvocationContext context) throws Exception {
        TestClass target = new TestClass();
        var method = target.getClass().getMethod("testMethod", String.class, Collection.class);
        var parameters1 = "org";
        var parameters2 = List.of("1", "2", "3");
        when(context.getTarget()).thenReturn(target);
        when(context.getMethod()).thenReturn(method);
        when(context.getParameters()).thenReturn(new Object[]{parameters1, parameters2});
        when(context.proceed()).thenReturn(List.of("r1", "r2"), List.of("r3", "r4"));

        var result = interceptor.intercept(context);

        assertThat(result, is(List.of("r1", "r2", "r3", "r4")));
        verify(context, times(2)).proceed();
    }

    private static class TestClass {
        @Partition(size = 2)
        public List<String> testMethod(String orgId, @PartitionBy Collection<String> ids) {
            return List.of("1");
        }
    }
}
