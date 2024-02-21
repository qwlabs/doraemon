package com.qwlabs.storage.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class StorageObject implements Serializable {
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

    @JsonIgnore
    public boolean isValid() {
        return Objects.nonNull(bucket) && Objects.nonNull(objectName);
    }

    @JsonIgnore
    public StorageObject validObject() {
        return isValid() ? this : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageObject that = (StorageObject) o;
        return Objects.equals(provider, that.provider)
            && Objects.equals(bucket, that.bucket)
            && Objects.equals(objectName, that.objectName)
            && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, bucket, objectName, name);
    }
}
