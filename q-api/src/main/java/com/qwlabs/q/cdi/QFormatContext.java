package com.qwlabs.q.cdi;

import com.qwlabs.q.conditions.QCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class QFormatContext {
    private final String dialect;
    private final Class<? extends QCondition> conditionType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QFormatContext that = (QFormatContext) o;
        return Objects.equals(dialect, that.dialect) && Objects.equals(conditionType, that.conditionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dialect, conditionType);
    }
}
