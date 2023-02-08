package com.qwlabs.storage.graphql.models;

import com.qwlabs.storage.models.CompleteUploadCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.graphql.Input;



@Input("CompleteUploadInput")
@Getter
@Setter
public class CompleteUploadInput {
    @NotNull
    private String provider;
    @NotNull
    private String bucket;
    @NotNull
    private String objectName;
    @NotNull
    private String uploadId;
    @NotNull
    private Integer partCount;
    private String name;

    public CompleteUploadCommand of() {
        return CompleteUploadCommand.builder()
            .provider(provider)
            .bucket(bucket)
            .objectName(objectName)
            .partCount(partCount)
            .uploadId(uploadId)
            .name(name)
            .build();
    }
}
