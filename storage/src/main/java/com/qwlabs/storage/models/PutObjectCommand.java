package com.qwlabs.storage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PutObjectCommand {
    private final String provider;
    private final String bucket;
    private final String objectName;
    private final InputStream inputStream;
}
