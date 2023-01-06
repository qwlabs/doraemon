package com.qwlabs.lang;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Annotations {

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
        if (element == null) {
            return false;
        }
        if (element.isAnnotationPresent(annotationType)) {
            return true;
        }
        Class<?> superClass = element.getSuperclass();
        if (superClass == null) {
            return false;
        }
        return isAnnotationPresent(superClass, annotationType);
    }
}
