package com.qwlabs.storage.graphql.loaders;

import com.qwlabs.lang.C2;
import com.qwlabs.storage.StorageService;
import com.qwlabs.storage.graphql.models.StorageObjectPayload;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;

@GraphQLApi
public class StorageObjectPayloadLoader {

    private final StorageService storageService;

    @Inject
    public StorageObjectPayloadLoader(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<String> url(@Source List<StorageObjectPayload> storageObjects) {
        return C2.list(storageObjects, storageObject ->
                storageService.getDownloadUrl(storageObject.getProvider(),
                        storageObject.getBucket(), storageObject.getObjectName()));
    }
}
