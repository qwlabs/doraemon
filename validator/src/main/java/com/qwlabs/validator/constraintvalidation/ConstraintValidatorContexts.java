package com.qwlabs.validator.constraintvalidation;

import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import java.util.StringJoiner;
import java.util.function.Consumer;

public final class ConstraintValidatorContexts {
    private static final String TEMPLATE_DELIMITER = ".";
    private static final String TEMPLATE_PREFIX = "{";
    private static final String TEMPLATE_SUFFIX = "}";

    public static String ofTemplate(Class<?> templateClass, String... suffix) {
        StringJoiner joiner = new StringJoiner(TEMPLATE_DELIMITER, TEMPLATE_PREFIX, TEMPLATE_SUFFIX);
        return joiner.add(templateClass.getName())
                .add(String.join(TEMPLATE_DELIMITER, suffix))
                .toString();
    }

    public static ConstraintValidatorContext add(ConstraintValidatorContext context,
                                                 Class<?> templateClass, String... suffix) {
        return add(context, null, ofTemplate(templateClass, suffix));
    }

    public static ConstraintValidatorContext add(ConstraintValidatorContext context,
                                                 Consumer<HibernateConstraintValidatorContext> consumer,
                                                 Class<?> templateClass, String... suffix) {
        return add(context, consumer, ofTemplate(templateClass, suffix));
    }

    public static ConstraintValidatorContext add(ConstraintValidatorContext context,
                                                 Consumer<HibernateConstraintValidatorContext> consumer,
                                                 String template) {
        acceptContext(context, consumer);
        context.buildConstraintViolationWithTemplate(template).addConstraintViolation();
        return context;
    }

    public static ConstraintValidatorContext addToProperty(ConstraintValidatorContext context,
                                                           String template,
                                                           String propertyNode) {
        return addToProperty(context, template, propertyNode, null);
    }


    public static ConstraintValidatorContext addToProperty(ConstraintValidatorContext context,
                                                           String template,
                                                           String propertyNode,
                                                           Consumer<HibernateConstraintValidatorContext> consumer) {
        acceptContext(context, consumer);
        context.buildConstraintViolationWithTemplate(template)
                .addPropertyNode(propertyNode)
                .addConstraintViolation();
        return context;
    }

    private static void acceptContext(ConstraintValidatorContext context, Consumer<HibernateConstraintValidatorContext> consumer) {
        if (consumer != null) {
            HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
            consumer.accept(hibernateContext);
        }
    }
}
