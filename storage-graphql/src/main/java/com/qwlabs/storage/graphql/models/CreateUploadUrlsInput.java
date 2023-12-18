package com.qwlabs.storage.graphql.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qwlabs.jackson.Jackson;
import com.qwlabs.storage.messages.Messages;
import com.qwlabs.storage.services.StorageContext;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.graphql.Input;

import java.util.Map;
import java.util.Objects;

@Input("CreateUploadUrlsInput")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUploadUrlsInput {
    private static TypeReference<Map<String, Object>> CONTENT_TYPE = new TypeReference<>() {
    };
    @NotNull
    private String businessType;

    //       TODO use Variables
    @Nullable
    private Object context;

    public StorageContext of() {
        return new StorageContext(businessType, ofContext());
    }

    private Map<String, Object> ofContext() {
        if (Objects.isNull(context)) {
            return null;
        }
        if (context instanceof Map) {
            return (Map<String, Object>) context;
        }
        if (context instanceof String) {
            return Jackson.read((String) context, CONTENT_TYPE)
                    .orElseThrow(() -> Messages.INSTANCE.invalidContent((String) context));
        }
        throw Messages.INSTANCE.invalidContent(context.toString());
    }
}
