package com.qwlabs.storage.services;


import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.qwlabs.storage.messages.StorageMessages;

import java.util.Map;
import java.util.Optional;

public class StorageContext {
    private static final String ATTRIBUTE_FILE_NAME = "fileName";
    private static final String ATTRIBUTE_FILE_HASH = "fileHash";
    private static final String ATTRIBUTE_FILE_PART_COUNT = "filePartCount";
    private static final String ATTRIBUTE_FILE_CONTENT_TYPE = "fileContentType";
    private final String businessType;
    private final Map<String, String> attributes;

    public StorageContext(String businessType, Map<String, String> attributes) {
        this.businessType = businessType;
        this.attributes = Optional.ofNullable(attributes).orElseGet(Maps::newHashMap);
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getFileName() {
        return getAttribute(ATTRIBUTE_FILE_NAME)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_NAME));
    }

    public String getFileHash() {
        return getAttribute(ATTRIBUTE_FILE_HASH)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_HASH));
    }

    public String getFileContentType() {
        return getAttribute(ATTRIBUTE_FILE_CONTENT_TYPE)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_CONTENT_TYPE));
    }

    public Integer getFilePartCount() {
        return getInteger(ATTRIBUTE_FILE_PART_COUNT)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundContextAttribute(ATTRIBUTE_FILE_PART_COUNT));
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Optional<String> getAttribute(String attributeName) {
        return Optional.ofNullable(attributes.get(attributeName));
    }

    public Optional<Integer> getInteger(String attributeName) {
        return getAttribute(attributeName)
                .map(Ints::tryParse);
    }

    public Optional<Long> getLong(String attributeName) {
        return getAttribute(attributeName)
                .map(Longs::tryParse);
    }
}
