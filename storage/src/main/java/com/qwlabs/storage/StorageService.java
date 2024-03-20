package com.qwlabs.storage;

import com.qwlabs.cdi.Primary;
import com.qwlabs.storage.models.CompleteUploadCommand;
import com.qwlabs.storage.models.GetDownloadUrlCommand;
import com.qwlabs.storage.models.GetObjectCommand;
import com.qwlabs.storage.models.GetUploadUrlCommand;
import com.qwlabs.storage.models.GetUploadUrlsCommand;
import com.qwlabs.storage.models.PutObjectCommand;
import com.qwlabs.storage.models.StorageObject;
import com.qwlabs.storage.models.StoragePlan;
import com.qwlabs.storage.models.UploadUrl;
import com.qwlabs.storage.models.UploadUrls;
import com.qwlabs.storage.services.StorageContext;
import com.qwlabs.storage.services.StorageEngine;
import com.qwlabs.storage.services.StoragePlanner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.io.InputStream;

@ApplicationScoped
public class StorageService {
    private final Instance<StoragePlanner> storagePlanner;
    private final Instance<StorageEngine> storageEngines;

    @Inject
    public StorageService(@Primary Instance<StoragePlanner> storagePlanner,
                          @Primary Instance<StorageEngine> storageEngines) {
        this.storagePlanner = storagePlanner;
        this.storageEngines = storageEngines;
    }

    public UploadUrl createUploadUrl(StorageContext storageContext) {
        StoragePlan plan = storagePlanner.get().plan(storageContext);
        GetUploadUrlCommand command = GetUploadUrlCommand.builder()
            .provider(plan.getProvider())
            .bucket(plan.getBucket())
            .objectName(plan.getObjectName())
            .contentType(storageContext.getFileContentType())
            .build();
        StorageEngine engine = storageEngines.get();
        return engine.createUploadUrl(command);
    }

    public UploadUrls createUploadUrls(StorageContext storageContext) {
        StoragePlan plan = storagePlanner.get().plan(storageContext);
        var command = GetUploadUrlsCommand.builder()
            .provider(plan.getProvider())
            .bucket(plan.getBucket())
            .objectName(plan.getObjectName())
            .partCount(storageContext.getFilePartCount())
            .contentType(storageContext.getFileContentType())
            .build();
        StorageEngine engine = storageEngines.get();
        return engine.createUploadUrls(command);
    }

    public StorageObject completeUpload(CompleteUploadCommand command) {
        return storageEngines.get().completeUpload(command);
    }

    public String getDownloadUrl(String provider, String bucket, String objectName) {
        if (bucket == null || objectName == null) {
            return null;
        }
        return storageEngines.get().getDownloadUrl(new GetDownloadUrlCommand(provider, bucket, objectName));
    }

    public InputStream getObject(GetObjectCommand command) {
        return storageEngines.get().getObject(command);
    }

    public String putObject(PutObjectCommand command) {
        return storageEngines.get().putObjectForUrl(command);
    }
}
