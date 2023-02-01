package com.qwlabs.graphql.builder;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.stream.Collectors;

public interface GqlFieldsAware<SELF> {

    default SELF fields(@NotNull Object @NotNull ... fields) {
        return fields(new GqlFields(Arrays.stream(fields)
                .map(field -> {
                    if (field instanceof String) {
                        return new GqlField((String) field);
                    }
                    if (field instanceof GqlField) {
                        return (GqlField) field;
                    }
                    throw new IllegalArgumentException("Invalid field type");
                })
                .collect(Collectors.toList())));
    }

    SELF fields(@NotNull GqlFields fields);
}
