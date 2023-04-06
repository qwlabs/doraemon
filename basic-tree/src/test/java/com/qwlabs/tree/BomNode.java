package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class BomNode {
    @JsonAnyGetter
    @JsonAnySetter
    @JsonIgnore
    private Map<String, String> attributes;

    @JsonIgnore
    public String getValue() {
        return attributes.get("value");
    }

    @JsonIgnore
    public String getParent() {
        var value = getValue();
        if (value.isEmpty()) {
            return null;
        }
        return Strings.emptyToNull(value.substring(0, value.length() - 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BomNode that = (BomNode) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    public static BomNode of(String value) {
        BomNode node = new BomNode();
        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("value", value);
        attributes.put("value1", value);
        node.setAttributes(attributes);
        return node;
    }
}
