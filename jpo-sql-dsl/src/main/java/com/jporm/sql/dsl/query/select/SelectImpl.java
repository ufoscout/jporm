/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.jporm.sql.dsl.query.select;

import java.util.ArrayList;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASql;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.select.from.FromImpl;
import com.jporm.sql.dsl.query.select.groupby.GroupBy;
import com.jporm.sql.dsl.query.select.groupby.GroupByImpl;
import com.jporm.sql.dsl.query.select.orderby.OrderBy;
import com.jporm.sql.dsl.query.select.orderby.OrderByImpl;
import com.jporm.sql.dsl.query.select.where.SelectWhere;
import com.jporm.sql.dsl.query.select.where.SelectWhereImpl;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectImpl extends ASql implements Select {

    public static String[] NO_FIELDS = new String[0];

    private static String SQL_EXCEPT = "\nEXCEPT \n";
    private static String SQL_INTERSECT = "\nINTERSECT \n";
    private static String SQL_UNION = "\nUNION \n";
    private static String SQL_UNION_ALL = "\nUNION ALL \n";

    private final PropertiesProcessor propertiesProcessor;

    private final FromImpl from;
    private final SelectWhereImpl where;
    private final OrderByImpl orderBy;
    private final GroupByImpl groupBy;
    private final List<SelectCommon> unions = new ArrayList<>();
    private final List<SelectCommon> unionAlls = new ArrayList<>();
    private final List<SelectCommon> intersects = new ArrayList<>();
    private final List<SelectCommon> excepts = new ArrayList<>();

    private final DBProfile dbProfile;

    private boolean distinct = false;
    private LockMode lockMode = LockMode.NO_LOCK;
    private int maxRows = 0;
    private int firstRow = -1;
    private final String[] selectFields;

    public SelectImpl(DBProfile dbProfile, final String[] selectFields, String fromTable, String fromTableAlias, PropertiesProcessor propertiesProcessor ) {
        this.dbProfile = dbProfile;
        this.selectFields = selectFields;
        this.propertiesProcessor = propertiesProcessor;
        from = new FromImpl(this, fromTable, fromTableAlias);
        where = new SelectWhereImpl(this);
        orderBy = new OrderByImpl(this);
        groupBy = new GroupByImpl(this);
    }

    @Override
    public void sqlValues(final List<Object> values) {
        where.sqlElementValues(values);
        groupBy.sqlElementValues(values);

        unions.forEach(select -> select.sqlValues(values));
        unionAlls.forEach(select -> select.sqlValues(values));
        excepts.forEach(select -> select.sqlValues(values));
        intersects.forEach(select -> select.sqlValues(values));

    }

    public Select distinct(final boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public LockMode getLockMode() {
        return lockMode;
    }

    public String[] getSelectFields() {
        return selectFields;
    }

    @Override
    public GroupBy groupBy() {
        return groupBy;
    }

    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public Select limit(final int limit) {
        maxRows = limit;
        return this;
    }

    @Override
    public Select lockMode(final LockMode lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    @Override
    public Select offset(final int offset) {
        firstRow = offset;
        return this;
    }

    @Override
    public OrderBy orderBy() {
        return orderBy;
    }

    @Override
    public String sqlRowCountQuery() {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM ( "); //$NON-NLS-1$
        renderSQLWithoutPagination(dbProfile, queryBuilder);
        queryBuilder.append(") a "); //$NON-NLS-1$
        return queryBuilder.toString();
    }

    @Override
    public void sqlQuery(final StringBuilder queryBuilder) {
        dbProfile.getSqlStrategy().paginateSQL(queryBuilder, firstRow, maxRows, builder -> renderSQLWithoutPagination(dbProfile, builder));
    }

    private void renderSQLWithoutPagination(final DBProfile dbProfile, final StringBuilder builder) {
        builder.append("SELECT "); //$NON-NLS-1$
        if (distinct) {
            builder.append("DISTINCT "); //$NON-NLS-1$
        }

        int size = selectFields.length;
        boolean first = true;
        for (int i = 0; i < size; i++) {
            String field = selectFields[i];
            if (!first) {
                builder.append(", "); //$NON-NLS-1$
            } else {
                first = false;
            }
            builder.append(field);
        }

        builder.append(" "); //$NON-NLS-1$
        from.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        where.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        groupBy.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        orderBy.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        render(SQL_UNION, unions, builder);
        render(SQL_UNION_ALL, unionAlls, builder);
        render(SQL_EXCEPT, excepts, builder);
        render(SQL_INTERSECT, intersects, builder);

        builder.append(lockMode.getMode());
    }

    @Override
    public SelectWhere where() {
        return where;
    }

    @Override
    public SelectWhere where(final List<WhereExpressionElement> expressionElements) {
        return where.and(expressionElements);
    }

    @Override
    public SelectWhere where(final String customClause, final Object... args) {
        return where.and(customClause, args);
    }

    @Override
    public SelectWhere where(final WhereExpressionElement... expressionElements) {
        return where.and(expressionElements);
    }

    @Override
    public Select union(SelectCommon select) {
        unions.add(select);
        return this;
    }

    private void render(String clause, List<SelectCommon> selects, final StringBuilder queryBuilder) {
        for (SelectCommon selectCommon : selects) {
            queryBuilder.append(clause);
            selectCommon.sqlQuery(queryBuilder);
        }
    }

    @Override
    public Select unionAll(SelectCommon select) {
        unionAlls.add(select);
        return this;
    }

    @Override
    public Select except(SelectCommon select) {
        excepts.add(select);
        return this;
    }

    @Override
    public Select intersect(SelectCommon select) {
        intersects.add(select);
        return this;
    }

    @Override
    public final Select fullOuterJoin(String joinTable) {
        return from.fullOuterJoin(joinTable);
    }

    @Override
    public final Select fullOuterJoin(String joinTable, String joinTableAlias) {
        return from.fullOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select fullOuterJoin(String joinTable, String onLeftProperty, String onRigthProperty) {
        return from.fullOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select fullOuterJoin(String joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.fullOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select innerJoin(String joinTable) {
        return from.innerJoin(joinTable);
    }

    @Override
    public final Select innerJoin(String joinTable, String joinTableAlias) {
        return from.innerJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select innerJoin(String joinTable, String onLeftProperty, String onRigthProperty) {
        return from.innerJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select innerJoin(String joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.innerJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select join(String joinTable) {
        return from.join(joinTable);
    }

    @Override
    public final Select join(String joinTable, String joinTableAlias) {
        return from.join(joinTable, joinTableAlias);
    }

    @Override
    public final Select leftOuterJoin(String joinTable) {
        return from.leftOuterJoin(joinTable);
    }

    @Override
    public final Select leftOuterJoin(String joinTable, String joinTableAlias) {
        return from.leftOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select leftOuterJoin(String joinTable, String onLeftProperty, String onRigthProperty) {
        return from.leftOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select leftOuterJoin(String joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.leftOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select naturalJoin(String joinTable) {
        return from.naturalJoin(joinTable);
    }

    @Override
    public final Select naturalJoin(String joinTable, String joinTableAlias) {
        return from.naturalJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select rightOuterJoin(String joinTable) {
        return from.rightOuterJoin(joinTable);
    }

    @Override
    public final Select rightOuterJoin(String joinTable, String joinTableAlias) {
        return from.rightOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select rightOuterJoin(String joinTable, String onLeftProperty, String onRigthProperty) {
        return from.rightOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select rightOuterJoin(String joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.rightOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

}
