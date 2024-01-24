package com.qwlabs.storage.s3;

import com.qwlabs.lang.S2;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3BaseClientBuilder;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

public final class S3StorageHelper {
    private S3StorageHelper() {
    }

    public static CustomS3Client createS3Client(S3Config config) {
        return new CustomS3Client(
            createS3SyncClient(config),
            createS3AsyncClient(config),
            createS3Presigner(config)
        );
    }

    private static S3Client createS3SyncClient(S3Config config) {
        S3ClientBuilder builder = S3Client.builder();
        configure(builder, config);
        return builder.build();
    }

    private static S3AsyncClient createS3AsyncClient(S3Config config) {
        S3AsyncClientBuilder builder = S3AsyncClient.builder();
        configure(builder, config);
        return builder.build();
    }

    private static S3Presigner createS3Presigner(S3Config config) {
        S3Presigner.Builder builder = S3Presigner.builder();
        configure(builder, config);
        return builder.build();
    }

    private static void configure(S3Presigner.Builder builder,
                                  S3Config config) {
        config.validate();
        builder.credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(config.getAccessKey(), config.getSecretKey())));
        if (!S2.isBlank(config.getRegion())) {
            builder.region(Region.of(config.getRegion()));
        }
        builder.endpointOverride(URI.create(config.getUrl()));
    }

    private static void configure(S3BaseClientBuilder builder,
                                  S3Config config) {
        config.validate();
        builder.credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(config.getAccessKey(), config.getSecretKey())));
        if (!S2.isBlank(config.getRegion())) {
            builder.region(Region.of(config.getRegion()));
        }
        builder.endpointOverride(URI.create(config.getUrl()));
    }
}
