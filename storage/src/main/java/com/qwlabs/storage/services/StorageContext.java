package com.qwlabs.storage.services;


import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.qwlabs.storage.messages.StorageMessages;

import java.util.Map;
import java.util.Optional;

public class StorageContext {
    private static final String ATTRIBUTE_USER_ID = "userId";
    private static final String ATTRIBUTE_FILE_NAME = "fileName";
    private static final String ATTRIBUTE_FILE_HASH = "fileHash";
    private static final String ATTRIBUTE_FILE_PART_COUNT = "filePartCount";
    private static final String ATTRIBUTE_FILE_CONTENT_TYPE = "fileContentType";
    private final String businessType;
    private final Map attributes;

    public StorageContext(String businessType, Map attributes) {
        this.businessType = businessType;
        this.attributes = Optional.ofNullable(attributes).orElseGet(Maps::newHashMap);
    }

    public StorageContext setUserId(String userId) {
        this.attributes.put(ATTRIBUTE_USER_ID, userId);
        return this;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getFileName() {
        return getString(ATTRIBUTE_FILE_NAME);
    }

    public String getFileHash() {
        return getString(ATTRIBUTE_FILE_HASH);
    }

    public String getFileContentType() {
        return getString(ATTRIBUTE_FILE_CONTENT_TYPE);
    }

    public Integer getFilePartCount() {
        return getInteger(ATTRIBUTE_FILE_PART_COUNT);
    }

    public String getUserId() {
        return getString(ATTRIBUTE_USER_ID);
    }

    public Map getAttributes() {
        return attributes;
    }

    public String getString(String attributeName) {
        return Optional.ofNullable(attributes.get(attributeName))
                .map(Object::toString)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(attributeName));
    }

    public Integer getInteger(String attributeName) {
        String value = getString(attributeName);
        return Optional.ofNullable(Ints.tryParse(value))
                .orElseThrow(() -> StorageMessages.INSTANCE.invalidContextAttributeType(
                        attributeName, value, Integer.class.getTypeName()));
    }
}
