package com.qwlabs.storage.graphql.models;

import com.qwlabs.storage.services.StorageContext;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.graphql.Input;

import java.util.Map;

@Input("CreateUploadUrlsInput")
@Getter
@Setter
public class CreateUploadUrlsInput {
    @NotNull
    private String businessType;

    private Map<String, String> context;


    public StorageContext of() {
        return new StorageContext(businessType, context);
    }
}
