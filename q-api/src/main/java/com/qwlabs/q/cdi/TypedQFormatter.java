package com.qwlabs.q.cdi;

import com.google.common.base.Suppliers;
import com.qwlabs.cdi.Dispatchable;
import com.qwlabs.lang.Annotations;
import com.qwlabs.q.QEngine;
import com.qwlabs.q.conditions.QCondition;
import com.qwlabs.q.formatters.QFormatter;
import jakarta.annotation.Nullable;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class TypedQFormatter<C extends QCondition>
        implements Dispatchable<QFormatContext>,
        QFormatter,
        QEngineAware {
    private final Supplier<QFormatContext> context = Suppliers.memoize(this::buildContext);
    private final Supplier<QEngine> engine = Suppliers.memoize(this::loadEngine);
    private final QEngine presetEngine;

    public TypedQFormatter() {
        this(null);
    }

    public TypedQFormatter(QEngine presetEngine) {
        this.presetEngine = presetEngine;
    }

    private QEngine loadEngine() {
        return Optional.ofNullable(presetEngine)
                .orElseGet(this::engine);
    }

    private QFormatContext buildContext() {
        return QFormatContext.builder()
                .conditionType(Annotations.actualTypeArgument(this.getClass()))
                .dialect(dialect())
                .build();
    }

    @Override
    public @NotNull String format(@NotNull QCondition condition) {
        return doFormat((C) condition);
    }

    @Override
    public boolean dispatchable(@Nullable QFormatContext context) {
        return Objects.equals(this.context.get(), context);
    }

    @NotNull
    protected String continueFormat(@NotNull QCondition condition) {
        return engine.get().format(dialect(), condition);
    }

    protected abstract String dialect();

    protected abstract String doFormat(@NotNull C condition);
}
