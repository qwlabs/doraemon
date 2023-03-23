package com.qwlabs.security;


import jakarta.annotation.Nullable;

import java.util.Objects;

public interface GrantTargetAble {
    String ALL = "*";
    String SCOPE_SEPARATOR = ":";
    String SCOPE_TEMPLATE = "%s:%s";

    @Nullable
    String getTargetType();

    @Nullable
    String getTarget();

    /**
     * definite mean grant with definite target
     *
     * @return if is definite
     */
    default boolean isSpecified() {
        return isSpecified(getTargetType());
    }

    static boolean isSpecified(GrantTargetAble target) {
        return target != null && isSpecified(target.getTargetType());
    }

    static boolean isSpecified(String targetType) {
        return Objects.nonNull(targetType);
    }


    default boolean isAll() {
        return Objects.equals(ALL, getTargetType());
    }

    default boolean isAll(String targetType) {
        return Objects.equals(targetType, getTargetType())
            && Objects.equals(ALL, getTarget());
    }

    default boolean contains(GrantTargetAble that) {
        if (isAll()) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!that.isSpecified()) {
            return true;
        }
        if (isAll(that.getTargetType())) {
            return true;
        }
        return Objects.equals(this.getTarget(), that.getTarget());
    }
}