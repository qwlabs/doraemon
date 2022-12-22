package com.qwlabs.storage.s3;


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

import java.io.InputStream;
import java.util.function.Function;

public class S3StorageEngine implements StorageEngine, Dispatchable<String> {
    public static final String PROVIDER = "S3";
    private final CustomS3Client s3Client;
    private final Function<String, Boolean> dispatchable;

    public S3StorageEngine(CustomS3Client s3Client,
                           Function<String, Boolean> dispatchable) {
        this.s3Client = s3Client;
        this.dispatchable = dispatchable;
    }

    //TODO
    @Override
    public UploadUrls createUploadUrls(GetUploadUrlsCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "createUploadUrls");
    }

    //TODO
    @Override
    public String completeUpload(CompleteUploadCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "completeUpload");
    }

    //TODO
    @Override
    public InputStream getObject(GetObjectCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "getObject");
    }

    //TODO
    @Override
    public StorageObject putObject(PutObjectCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "putObject");
    }

    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return this.s3Client.createDownloadUrl(command.getBucket(), command.getObjectName());
    }


    @Override
    public boolean dispatchable(String context) {
        return dispatchable.apply(context);
    }
}
