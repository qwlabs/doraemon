package com.qwlabs.cdi.partition;


import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Partition {
    int ONE_THOUSAND = 1000;
    int TEN_THOUSAND = 10000;

    @Nonbinding
    int size() default ONE_THOUSAND;
}
