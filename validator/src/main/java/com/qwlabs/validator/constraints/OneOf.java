package com.qwlabs.validator.constraints;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(
        validatedBy = {}
)
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OneOf.List.class)
public @interface OneOf {

    String message() default "{com.qwlabs.validator.constraints.OneOf.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

    boolean allowEmpty() default false;

    boolean ignoreCase() default false;

    boolean ignoreWhitespace() default false;

    @Target({
            ElementType.METHOD,
            ElementType.FIELD,
            ElementType.ANNOTATION_TYPE,
            ElementType.CONSTRUCTOR,
            ElementType.PARAMETER,
            ElementType.TYPE_USE
    })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        OneOf[] value();
    }
}