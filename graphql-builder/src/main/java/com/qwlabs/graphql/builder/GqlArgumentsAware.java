package com.qwlabs.graphql.builder;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public interface GqlArgumentsAware<SELF> {

    default SELF args(@NotNull GqlArgument @NotNull ... args) {
        return args(new GqlArguments(Arrays.asList(args)));
    }

    SELF args(@NotNull GqlArguments fields);
}
