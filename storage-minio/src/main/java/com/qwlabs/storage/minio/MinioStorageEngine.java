package com.qwlabs.storage.minio;


import com.google.common.collect.Lists;
import com.qwlabs.cdi.dispatch.Dispatchable;
import com.qwlabs.storage.messages.Messages;
import com.qwlabs.storage.models.CompleteUploadCommand;
import com.qwlabs.storage.models.GetDownloadUrlCommand;
import com.qwlabs.storage.models.GetObjectCommand;
import com.qwlabs.storage.models.GetUploadUrlCommand;
import com.qwlabs.storage.models.GetUploadUrlsCommand;
import com.qwlabs.storage.models.PutObjectCommand;
import com.qwlabs.storage.models.StorageObject;
import com.qwlabs.storage.models.UploadUrl;
import com.qwlabs.storage.models.UploadUrls;
import com.qwlabs.storage.services.StorageEngine;
import io.minio.messages.ListPartsResult;
import io.minio.messages.Part;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class MinioStorageEngine implements StorageEngine, Dispatchable<String> {
    private static final String PROVIDER = "minio";
    protected final CustomMinioClient minioClient;

    public MinioStorageEngine(CustomMinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public UploadUrl createUploadUrl(GetUploadUrlCommand command) {
        setupBucket(command.getBucket());
        var url = minioClient.createUploadUrl(command.getBucket(), command.getObjectName());
        return UploadUrl.builder()
            .provider(command.getProvider())
            .bucket(command.getBucket())
            .objectName(command.getObjectName())
            .url(url)
            .build();
    }

    @Override
    public UploadUrls createUploadUrls(GetUploadUrlsCommand command) {
        setupBucket(command.getBucket());
        String uploadId = minioClient.createUploadId(command.getBucket(),
            command.getObjectName(), command.getContentType());
        List<String> urls = Lists.newArrayList();
        for (int partNumber = 1; partNumber <= command.getPartCount(); partNumber++) {
            urls.add(minioClient.createUploadUrl(command.getBucket(), command.getObjectName(),
                uploadId, partNumber));
        }
        return UploadUrls.builder()
            .provider(command.getProvider())
            .bucket(command.getBucket())
            .objectName(command.getObjectName())
            .uploadId(uploadId)
            .urls(urls)
            .build();
    }

    @Override
    public StorageObject completeUpload(CompleteUploadCommand command) {
        ListPartsResult result = minioClient.listParts(
            command.getBucket(), command.getObjectName(), command.getUploadId());
        if (result.partList().size() != command.getPartCount()) {
            throw Messages.INSTANCE.invalidPartCount(command.getPartCount(),
                result.partList().size());
        }
        List<Part> parts = result.partList();
        minioClient.completeUpload(command.getBucket(), command.getObjectName(),
            command.getUploadId(), parts);
        return StorageObject.builder()
            .provider(PROVIDER)
            .bucket(command.getBucket())
            .objectName(command.getObjectName())
            .name(command.getName())
            .build();
    }

    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return minioClient.createDownloadUrl(command.getBucket(), command.getObjectName());
    }

    @Override
    public InputStream getObject(GetObjectCommand command) {
        return minioClient.getObject(command.getBucket(), command.getObjectName());
    }

    @Override
    public StorageObject putObject(PutObjectCommand command) {
        setupBucket(command.getBucket());
        return minioClient.putObject(command);
    }

    private void setupBucket(String bucket) {
        if (!minioClient.bucketExists(bucket)) {
            minioClient.makeBucket(bucket);
        }
    }

    @Override
    public boolean dispatchable(@Nullable String context) {
        return Objects.equals(PROVIDER, context);
    }
}
