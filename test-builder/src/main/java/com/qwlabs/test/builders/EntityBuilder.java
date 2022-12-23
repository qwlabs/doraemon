package com.qwlabs.test.builders;

public interface EntityBuilder<T> extends ModelBuilder<T> {
    T persist();
}
