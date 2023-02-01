package com.qwlabs.graphql.builder;

import jakarta.validation.constraints.NotNull;

public class GqlRefArgument extends GqlArgument {

    protected GqlRefArgument(@NotNull String name) {
        super(name, String.format("$%s", name));
    }
}
