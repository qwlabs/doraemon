package com.qwlabs.storage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUploadUrlsCommand {
    private final String provider;
    private final String bucket;
    private final String objectName;
    private final Integer partCount;
    private final String contentType;
}
