package com.qwlabs.jackson.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.LogicalType;

import java.io.IOException;


@JacksonStdImpl
public class StringProcessDeserializer extends StdScalarDeserializer<String> {
    public static final StringProcessDeserializer INSTANCE = new StringProcessDeserializer();

    protected StringProcessDeserializer() {
        super(String.class);
    }

    public LogicalType logicalType() {
        return LogicalType.Textual;
    }

    public boolean isCachable() {
        return true;
    }

    public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
        return "";
    }

    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            return trim(p.getText());
        }
        if (p.hasToken(JsonToken.START_ARRAY)) {
            return this._deserializeFromArray(p, ctxt);
        }
        return trim(this._parseString(p, ctxt, this));
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    public String deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return this.deserialize(p, ctxt);
    }
}
