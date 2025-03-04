package com.qwlabs.storage.services;


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

import java.io.InputStream;

public interface StorageEngine {

    default UploadUrl createUploadUrl(GetUploadUrlCommand command) {
        throw Messages.INSTANCE.notImplemented();
    }

    default UploadUrls createUploadUrls(GetUploadUrlsCommand command) {
        throw Messages.INSTANCE.notImplemented();
    }

    default StorageObject completeUpload(CompleteUploadCommand command) {
        throw Messages.INSTANCE.notImplemented();
    }

    default String getDownloadUrl(GetDownloadUrlCommand command) {
        throw Messages.INSTANCE.notImplemented();
    }

    default InputStream getObject(GetObjectCommand command) {
        throw Messages.INSTANCE.notImplemented();
    }

    default StorageObject putObject(PutObjectCommand command) {
        throw Messages.INSTANCE.notImplemented();
    }

    default String putObjectForUrl(PutObjectCommand command) {
        return getDownloadUrl(GetDownloadUrlCommand.builder()
            .bucket(command.getBucket())
            .objectName(putObject(command).getObjectName())
            .build());
    }
}
