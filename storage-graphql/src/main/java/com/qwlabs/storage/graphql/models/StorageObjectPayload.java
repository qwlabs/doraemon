package com.qwlabs.storage.graphql.models;

import com.qwlabs.storage.models.StorageObject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.eclipse.microprofile.graphql.Type;


@Type("StorageObject")
@Builder
@Getter
@AllArgsConstructor
public class StorageObjectPayload {
    private final String provider;
    @NotNull
    private final String bucket;
    @NotNull
    private final String objectName;
    private final String name;

    public static StorageObjectPayload of(StorageObject storageObject) {
        if (storageObject == null) {
            return null;
        }
        return StorageObjectPayload.builder()
            .provider(storageObject.getProvider())
            .bucket(storageObject.getBucket())
            .objectName(storageObject.getObjectName())
            .name(storageObject.getName())
            .build();
    }
}
