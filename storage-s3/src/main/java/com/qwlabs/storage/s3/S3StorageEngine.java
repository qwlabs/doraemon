package com.qwlabs.storage.s3;


import com.qwlabs.cdi.dispatch.Dispatchable;
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
import jakarta.annotation.Nullable;

import java.io.InputStream;
import java.util.Objects;


public class S3StorageEngine implements StorageEngine, Dispatchable<String> {
    private static final String PROVIDER = "s3";
    protected final CustomS3Client s3Client;

    public S3StorageEngine(CustomS3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public UploadUrl createUploadUrl(GetUploadUrlCommand command) {
        return StorageEngine.super.createUploadUrl(command);
    }

    @Override
    public UploadUrls createUploadUrls(GetUploadUrlsCommand command) {
        return StorageEngine.super.createUploadUrls(command);
    }

    @Override
    public String completeUpload(CompleteUploadCommand command) {
        return StorageEngine.super.completeUpload(command);
    }

    @Override
    public InputStream getObject(GetObjectCommand command) {
        return StorageEngine.super.getObject(command);
    }

    @Override
    public StorageObject putObject(PutObjectCommand command) {
        return StorageEngine.super.putObject(command);
    }

    @Override
    public String putObjectForUrl(PutObjectCommand command) {
        return StorageEngine.super.putObjectForUrl(command);
    }

    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return this.s3Client.createDownloadUrl(command.getBucket(), command.getObjectName());
    }

    @Override
    public boolean dispatchable(@Nullable String context) {
        return Objects.equals(PROVIDER, context);
    }
}
