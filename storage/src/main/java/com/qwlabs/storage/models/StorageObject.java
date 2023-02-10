package com.qwlabs.storage.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    public boolean isValid() {
        return Objects.nonNull(bucket) && Objects.nonNull(objectName);
    }

    public StorageObject validObject() {
        return isValid() ? this : null;
    }
}
