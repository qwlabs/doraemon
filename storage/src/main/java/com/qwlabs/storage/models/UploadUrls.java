package com.qwlabs.storage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UploadUrls {
    private final String provider;
    private final String bucket;
    private final String objectName;
    private final String uploadId;
    private final List<String> urls;
}
