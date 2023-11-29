package com.qwlabs.storage.services;


import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.qwlabs.storage.messages.StorageMessages;
import jakarta.annotation.Nullable;

import java.util.Map;
import java.util.Optional;

public class StorageContext {
    public static final String ATTRIBUTE_FILE_NAME = "fileName";
    public static final String ATTRIBUTE_FILE_HASH = "fileHash";
    public static final String ATTRIBUTE_FILE_PART_COUNT = "filePartCount";
    public static final String ATTRIBUTE_FILE_CONTENT_TYPE = "fileContentType";
    private final String businessType;
    private final Map<String, Object> attributes;

    public StorageContext(String businessType, @Nullable Map<String, Object> attributes) {
        this.businessType = businessType;
        this.attributes = Optional.ofNullable(attributes).orElseGet(Maps::newHashMap);
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getFileName() {
        return getString(ATTRIBUTE_FILE_NAME)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_NAME));
    }

    public String getFileHash() {
        return getString(ATTRIBUTE_FILE_HASH)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_HASH));
    }

    public String getFileContentType() {
        return getString(ATTRIBUTE_FILE_CONTENT_TYPE)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_CONTENT_TYPE));
    }

    public Integer getFilePartCount() {
        return getInteger(ATTRIBUTE_FILE_PART_COUNT)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_PART_COUNT));
    }

    public StorageContext put(String attributeName, String attributeValue) {
        this.attributes.put(attributeName, attributeValue);
        return this;
    }

    public Map<String, Object> get() {
        return attributes;
    }

    public Optional<Object> get(String attributeName) {
        return Optional.ofNullable(attributes.get(attributeName));
    }

    public Optional<String> getString(String attributeName) {
        return get(attributeName).map(Object::toString);
    }

    public Optional<Integer> getInteger(String attributeName) {
        return getString(attributeName)
                .map(Ints::tryParse);
    }

    public Optional<Long> getLong(String attributeName) {
        return getString(attributeName)
                .map(Longs::tryParse);
    }
}
