package com.qwlabs.graphql.builder;

import com.qwlabs.graphql.builder.formatters.GqlFormatter;
import com.qwlabs.lang.C2;

import java.util.List;
import java.util.StringJoiner;

public final class GqlFields {
    private List<GqlField> fields;

    protected GqlFields(List<GqlField> fields) {
        this.fields = fields;
    }

    public boolean isEmpty() {
        return C2.isEmpty(this.fields);
    }

    public String build(GqlFormatter formatter, int level) {
        if (C2.isEmpty(this.fields)) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(
                formatter.formatFieldDelimiter(" "),
                formatter.formatStartBraces("{"),
                formatter.formatEndBraces("}", level));
        this.fields.forEach(field -> joiner.add(field.build(formatter, level + 1)));
        return joiner.toString();
    }

}
