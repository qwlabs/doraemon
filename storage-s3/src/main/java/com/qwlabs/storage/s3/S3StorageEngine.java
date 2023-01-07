package com.qwlabs.storage.s3;


import com.qwlabs.cdi.Dispatchable;
import com.qwlabs.storage.models.GetDownloadUrlCommand;
import com.qwlabs.storage.services.StorageEngine;


public abstract class S3StorageEngine implements StorageEngine, Dispatchable<String> {
    protected final CustomS3Client s3Client;

    public S3StorageEngine(CustomS3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return this.s3Client.createDownloadUrl(command.getBucket(), command.getObjectName());
    }
}
