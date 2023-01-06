package com.qwlabs.q.cdi;

import com.google.common.base.Suppliers;
import com.qwlabs.cdi.Dispatchable;
import com.qwlabs.lang.Annotations;
import com.qwlabs.q.QEngine;
import com.qwlabs.q.conditions.QCondition;
import com.qwlabs.q.formatters.QFormatter;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class TypedQFormatter<C extends QCondition> implements Dispatchable<QFormatContext>, QFormatter {
    private final QEngine engine;
    private final Supplier<QFormatContext> context = Suppliers.memoize(this::buildContext);

    private QFormatContext buildContext() {
        return QFormatContext.builder()
                .conditionType(Annotations.actualTypeArgument(this.getClass()))
                .dialect(dialect())
                .build();
    }

    public TypedQFormatter(QEngine engine) {
        this.engine = engine;
    }

    @Override
    public final @NotNull String format(@NotNull QCondition condition) {
        return doFormat((C) condition);
    }

    @Override
    public boolean dispatchable(@Nullable QFormatContext context) {
        return Objects.equals(this.context.get(), context);
    }

    @NotNull
    protected String continueFormat(@NotNull QCondition condition) {
        return engine.format(dialect(), condition);
    }

    protected abstract String dialect();

    protected abstract String doFormat(@NotNull C condition);
}
