package com.qwlabs.storage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CompleteUploadCommand {
    private final String provider;
    private final String bucket;
    private final String objectName;
    private final String name;
    private final String uploadId;
    private final Integer partCount;
}
