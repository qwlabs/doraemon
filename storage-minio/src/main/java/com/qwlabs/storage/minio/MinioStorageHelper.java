package com.qwlabs.storage.minio;

import io.minio.MinioAsyncClient;

public final class MinioStorageHelper {
    private MinioStorageHelper() {
    }

    public static CustomMinioClient createMinioClient(MinioConfig config) {
        config.validate();
        return new CustomMinioClient(createAsyncMinioClient(config));
    }

    private static MinioAsyncClient createAsyncMinioClient(MinioConfig config) {
        return MinioAsyncClient.builder()
                .endpoint(config.getUrl())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
    }
}
