package com.qwlabs.excel.parsers;

public interface Parser<T> {
    T parse(String value);
}
