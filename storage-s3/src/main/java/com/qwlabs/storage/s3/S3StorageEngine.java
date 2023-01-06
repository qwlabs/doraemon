package com.qwlabs.storage.s3;


import com.qwlabs.cdi.Dispatchable;
import com.qwlabs.storage.models.GetDownloadUrlCommand;
import com.qwlabs.storage.services.StorageEngine;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

public class S3StorageEngine implements StorageEngine, Dispatchable<String> {
    private final Function<String, Boolean> dispatchable;
    protected final CustomS3Client s3Client;

    public S3StorageEngine(CustomS3Client s3Client,
                           Function<String, Boolean> dispatchable) {
        this.s3Client = s3Client;
        this.dispatchable = dispatchable;
    }

    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return this.s3Client.createDownloadUrl(command.getBucket(), command.getObjectName());
    }


    @Override
    public boolean dispatchable(@Nullable String context) {
        return dispatchable.apply(context);
    }
}
