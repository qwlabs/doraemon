package com.qwlabs.graphql.relay;

import graphql.PublicApi;
import graphql.relay.DefaultConnectionCursor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@PublicApi
public class ConnectionCursor extends DefaultConnectionCursor {

    public ConnectionCursor(String value) {
        super(value);
    }

    public static ConnectionCursor valueOf(String value) {
        return new ConnectionCursor(value);
    }

    @Override
    public String toString() {
        return getValue();
    }

    public static ConnectionCursor createCursor(long offset) {
        byte[] bytes = Long.toString(offset).getBytes(StandardCharsets.UTF_8);
        return valueOf(Base64.getEncoder().encodeToString(bytes));
    }
}
