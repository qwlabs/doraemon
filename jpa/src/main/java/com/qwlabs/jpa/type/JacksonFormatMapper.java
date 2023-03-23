package com.qwlabs.jpa.type;

import com.qwlabs.jackson.Jackson;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.format.FormatMapper;
import org.hibernate.type.format.jackson.JacksonJsonFormatMapper;


public class JacksonFormatMapper implements FormatMapper {
    private final JacksonJsonFormatMapper delegate;

    public JacksonFormatMapper() {
        this.delegate = new JacksonJsonFormatMapper(Jackson.getMapper());
    }

    @Override
    public <T> T fromString(CharSequence charSequence, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return delegate.fromString(charSequence, javaType, wrapperOptions);
    }

    @Override
    public <T> String toString(T t, JavaType<T> javaType, WrapperOptions wrapperOptions) {
        return delegate.toString(t, javaType, wrapperOptions);
    }
}
