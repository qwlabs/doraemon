package com.qwlabs.storage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class StoragePlan {
    private final String provider;
    private final String bucket;
    private final String objectName;
}
