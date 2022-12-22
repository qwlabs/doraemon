package com.qwlabs.storage.minio;

import io.minio.MinioAsyncClient;

public abstract class MinioStorageBootstrap {

    protected MinioAsyncClient createMinioClient(MinioConfig config) {
        config.validate();
        return MinioAsyncClient.builder()
                .endpoint(config.getUrl())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }
}
