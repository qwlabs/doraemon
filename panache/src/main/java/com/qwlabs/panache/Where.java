package com.qwlabs.panache;

import io.quarkus.panache.common.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public Where and(String expression, String name, Object value) {
        Objects.requireNonNull(expression, "expression can not be null.");
        Objects.requireNonNull(name, "name can not be null.");
        return with(AND, expression, name, value);
    }

    public Where or(String expression, String name, Object value) {
        Objects.requireNonNull(expression, "expression can not be null.");
        Objects.requireNonNull(name, "name can not be null.");
        return with(OR, expression, name, value);
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
            return where;
        }
        this.pack().where.append(operator).append(where.pack().where);
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

    private Where with(String operator, String expression, String name, Object value) {
        if (value == null) {
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
        return where.toString() + toOrderBy(sort);
    }

    public String getWithWhere() {
        return isEmpty() ? "" : " where " + where.toString();
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }
}