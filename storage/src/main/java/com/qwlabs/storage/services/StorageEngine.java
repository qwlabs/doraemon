package com.qwlabs.storage.services;


import com.qwlabs.storage.models.CompleteUploadCommand;
import com.qwlabs.storage.models.GetDownloadUrlCommand;
import com.qwlabs.storage.models.GetObjectCommand;
import com.qwlabs.storage.models.GetUploadUrlsCommand;
import com.qwlabs.storage.models.PutObjectCommand;
import com.qwlabs.storage.models.StorageObject;
import com.qwlabs.storage.models.UploadUrls;

import java.io.InputStream;

public interface StorageEngine {

    UploadUrls createUploadUrls(GetUploadUrlsCommand command);

    String completeUpload(CompleteUploadCommand command);

    String getDownloadUrl(GetDownloadUrlCommand command);

    InputStream getObject(GetObjectCommand command);

    StorageObject putObject(PutObjectCommand command);

    default String putObjectForUrl(PutObjectCommand command) {
        return getDownloadUrl(GetDownloadUrlCommand.builder()
            .bucket(command.getBucket())
            .objectName(putObject(command).getObjectName())
            .build());
    }
}
