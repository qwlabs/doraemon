package com.qwlabs.storage.minio;

import io.minio.MinioAsyncClient;

public final class MinioStorageHelper {
    private MinioStorageHelper() {
    }

    public static MinioAsyncClient createMinioClient(MinioConfig config) {
        config.validate();
        return MinioAsyncClient.builder()
                .endpoint(config.getUrl())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }
}
