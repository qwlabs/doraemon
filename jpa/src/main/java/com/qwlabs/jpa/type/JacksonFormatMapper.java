package com.qwlabs.jpa.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qwlabs.jackson.Jackson;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.FormatMapper;

import java.lang.reflect.Type;


public class JacksonFormatMapper implements FormatMapper {

    @Override
    public <T> T fromString(CharSequence charSequence, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        if (javaType.getJavaType() == String.class || javaType.getJavaType() == Object.class) {
            return (T) charSequence.toString();
        }
        try {
            return Jackson.getMapper().readValue(charSequence.toString(), new TypeReference<>() {
                @Override
                public Type getType() {
                    return javaType.getJavaType();
                }
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not deserialize string to java type: " + javaType, e);
        }
    }

    @Override
    public <T> String toString(T t, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        if (javaType.getJavaType() == String.class || javaType.getJavaType() == Object.class) {
            return (String) t;
        }
        try {
            return Jackson.getMapper().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize object of java type: " + javaType, e);
        }
    }
}
