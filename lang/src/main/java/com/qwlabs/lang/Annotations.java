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
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length <= index) {
            return null;
        }
        Type genericType = actualTypeArguments[index];
        return (Class<T>) genericType;
    }

    public static boolean isAnnotationPresent(Class<?> element, Class<? extends Annotation> annotationType) {
        if (element == null) {
            return false;
        }
        if (element.isAnnotationPresent(annotationType)) {
            return true;
        }
        if (element.getSuperclass() == null) {
            return false;
        }
        return isAnnotationPresent(element.getSuperclass().getSuperclass(), annotationType);
    }
}
