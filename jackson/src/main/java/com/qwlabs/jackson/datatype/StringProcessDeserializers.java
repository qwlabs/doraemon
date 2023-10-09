package com.qwlabs.jackson.datatype;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;

import java.io.Serializable;


public class StringProcessDeserializers extends Deserializers.Base implements Serializable {
    public static final StringProcessDeserializers INSTANCE = new StringProcessDeserializers();

    @Override
    public JsonDeserializer<?> findBeanDeserializer(JavaType type,
                                                    DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {
        if (type.hasRawClass(String.class)) {
            return StringProcessDeserializer.INSTANCE;
        }
        return super.findBeanDeserializer(type, config, beanDesc);
    }
}
