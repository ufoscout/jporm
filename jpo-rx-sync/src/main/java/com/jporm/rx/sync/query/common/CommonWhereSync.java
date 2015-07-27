package com.jporm.rx.sync.query.common;

import com.jporm.commons.core.query.clause.Where;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.sql.query.clause.WhereExpressionElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by ufo on 27/07/15.
 */
public abstract class CommonWhereSync<T extends Where<T>> implements Where<T> {

    private Where<?> whereClause;

    public CommonWhereSync(Where<?> whereClause) {
        this.whereClause = whereClause;
    }

    protected abstract T where();

    @Override
    public T allEq(Map<String, Object> propertyMap) {
        whereClause.allEq(propertyMap);
        return where();
    }

    @Override
    public T and(WhereExpressionElement... WhereExpressionElements) {
        whereClause.and(WhereExpressionElements);
        return where();
    }

    @Override
    public T and(List<WhereExpressionElement> WhereExpressionElements) {
        whereClause.and(WhereExpressionElements);
        return where();
    }

    @Override
    public T and(String customClause, Object... args) {
        whereClause.and(customClause, args);
        return where();
    }

    @Override
    public T eq(String property, Object value) {
        whereClause.eq(property, value);
        return where();
    }

    @Override
    public T eqProperties(String firstProperty, String secondProperty) {
        whereClause.eqProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ge(String property, Object value) {
        whereClause.ge(property, value);
        return where();
    }

    @Override
    public T geProperties(String firstProperty, String secondProperty) {
        whereClause.geProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T gt(String property, Object value) {
        whereClause.gt(property, value);
        return where();
    }

    @Override
    public T gtProperties(String firstProperty, String secondProperty) {
        whereClause.gtProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ieq(String property, String value) {
        whereClause.ieq(property, value);
        return where();
    }

    @Override
    public T ieqProperties(String firstProperty, String secondProperty) {
        whereClause.ieqProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ilike(String property, String value) {
        whereClause.ilike(property, value);
        return where();
    }

    @Override
    public T in(String property, CommonFindQueryRoot subQuery) {
        whereClause.in(property, subQuery);

        return where();
    }

    @Override
    public T in(String property, Collection<?> values) {
        whereClause.in(property, values);
        return where();
    }

    @Override
    public T in(String property, Object[] values) {
        whereClause.in(property, values);
        return where();
    }

    @Override
    public T isNotNull(String property) {
        whereClause.isNotNull(property);
        return where();
    }

    @Override
    public T isNull(String property) {
        whereClause.isNull(property);
        return where();
    }

    @Override
    public T le(String property, Object value) {
        whereClause.le(property, value);
        return where();
    }

    @Override
    public T leProperties(String firstProperty, String secondProperty) {
        whereClause.leProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T like(String property, String value) {
        whereClause.like(property, value);
        return where();
    }

    @Override
    public T lt(String property, Object value) {
        whereClause.lt(property, value);
        return where();
    }

    @Override
    public T ltProperties(String firstProperty, String secondProperty) {
        whereClause.ltProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T ne(String property, Object value) {
        whereClause.ne(property, value);
        return where();
    }

    @Override
    public T neProperties(String firstProperty, String secondProperty) {
        whereClause.neProperties(firstProperty, secondProperty);
        return where();
    }

    @Override
    public T nin(String property, CommonFindQueryRoot subQuery) {
        whereClause.nin(property, subQuery);
        return where();
    }

    @Override
    public T nin(String property, Collection<?> values) {
        whereClause.nin(property, values);
        return where();
    }

    @Override
    public T nin(String property, Object[] values) {
        whereClause.nin(property, values);
        return where();
    }

    @Override
    public T nlike(String property, String value) {
        whereClause.nlike(property, value);
        return where();
    }

    @Override
    public T not(WhereExpressionElement... expression) {
        whereClause.not(expression);
        return where();
    }

    @Override
    public T not(List<WhereExpressionElement> whereExpressionElements) {
        whereClause.not(whereExpressionElements);
        return where();
    }

    @Override
    public T not(String customClause, Object... args) {
        whereClause.not(customClause, args);
        return where();
    }

    @Override
    public T or(WhereExpressionElement... whereExpressionElements) {
        whereClause.or(whereExpressionElements);
        return where();
    }

    @Override
    public T or(List<WhereExpressionElement> whereExpressionElements) {
        whereClause.or(whereExpressionElements);
        return where();
    }

    @Override
    public T or(String customClause, Object... args) {
        whereClause.or(customClause, args);
        return where();
    }


}
