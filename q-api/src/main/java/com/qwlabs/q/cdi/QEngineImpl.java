package com.qwlabs.q.cdi;

import com.qwlabs.cdi.DispatchInstance;
import com.qwlabs.exceptions.ServiceException;
import com.qwlabs.lang.S2;
import com.qwlabs.q.QEngine;
import com.qwlabs.q.QMessages;
import com.qwlabs.q.conditions.QCondition;
import com.qwlabs.q.formatters.QFormatter;
import com.qwlabs.q.parsers.QParser;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

@Slf4j
@ApplicationScoped
public class QEngineImpl implements QEngine {
    private final DispatchInstance<QFormatContext, QFormatter> formatters;

    private final DispatchInstance<String, QParser> parsers;

    @Inject
    public QEngineImpl(Instance<QFormatter> formatters,
                       Instance<QParser> parsers) {
        this.formatters = DispatchInstance.of(formatters, true);
        this.parsers = DispatchInstance.of(parsers, true);
    }

    @Override
    @Nullable
    public String format(@NotNull String dialect, @Nullable QCondition condition) {
        if (S2.isEmpty(dialect)) {
            throw QMessages.INSTANCE.invalidDialect(dialect);
        }
        if (condition == null) {
            return null;
        }
        var context = QFormatContext.builder()
                .conditionType(condition.getClass())
                .dialect(dialect)
                .build();
        return formatters.get(context).format(condition);
    }

    @Override
    @Nullable
    public QCondition parse(@NotNull String dialect, @Nullable String query) {
        if (S2.isEmpty(dialect)) {
            throw QMessages.INSTANCE.invalidDialect(dialect);
        }
        if (query == null) {
            return null;
        }
        try {
            return parsers.get(dialect).parse(query);
        } catch (ServiceException e) {
            LOGGER.error("Invalid q. dialect:{} query: {}", dialect, query, e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Invalid q. dialect:{} query: {}", dialect, query, e);
            throw QMessages.INSTANCE.invalidQuery(query);
        }
    }
}
