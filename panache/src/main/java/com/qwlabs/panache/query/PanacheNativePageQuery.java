package com.qwlabs.panache.query;

import com.qwlabs.panache.Where;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class PanacheNativePageQuery<Entity> extends AbstractPanacheNativeQuery<Entity> {
    private final EntityManager em;
    private final String dataQuery;
    private final Map<String, Object> paramsArrayOrMap;
    private final String countQuery;
    private final Class<Entity> clazz;

    public PanacheNativePageQuery(EntityManager em, String dataQuery, String countQuery,
                                  Map<String, Object> paramsArrayOrMap,
                                  Class<Entity> clazz) {
        this.em = em;
        this.dataQuery = dataQuery;
        this.countQuery = countQuery;
        this.paramsArrayOrMap = paramsArrayOrMap;
        this.clazz = clazz;
    }


    public PanacheNativePageQuery(EntityManager em, String table,
                                  Where where,
                                  Class<Entity> clazz) {
        this.em = em;
        this.dataQuery = " select * from " + table + where.getWithWhere();
        this.countQuery = " select count(*) from " + table + where.getWithWhere();
        this.paramsArrayOrMap = where.getParameters();
        this.clazz = clazz;
    }

    @Override
    public <T> PanacheQuery<T> project(Class<T> type) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withLock(LockModeType lockModeType) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> withHint(String hintName, Object value) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName, Parameters parameters) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName, Map<String, Object> parameters) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public <T extends Entity> PanacheQuery<T> filter(String filterName) {
        return (PanacheQuery<T>) this;
    }

    @Override
    public long count() {
        BigInteger count = (BigInteger) buildCountQuery().getSingleResult();
        return count.longValue();
    }

    private Query buildDataQuery() {
        Query nativeQuery = em.createNativeQuery(dataQuery, clazz);
        Optional.ofNullable(this.paramsArrayOrMap).ifPresent(params -> params.forEach(nativeQuery::setParameter));
        return nativeQuery;
    }

    private Query buildDataPageQuery() {
        String limit = " limit " + range.getLastIndex() + " offset " + range.getStartIndex();
        Query nativeQuery = em.createNativeQuery(dataQuery + limit, clazz);
        Optional.ofNullable(this.paramsArrayOrMap).ifPresent(params -> params.forEach(nativeQuery::setParameter));
        return nativeQuery;
    }

    private Query buildCountQuery() {
        Query nativeQuery = em.createNativeQuery(countQuery);
        Optional.ofNullable(this.paramsArrayOrMap).ifPresent(params -> params.forEach(nativeQuery::setParameter));
        return nativeQuery;
    }

    @Override
    public <T extends Entity> List<T> list() {
        if (range == null) {
            return buildDataQuery().getResultList();
        }
        return buildDataPageQuery().getResultList();
    }

    @Override
    public <T extends Entity> Stream<T> stream() {
        if (range == null) {
            return buildDataQuery().getResultStream();
        }
        return buildDataPageQuery().getResultStream();
    }

    @Override
    public <T extends Entity> T firstResult() {
        return (T) this.firstResultOptional().get();
    }

    @Override
    public <T extends Entity> Optional<T> firstResultOptional() {
        return buildDataQuery().getResultStream().findFirst();
    }

    @Override
    public <T extends Entity> T singleResult() {
        return (T) this.singleResultOptional().get();
    }

    @Override
    public <T extends Entity> Optional<T> singleResultOptional() {
        return (Optional<T>) buildDataQuery().getSingleResult();
    }
}