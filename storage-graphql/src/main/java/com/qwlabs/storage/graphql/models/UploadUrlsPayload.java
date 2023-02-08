package com.qwlabs.storage.graphql.models;

import com.qwlabs.storage.models.UploadUrls;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.eclipse.microprofile.graphql.Type;

import java.util.List;

@Type("UploadUrls")
@Builder
@Getter
@AllArgsConstructor
public class UploadUrlsPayload {
    @NotNull
    private final String provider;
    @NotNull
    private final String bucket;
    @NotNull
    private final String objectName;
    @NotNull
    private final String uploadId;
    @NotNull
    private final List<@NotNull String> urls;

    public static UploadUrlsPayload of(UploadUrls entity) {
        return UploadUrlsPayload.builder()
            .provider(entity.getProvider())
            .bucket(entity.getBucket())
            .objectName(entity.getObjectName())
            .uploadId(entity.getUploadId())
            .urls(entity.getUrls())
            .build();
    }
}