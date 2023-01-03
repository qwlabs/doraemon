package com.qwlabs.storage.minio;


import com.google.common.collect.Lists;
import com.qwlabs.cdi.Dispatchable;
import com.qwlabs.storage.messages.StorageMessages;
import com.qwlabs.storage.models.CompleteUploadCommand;
import com.qwlabs.storage.models.GetDownloadUrlCommand;
import com.qwlabs.storage.models.GetObjectCommand;
import com.qwlabs.storage.models.GetUploadUrlsCommand;
import com.qwlabs.storage.models.PutObjectCommand;
import com.qwlabs.storage.models.StorageObject;
import com.qwlabs.storage.models.UploadUrls;
import com.qwlabs.storage.services.StorageEngine;
import io.minio.messages.ListPartsResult;
import io.minio.messages.Part;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public class MinioStorageEngine implements StorageEngine, Dispatchable<String> {
    private final CustomMinioClient minioClient;
    private final Function<String, Boolean> dispatchable;

    public MinioStorageEngine(CustomMinioClient minioClient,
                              Function<String, Boolean> dispatchable) {
        this.minioClient = minioClient;
        this.dispatchable = dispatchable;
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
        return new UploadUrls(command.getProvider(), command.getBucket(),
                command.getObjectName(), uploadId, urls);
    }

    @Override
    public String completeUpload(CompleteUploadCommand command) {
        ListPartsResult result = minioClient.listParts(
                command.getBucket(), command.getObjectName(), command.getUploadId());
        if (result.partList().size() != command.getPartCount()) {
            throw StorageMessages.INSTANCE.invalidPartCount(command.getPartCount(),
                    result.partList().size());
        }
        List<Part> parts = result.partList();
        minioClient.completeUpload(command.getBucket(), command.getObjectName(),
                command.getUploadId(), parts);
        return minioClient.createDownloadUrl(command.getBucket(), command.getObjectName());
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
        return minioClient.putObject(command.getBucket(), command.getObjectName(), command.getInputStream());
    }

    private void setupBucket(String bucket) {
        if (!minioClient.bucketExists(bucket)) {
            minioClient.makeBucket(bucket);
        }
    }

    @Override
    public boolean dispatchable(@Nullable String context) {
        return dispatchable.apply(context);
    }
}
