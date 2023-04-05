package com.qwlabs.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class TestTreeNode extends BaseTreeNode<TestTreeNode> {
    private String value;

    @Override
    public TestTreeNode newInstance() {
        return new TestTreeNode();
    }

    @JsonIgnore
    public String getParent() {
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
        TestTreeNode that = (TestTreeNode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public static TestTreeNode of(String value) {
        TestTreeNode node = new TestTreeNode();
        node.setValue(value);
        return node;
    }
}
