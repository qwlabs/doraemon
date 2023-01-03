package com.qwlabs.storage.url;


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
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.util.Objects;

@ApplicationScoped
public class UrlStorageEngine implements StorageEngine, Dispatchable<String> {
        private static final String PROVIDER = "url";

    @Override
    public UploadUrls createUploadUrls(GetUploadUrlsCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "upload");
    }

    @Override
    public String completeUpload(CompleteUploadCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "upload");
    }

    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return String.format("%s%s", command.getBucket(), command.getObjectName());
    }

    @Override
    public InputStream getObject(GetObjectCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "download");
    }

    @Override
    public StorageObject putObject(PutObjectCommand command) {
        throw StorageMessages.INSTANCE.notSupported(PROVIDER, "putObject");
    }

    @Override
    public boolean dispatchable(@Nullable String context) {
        return Objects.equals(PROVIDER, context);
    }
}
