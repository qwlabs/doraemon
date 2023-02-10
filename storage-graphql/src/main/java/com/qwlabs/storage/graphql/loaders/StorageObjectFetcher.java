package com.qwlabs.storage.graphql.loaders;

import com.qwlabs.lang.C2;
import com.qwlabs.storage.graphql.models.StorageObjectPayload;
import com.qwlabs.storage.models.StorageObject;
import io.smallrye.graphql.api.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class StorageObjectFetcher {

    @NotNull
    public <S> List<@Nullable StorageObjectPayload> fetchSingle(List<S> sources, Function<S, StorageObject> loadFun) {
        if (C2.isEmpty(sources)) {
            return List.of();
        }
        return C2.stream(sources)
                .map(loadFun)
                .map(StorageObject::validObject)
                .map(StorageObjectPayload::of)
                .collect(Collectors.toList());
    }

    @NotNull
    public <S> List<@NotNull List<@NotNull StorageObjectPayload>> fetchMulti(List<S> sources, Function<S, List<StorageObject>> loadFun) {
        if (C2.isEmpty(sources)) {
            return List.of();
        }
        return C2.stream(sources)
                .map(loadFun)
                .map(storageObjects ->
                        C2.stream(storageObjects)
                                .map(StorageObject::validObject)
                                .filter(Objects::nonNull)
                                .map(StorageObjectPayload::of)
                                .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }
}
