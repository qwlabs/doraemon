package com.qwlabs.storage.minio;

import com.google.common.collect.HashMultimap;
import com.qwlabs.storage.exceptions.StorageException;
import com.qwlabs.storage.models.StorageObject;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.ListPartsResult;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class CustomMinioClient extends MinioAsyncClient {

    private static final int UPLOAD_EXPIRES_IN_DAYS = 1;
    private static final int DOWNLOAD_EXPIRES_IN_DAYS = 1;
    private static final int MAX_PART_COUNT = 1000;
    private static final int STREAM_PART_DEFAULT_SIZE = -1;

    public CustomMinioClient(MinioAsyncClient client) {
        super(client);
    }

    protected String createUploadId(String bucket, String objectName, String contentType) {
        HashMultimap<String, String> headers = HashMultimap.create();
        headers.put("Content-Type", contentType);
        try {
            var response = super.createMultipartUpload(bucket, null,
                objectName, headers, null);
            return response.result().uploadId();
        } catch (NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException
            | ServerException | XmlParserException | ErrorResponseException | InternalException
            | InvalidResponseException e) {
            LOGGER.warn("Can not create upload id.", e);
            throw new StorageException("Can not create upload id.", e);
        }
    }

    protected String createUploadUrl(String bucket, String objectName, String uploadId, Integer partNumber) {
        Map<String, String> queryParams = Map.of("uploadId", uploadId, "partNumber", partNumber.toString());
        var args = GetPresignedObjectUrlArgs.builder()
            .method(Method.PUT)
            .expiry(UPLOAD_EXPIRES_IN_DAYS, TimeUnit.DAYS)
            .bucket(bucket)
            .object(objectName)
            .extraQueryParams(queryParams)
            .build();
        try {
            return this.getPresignedObjectUrl(args);
        } catch (NoSuchAlgorithmException | InsufficientDataException | IOException | InvalidKeyException
            | ServerException | XmlParserException | ErrorResponseException | InternalException
            | InvalidResponseException e) {
            LOGGER.warn("Can not create upload url.", e);
            throw new StorageException("Can not create upload url.", e);
        }
    }

    public String createDownloadUrl(String bucket, String objectName) {
        var args = GetPresignedObjectUrlArgs.builder()
            .method(Method.GET)
            .expiry(DOWNLOAD_EXPIRES_IN_DAYS, TimeUnit.DAYS)
            .bucket(bucket)
            .object(objectName)
            .build();
        try {
            return this.getPresignedObjectUrl(args);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException
            | InvalidResponseException | IOException | NoSuchAlgorithmException | XmlParserException
            | ServerException e) {
            LOGGER.warn("Can not get download url.", e);
            throw new StorageException("Can not create download url.", e);
        }
    }

    public boolean bucketExists(String bucket) {
        var args = BucketExistsArgs.builder().bucket(bucket).build();
        try {
            return this.bucketExists(args).get();
        } catch (InsufficientDataException | InternalException | InvalidKeyException
            | IOException | NoSuchAlgorithmException
            | XmlParserException | InterruptedException | ExecutionException e) {
            LOGGER.warn("Can not check bucket exists.", e);
            throw new StorageException(e);
        }
    }

    public void makeBucket(String bucket) {
        MakeBucketArgs args = MakeBucketArgs.builder()
            .bucket(bucket)
            .build();
        try {
            this.makeBucket(args).get();
        } catch (InsufficientDataException | InternalException | InvalidKeyException
            | IOException | NoSuchAlgorithmException
            | XmlParserException | InterruptedException | ExecutionException e) {
            LOGGER.warn("Can not make bucket.", e);
            throw new StorageException("Can not make bucket.", e);
        }
    }

    public ListPartsResult listParts(String bucket, String objectName, String uploadId) {
        try {
            return this.listPartsAsync(bucket, null, objectName, MAX_PART_COUNT, null,
                uploadId, null, null)
                .get()
                .result();
        } catch (InsufficientDataException | InternalException | InvalidKeyException
            | NoSuchAlgorithmException | XmlParserException | InterruptedException
            | ExecutionException | IOException e) {
            LOGGER.warn("Can not list multipart uploads.", e);
            throw new StorageException("Can not list multipart uploads.", e);
        }
    }

    public ObjectWriteResponse completeUpload(String bucket, String objectName,
                                              String uploadId, List<Part> parts) {
        try {
            return this.completeMultipartUploadAsync(bucket, null, objectName, uploadId,
                parts.toArray(new Part[]{}), null, null)
                .get();
        } catch (InsufficientDataException | InternalException | InvalidKeyException
            | IOException | NoSuchAlgorithmException
            | XmlParserException | InterruptedException | ExecutionException e) {
            LOGGER.warn("Can not complete multipart upload.", e);
            throw new StorageException("Can not complete multipart upload.", e);
        }
    }

    public GetObjectResponse getObject(String bucket, String objectName) {
        try {
            CompletableFuture<GetObjectResponse> future = this.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            return future.get();
        } catch (InsufficientDataException | InternalException | InvalidKeyException
            | IOException | NoSuchAlgorithmException
            | XmlParserException | InterruptedException | ExecutionException e) {
            LOGGER.warn("Can not get object", e);
            throw new StorageException("Can not get object", e);
        }
    }

    public StorageObject putObject(String bucket, String objectName, InputStream stream) {
        try {
            CompletableFuture<ObjectWriteResponse> future = this.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(stream, stream.available(), STREAM_PART_DEFAULT_SIZE)
                    .build());
            ObjectWriteResponse result = future.get();
            return StorageObject.of(result.bucket(), result.object());
        } catch (InsufficientDataException | InternalException | InvalidKeyException
            | IOException | NoSuchAlgorithmException
            | XmlParserException | InterruptedException | ExecutionException e) {
            LOGGER.warn("Can not get object", e);
            throw new StorageException("Can not get object", e);
        }
    }
}
