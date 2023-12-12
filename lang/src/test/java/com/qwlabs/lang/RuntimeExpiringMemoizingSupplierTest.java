package com.qwlabs.lang;

import com.google.common.base.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RuntimeExpiringMemoizingSupplierTest {
    @Test
    void should_call_once(@Mock RealSupplier delegate) {
        RuntimeExpiringMemoizingSupplier<String> supplier = new RuntimeExpiringMemoizingSupplier<>(delegate);

        when(delegate.get()).thenReturn(WithExpiring.of("1", 1, TimeUnit.DAYS));

        var value1 = supplier.get();
        var value2 = supplier.get();

        assertSame(value1, value2);
        assertThat(value1, is("1"));

        verify(delegate).get();
    }

    static class RealSupplier implements Supplier<WithExpiring<String>> {
        @Override
        public WithExpiring<String> get() {
            return WithExpiring.of("real", 1, TimeUnit.DAYS);
        }
    }
}
