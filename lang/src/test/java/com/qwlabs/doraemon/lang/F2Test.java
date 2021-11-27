package com.qwlabs.doraemon.lang;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class F2Test {
    @Test
    void should_ifEmpty() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);

        F2.ifEmpty(null, consumer1);
        F2.ifEmpty("", consumer2);

        verify(consumer1).accept();
        verify(consumer2, never()).accept();
    }

    @Test
    void should_ifPresent_empty_customer() {
        EmptyConsumer consumer1 = mock(EmptyConsumer.class);
        EmptyConsumer consumer2 = mock(EmptyConsumer.class);

        F2.ifPresent(null, consumer1);
        F2.ifPresent("", consumer2);

        verify(consumer1, never()).accept();
        verify(consumer2).accept();
    }

    @Test
    void should_ifPresent_customer() {
        Consumer<String> consumer1 = mock(Consumer.class);
        Consumer<String> consumer2 = mock(Consumer.class);

        F2.ifPresent(null, consumer1);
        F2.ifPresent("", consumer2);

        verify(consumer1, never()).accept(any());
        verify(consumer2).accept("");
    }
}
