package com.qwlabs.graphql.builder;

import com.qwlabs.graphql.builder.formatters.GqlFormatter;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public final class GqlField implements GqlFieldsAware<GqlField>, GqlArgumentsAware<GqlField> {
    private final String name;
    private GqlArguments arguments;
    private GqlFields fields;
    private String alias;

    protected GqlField(@NotNull String name) {
        this.name = name;
    }

    public GqlField args(GqlArguments arguments) {
        this.arguments = arguments;
        return this;
    }

    public GqlField fields(GqlFields fields) {
        this.fields = fields;
        return this;
    }

    public GqlField alias(String alias) {
        this.alias = alias;
        return this;
    }

    public String build(GqlFormatter formatter, int level) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatter.getIndent(level));
        Optional.ofNullable(this.alias).ifPresent(a ->
                builder.append(formatter.formatFieldAlias(a))
                        .append(formatter.formatColon(":"))
        );
        builder.append(formatter.formatFieldName(this.name));
        Optional.ofNullable(arguments).ifPresent(as -> builder.append(as.build(formatter)));
        Optional.ofNullable(fields).ifPresent(fs -> builder.append(fs.build(formatter, level)));
        return builder.toString();
    }
}
