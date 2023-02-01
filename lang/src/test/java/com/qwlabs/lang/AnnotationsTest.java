package com.qwlabs.lang;

import org.junit.jupiter.api.Test;

import static com.qwlabs.lang.Annotations.actualTypeArgument;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class AnnotationsTest {

    @Test
    void should_get_type() {
        assertThat(actualTypeArgument(A.class).toString(), is("class java.lang.String"));
        assertThat(actualTypeArgument(B.class).toString(), is("class java.lang.String"));
    }

    public interface I {

    }

    public abstract static class Base<E> implements I {

    }

    public static class A extends Base<String> {

    }

    public static class B extends A {

    }
}