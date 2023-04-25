package com.qwlabs.security;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.annotation.Nullable;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class GrantTarget implements GrantTargetAble {
    public static final GrantTarget NON_SPECIFIED = new GrantTarget(null, null);

    private final String targetType;
    private final String target;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GrantTarget that = (GrantTarget) o;
        return Objects.equals(targetType, that.targetType) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType, target);
    }

    @Override
    public String toString() {
        return SCOPE_TEMPLATE.formatted(targetType, target);
    }

    @Nullable
    public static GrantTarget of(@Nullable String scope) {
        if (scope == null) {
            return NON_SPECIFIED;
        }
        var sections = scope.split(SCOPE_SEPARATOR);
        if (sections.length != 2) {
            return NON_SPECIFIED;
        }
        var targetType = Strings.emptyToNull(sections[0].trim());
        var target = Strings.emptyToNull(sections[1].trim());
        return of(targetType, target);
    }

    public static GrantTarget of(GrantTargetAble target) {
        return of(target.getTargetType(), target.getTarget());
    }

    public static GrantTarget of(String targetType, String target) {
        return GrantTargetAble.isSpecified(targetType) ? new GrantTarget(targetType, target) : NON_SPECIFIED;
    }
}
