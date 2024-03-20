package com.qwlabs.storage.services;

import com.qwlabs.cdi.dispatch.DispatchInstance;
import com.qwlabs.cdi.Primary;
import com.qwlabs.storage.messages.StorageMessages;
import com.qwlabs.storage.models.CompleteUploadCommand;
import com.qwlabs.storage.models.GetDownloadUrlCommand;
import com.qwlabs.storage.models.GetObjectCommand;
import com.qwlabs.storage.models.GetUploadUrlsCommand;
import com.qwlabs.storage.models.PutObjectCommand;
import com.qwlabs.storage.models.StorageObject;
import com.qwlabs.storage.models.UploadUrls;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.io.InputStream;

@Primary
@ApplicationScoped
public class StorageEngineDispatcher implements StorageEngine {

    private final DispatchInstance<String, StorageEngine> dispatcher;

    @Inject
    public StorageEngineDispatcher(Instance<StorageEngine> instance) {
        this.dispatcher = DispatchInstance.of(instance, true);
    }

    private StorageEngine get(String provider) {
        return dispatcher.getOptional(provider)
                .orElseThrow(() -> StorageMessages.INSTANCE.notFoundStorageService(provider));
    }

    @Override
    public UploadUrls createUploadUrls(GetUploadUrlsCommand command) {
        return get(command.getProvider()).createUploadUrls(command);
    }

    @Override
    public StorageObject completeUpload(CompleteUploadCommand command) {
        return get(command.getProvider()).completeUpload(command);
    }

    @Override
    public String getDownloadUrl(GetDownloadUrlCommand command) {
        return dispatcher.getOptional(command.getProvider())
                .map(service -> service.getDownloadUrl(command))
                .orElse(null);
    }

    @Override
    public InputStream getObject(GetObjectCommand command) {
        return dispatcher.getOptional(command.getProvider())
                .map(service -> service.getObject(command))
                .orElse(null);
    }

    @Override
    public StorageObject putObject(PutObjectCommand command) {
        return dispatcher.getOptional(command.getProvider())
                .map(service -> service.putObject(command))
                .orElse(null);
    }
}
