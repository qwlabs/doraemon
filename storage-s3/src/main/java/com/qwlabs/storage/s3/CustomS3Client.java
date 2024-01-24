package com.qwlabs.storage.s3;

import com.qwlabs.storage.exceptions.StorageException;
import com.qwlabs.storage.models.StorageObject;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListPartsRequest;
import software.amazon.awssdk.services.s3.model.ListPartsResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.Part;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class CustomS3Client {
    private static final int UPLOAD_EXPIRES_IN_DAYS = 1;
    private static final int DOWNLOAD_EXPIRES_IN_DAYS = 1;
    private final S3Client syncClient;
    private final S3AsyncClient asyncClient;
    private final S3Presigner presigner;
    private static final int MAX_PART_COUNT = 1000;

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
            LOGGER.error("bucket={}, objectName={} not exist.", bucket, objectName);
            return false;
        } catch (Throwable e) {
            LOGGER.error("Can not check object exist.", e);
            throw new StorageException("Can not check object exist.", e);
        }
    }

    protected String createUploadId(String bucket, String objectName, String contentType) {
        try {
            CreateMultipartUploadRequest request = CreateMultipartUploadRequest.builder()
                .bucket(bucket)
                .contentType(contentType)
                .key(objectName)
                .build();
            var response = asyncClient.createMultipartUpload(request);
            return response.get().uploadId();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Can not create upload id.", e);
            throw new StorageException("Can not create upload id.", e);
        }
    }

    protected String createUploadUrl(String bucket, String objectName) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .build();
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(UPLOAD_EXPIRES_IN_DAYS))
                .putObjectRequest(objectRequest)
                .build();
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toString();
        } catch (S3Exception e) {
            throw new StorageException("Can not create upload url.", e);
        }
    }

    protected String createUploadUrl(String bucket, String objectName, String uploadId, Integer partNumber) {
        try {
            UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .build();
            UploadPartPresignRequest uploadPartPresignRequest = UploadPartPresignRequest.builder()
                .signatureDuration(Duration.ofDays(UPLOAD_EXPIRES_IN_DAYS))
                .uploadPartRequest(uploadPartRequest)
                .build();
            var presignedRequest = presigner.presignUploadPart(uploadPartPresignRequest);
            return presignedRequest.url().toString();
        } catch (S3Exception e) {
            throw new StorageException("Can not create upload url.", e);
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
        } catch (S3Exception e) {
            LOGGER.error("Can not create upload url.", e);
            throw new StorageException("Can not create upload url.", e);
        }
    }

    public boolean bucketExists(String bucket) {
        HeadBucketRequest request = HeadBucketRequest.builder().bucket(bucket).build();
        try {
            syncClient.headBucket(request);
            return true;
        } catch (S3Exception e) {
            LOGGER.error("Can not check object exist.", e);
            return false;

        }
    }

    public void makeBucket(String bucket) {
        CreateBucketRequest request = CreateBucketRequest.builder()
            .bucket(bucket)
            .build();
        try {
            syncClient.createBucket(request);
        } catch (S3Exception e) {
            LOGGER.error("Can not make bucket.", e);
            throw new StorageException("Can not make bucket.", e);
        }
    }

    public ListPartsResponse listParts(String bucket, String objectName, String uploadId) {
        try {
            ListPartsRequest request = ListPartsRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .uploadId(uploadId)
                .maxParts(MAX_PART_COUNT)
                .build();
            return syncClient.listParts(request);
        } catch (S3Exception e) {
            LOGGER.error("Can not list multipart uploads.", e);
            throw new StorageException("Can not list multipart uploads.", e);
        }
    }

    public CompleteMultipartUploadResponse completeUpload(String bucket,
                                                          String objectName,
                                                          String uploadId,
                                                          List<Part> parts) {
        List<CompletedPart> completedParts = parts.stream().map(
            part -> CompletedPart.builder()
                .eTag(part.eTag())
                .partNumber(part.partNumber())
                .build()
        ).toList();
        try {
            CompletedMultipartUpload upload = CompletedMultipartUpload.builder()
                .parts(completedParts)
                .build();
            CompleteMultipartUploadRequest request = CompleteMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .multipartUpload(upload)
                .uploadId(uploadId)
                .build();
            return syncClient.completeMultipartUpload(request);
        } catch (S3Exception e) {
            LOGGER.error("Can not complete multipart upload.", e);
            throw new StorageException("Can not complete multipart upload.", e);
        }
    }

    public ResponseInputStream<GetObjectResponse> getObject(String bucket, String objectName) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .build();
            return syncClient.getObject(request);
        } catch (S3Exception e) {
            LOGGER.error("Can not get object", e);
            throw new StorageException("Can not get object", e);
        }
    }

    public StorageObject putObject(String bucket, String objectName, InputStream stream) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .build();
            RequestBody requestBody = RequestBody.fromInputStream(stream, stream.available());
            syncClient.putObject(request, requestBody);
            return StorageObject.of(bucket, objectName);
        } catch (S3Exception | IOException e) {
            LOGGER.error("Can not get object", e);
            throw new StorageException("Can not get object", e);
        }
    }

}
