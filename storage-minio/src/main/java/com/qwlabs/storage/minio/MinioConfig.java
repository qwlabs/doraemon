package com.qwlabs.storage.minio;

import com.qwlabs.lang.S2;
import com.qwlabs.storage.messages.Messages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MinioConfig {
    private final String url;
    private final String accessKey;
    private final String secretKey;

    public void validate() {
        if (S2.isBlank(url)) {
            throw Messages.INSTANCE.lostConfig("url");
        }
        if (S2.isBlank(accessKey)) {
            throw Messages.INSTANCE.lostConfig("accessKey");
        }
        if (S2.isBlank(secretKey)) {
            throw Messages.INSTANCE.lostConfig("secretKey");
        }
    }
}
