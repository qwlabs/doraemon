package com.qwlabs.panache;

import io.quarkus.panache.common.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static io.quarkus.panache.hibernate.common.runtime.PanacheJpaUtil.toOrderBy;

public final class Where {
    private static final String AND = "and";
    private static final String OR = "or";

    private StringBuilder where = new StringBuilder();
    private Map<String, Object> parameters = new HashMap<>();
    private Sort sort;

    private Where() {
    }

    public static Where create() {
        return new Where();
    }

    public Where and(Where where) {
        Objects.requireNonNull(where, "where can not be null.");
        return with(AND, where);
    }

    public Where or(Where where) {
        Objects.requireNonNull(where, "where can not be null.");
        return with(OR, where);
    }

    public <T> Where and(String expression, String name, T value) {
        return and(expression, name, value, Objects::nonNull);
    }

    public <T> Where and(String expression, String name, T value, Predicate<T> predicate) {
        Objects.requireNonNull(expression, "expression can not be null.");
        Objects.requireNonNull(name, "name can not be null.");
        return with(AND, expression, name, value, predicate);
    }


    public Where and(String sql) {
        if (sql == null || sql.isBlank()) {
            return this;
        }
        return with(AND, sql);
    }


    public Where or(String sql) {
        if (sql == null || sql.isBlank()) {
            return this;
        }
        return with(OR, sql);
    }

    public <T> Where or(String expression, String name, T value) {
        return or(expression, name, value, Objects::nonNull);
    }

    public <T> Where or(String expression, String name, T value, Predicate<T> predicate) {
        Objects.requireNonNull(expression, "expression can not be null.");
        Objects.requireNonNull(name, "name can not be null.");
        return with(OR, expression, name, value, predicate);
    }

    public Where sort(Sort sort) {
        Objects.requireNonNull(sort, "sort can not be null.");
        this.sort = sort;
        return this;
    }

    private Where with(String operator, Where where) {
        if (where.isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            this.where = where.where;
        } else {
            this.pack().where.append(operator).append(where.pack().where);
        }
        this.parameters.putAll(where.parameters);
        return this;
    }

    private Where append(String operator) {
        if (where.length() > 0) {
            where.append(" ").append(operator).append(" ");
        }
        return this;
    }

    private Where pack() {
        where.insert(0, "(").append(")");
        return this;
    }

    private Where with(String operator, String query) {
        this.append(operator).where.append(query);
        return this;
    }

    private <T> Where with(String operator, String expression, String name, T value, Predicate<T> predicate) {
        if (!predicate.test(value)) {
            return this;
        }
        this.append(operator).where.append(expression);
        parameters.put(name, value);
        return this;
    }

    public boolean isEmpty() {
        return this.where.length() == 0 && parameters.isEmpty();
    }

    public String get() {
        return where.toString() + getSort().orElse("");
    }

    public String getWithWhere() {
        return isEmpty() ? "" : " where " + where.toString();
    }

    public Optional<String> getSort() {
        return Optional.ofNullable(toOrderBy(sort));
    }

    public String getAll() {
        return getWithWhere() + getSort().orElse("");
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }
}
