package com.qwlabs.storage.s3;

import com.qwlabs.storage.exceptions.StorageException;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Slf4j
public class CustomS3Client {
    private static final int DOWNLOAD_EXPIRES_IN_DAYS = 1;
    private final S3Client syncClient;
    private final S3AsyncClient asyncClient;
    private final S3Presigner presigner;

    public CustomS3Client(S3Client syncClient, S3AsyncClient asyncClient, S3Presigner presigner) {
        this.syncClient = syncClient;
        this.asyncClient = asyncClient;
        this.presigner = presigner;
    }

    public boolean exist(String bucket, String objectName) {
        var request = HeadObjectRequest.builder()
                .bucket(bucket).key(objectName).build();
        try {
            var response = syncClient.headObject(request);
            return response.contentLength() > 0;
        } catch (NoSuchBucketException | NoSuchKeyException e) {
            LOGGER.info("bucket={}, objectName={} not exist.", bucket, objectName);
            return false;
        } catch (Throwable e) {
            LOGGER.warn("Can not check object exist.", e);
            throw new StorageException("Can not check object exist.", e);
        }
    }

    public String createDownloadUrl(String bucket, String objectName) {
        var getRequest = GetObjectRequest.builder().bucket(bucket).key(objectName).build();
        var request = GetObjectPresignRequest.builder()
                .getObjectRequest(getRequest)
                .signatureDuration(Duration.ofDays(DOWNLOAD_EXPIRES_IN_DAYS))
                .build();
        try {
            return this.presigner.presignGetObject(request).url().toString();
        } catch (Exception e) {
            LOGGER.warn("Can not create upload url.", e);
            throw new StorageException("Can not create upload url.", e);
        }
    }
}
