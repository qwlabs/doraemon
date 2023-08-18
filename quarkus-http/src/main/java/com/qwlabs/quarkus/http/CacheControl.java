package com.qwlabs.quarkus.http;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface CacheControl {
    @Nonbinding
    long maxAge() default -1;
    @Nonbinding
    boolean mustRevalidate() default false;

    @Nonbinding
    boolean isPublic() default false;
}
