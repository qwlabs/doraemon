package com.qwlabs.graphql.builder;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public interface GqlVariablesAware<SELF> {

    default SELF vars(@NotNull GqlVariable @NotNull ... variables) {
        return vars(new GqlVariables(Arrays.asList(variables)));
    }

    SELF vars(@NotNull GqlVariables variables);
}
