package com.qwlabs.storage.s3;


import com.google.common.collect.Lists;
import com.qwlabs.cdi.Dispatchable;
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
import software.amazon.awssdk.services.s3.model.ListPartsResponse;
import software.amazon.awssdk.services.s3.model.Part;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class S3StorageEngine implements StorageEngine, Dispatchable<String> {
    private static final String PROVIDER = "s3";
    protected final CustomS3Client s3Client;

    public S3StorageEngine(CustomS3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public UploadUrl createUploadUrl(GetUploadUrlCommand command) {
        setupBucket(command.getBucket());
        var url = s3Client.createUploadUrl(command.getBucket(), command.getObjectName());
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
        String uploadId = s3Client.createUploadId(command.getBucket(),
            command.getObjectName(), command.getContentType());
        List<String> urls = Lists.newArrayList();
        for (int partNumber = 1; partNumber <= command.getPartCount(); partNumber++) {
            urls.add(s3Client.createUploadUrl(command.getBucket(), command.getObjectName(), uploadId, partNumber));
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
        ListPartsResponse result = s3Client.listParts(command.getBucket(), command.getObjectName(), command.getUploadId());
        if (result.parts().size() != command.getPartCount()) {
            throw Messages.INSTANCE.invalidPartCount(command.getPartCount(),
                result.parts().size());
        }
        List<Part> parts = result.parts();
        s3Client.completeUpload(command.getBucket(), command.getObjectName(), command.getUploadId(), parts);
        return StorageObject.builder()
            .provider(PROVIDER)
            .bucket(command.getBucket())
            .objectName(command.getObjectName())
            .name(command.getName())
            .build();
    }


    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return s3Client.createDownloadUrl(command.getBucket(), command.getObjectName());
    }

    @Override
    public InputStream getObject(GetObjectCommand command) {
        return s3Client.getObject(command.getBucket(), command.getObjectName());
    }

    @Override
    public StorageObject putObject(PutObjectCommand command) {
        setupBucket(command.getBucket());
        return s3Client.putObject(command);
    }

    private void setupBucket(String bucket) {
        if (!s3Client.bucketExists(bucket)) {
            s3Client.makeBucket(bucket);
        }
    }

    @Override
    public boolean dispatchable(String context) {
        return Objects.equals(PROVIDER, context);
    }
}
