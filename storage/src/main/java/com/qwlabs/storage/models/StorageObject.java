package com.qwlabs.storage.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StorageObject {
    private String provider;
    @NotNull
    private String bucket;
    @NotNull
    private String objectName;

    private String name;

    public static StorageObject of(String bucket, String objectName) {
        return of(bucket, objectName, null);
    }

    public static StorageObject of(String bucket, String objectName, String provider) {
        return of(bucket, objectName, provider, null);
    }

    public static StorageObject of(String bucket, String objectName, String provider, String name) {
        StorageObject storageObject = new StorageObject();
        storageObject.setBucket(bucket);
        storageObject.setObjectName(objectName);
        storageObject.setProvider(provider);
        storageObject.setName(name);
        return storageObject;
    }
}
