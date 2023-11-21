package com.qwlabs.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class Annotations {
    private Annotations() {
    }

    public static <T> Class<T> actualTypeArgument(Class<?> element) {
        return actualTypeArgument(element, 0);
    }

    public static <T> Class<T> actualTypeArgument(Class<?> element, int index) {
        if (element == null) {
            return null;
        }
        Type genericSuperclass = element.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > index) {
                Type genericType = actualTypeArguments[index];
                return (Class<T>) genericType;
            }
        }
        Class<?> superClass = element.getSuperclass();
        if (superClass == null) {
            return null;
        }
        return actualTypeArgument(superClass, index);
    }

    public static boolean isAnnotationPresent(Class<?> element, Class<? extends Annotation> annotationType) {
        return getAnnotation(element, annotationType) != null;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> element, Class<A> annotationType) {
        if (element == null) {
            return null;
        }
        var may = element.getAnnotation(annotationType);
        if (may != null) {
            return may;
        }
        return getAnnotation(element.getSuperclass(), annotationType);
    }
}
