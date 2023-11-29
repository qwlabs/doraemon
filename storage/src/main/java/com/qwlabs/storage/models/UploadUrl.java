package com.qwlabs.storage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UploadUrl {
    private final String provider;
    private final String bucket;
    private final String objectName;
    private final String url;
}
