package com.qwlabs.storage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetDownloadUrlCommand {
    private final String provider;
    private final String bucket;
    private final String objectName;
}
