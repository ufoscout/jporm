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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASql;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.processor.TableName;
import com.jporm.sql.dsl.query.processor.TablePropertiesProcessor;
import com.jporm.sql.dsl.query.select.from.FromImpl;
import com.jporm.sql.dsl.query.select.groupby.GroupBy;
import com.jporm.sql.dsl.query.select.groupby.GroupByImpl;
import com.jporm.sql.dsl.query.select.orderby.OrderBy;
import com.jporm.sql.dsl.query.select.orderby.OrderByImpl;
import com.jporm.sql.dsl.query.select.where.SelectWhere;
import com.jporm.sql.dsl.query.select.where.SelectWhereImpl;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;
import com.jporm.sql.dsl.util.StringUtil;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectImpl<JOIN> extends ASql implements Select<JOIN> {

    public static String[] NO_FIELDS = new String[0];
    public static String SQL_SELECT_SPLIT_PATTERN = "[^,]*[\\(][^\\)]*[\\)][^,]*|[^,]+";
    private static Pattern patternSelectClause = Pattern.compile(SQL_SELECT_SPLIT_PATTERN);

    private static String SQL_EXCEPT = "\nEXCEPT \n";
    private static String SQL_INTERSECT = "\nINTERSECT \n";
    private static String SQL_UNION = "\nUNION \n";
    private static String SQL_UNION_ALL = "\nUNION ALL \n";

    private final PropertiesProcessor propertiesProcessor;

    private final FromImpl<JOIN> from;
    private final SelectWhereImpl where;
    private final OrderByImpl orderBy;
    private final GroupByImpl groupBy;
    private final List<SelectCommon> unions = new ArrayList<>();
    private final List<SelectCommon> unionAlls = new ArrayList<>();
    private final List<SelectCommon> intersects = new ArrayList<>();
    private final List<SelectCommon> excepts = new ArrayList<>();

    private boolean distinct = false;
    private LockMode lockMode = LockMode.NO_LOCK;
    private int maxRows = 0;
    private int firstRow = -1;
    private final String[] selectFields;

    public SelectImpl(DBProfile dbProfile, String[] selectFields, final JOIN tableNameSource, final TablePropertiesProcessor<JOIN> propertiesProcessor) {
        this(dbProfile, selectFields, propertiesProcessor.getTableName(tableNameSource), propertiesProcessor);
    }

    public SelectImpl(DBProfile dbProfile, String[] selectFields, final JOIN tableNameSource, final TablePropertiesProcessor<JOIN> propertiesProcessor, final String alias) {
        this(dbProfile, selectFields, propertiesProcessor.getTableName(tableNameSource, alias), propertiesProcessor);
    }

    private SelectImpl(DBProfile dbProfile, String[] selectFields, final TableName tableName, final TablePropertiesProcessor<JOIN> propertiesProcessor) {
        super(dbProfile);
        this.selectFields = selectFields;
        this.propertiesProcessor = propertiesProcessor;
        from = new FromImpl<JOIN>(this, tableName, propertiesProcessor);
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

    public Select<JOIN> distinct(final boolean distinct) {
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
    public Select<JOIN> limit(final int limit) {
        maxRows = limit;
        return this;
    }

    @Override
    public Select<JOIN> lockMode(final LockMode lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    @Override
    public Select<JOIN> offset(final int offset) {
        firstRow = offset;
        return this;
    }

    @Override
    public OrderBy orderBy() {
        return orderBy;
    }

    @Override
    public String sqlRowCountQuery() {
        return sqlRowCountQuery(getDefaultDbProfile());
    }

    @Override
    public String sqlRowCountQuery(DBProfile dbProfile) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM ( "); //$NON-NLS-1$
        renderSQLWithoutPagination(dbProfile, queryBuilder);
        queryBuilder.append(") a "); //$NON-NLS-1$
        return queryBuilder.toString();
    }

    @Override
    public void sqlQuery(final DBProfile dbProfile, final StringBuilder queryBuilder) {
        dbProfile.getSqlStrategy().paginateSQL(queryBuilder, firstRow, maxRows, builder -> renderSQLWithoutPagination(dbProfile, builder));
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
    public Select<JOIN> union(SelectCommon select) {
        unions.add(select);
        return this;
    }

    @Override
    public Select<JOIN> unionAll(SelectCommon select) {
        unionAlls.add(select);
        return this;
    }

    @Override
    public Select<JOIN> except(SelectCommon select) {
        excepts.add(select);
        return this;
    }

    @Override
    public Select<JOIN> intersect(SelectCommon select) {
        intersects.add(select);
        return this;
    }

    @Override
    public final Select<JOIN> fullOuterJoin(JOIN joinTable) {
        return from.fullOuterJoin(joinTable);
    }

    @Override
    public final Select<JOIN> fullOuterJoin(JOIN joinTable, String joinTableAlias) {
        return from.fullOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select<JOIN> fullOuterJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty) {
        return from.fullOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select<JOIN> fullOuterJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.fullOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select<JOIN> innerJoin(JOIN joinTable) {
        return from.innerJoin(joinTable);
    }

    @Override
    public final Select<JOIN> innerJoin(JOIN joinTable, String joinTableAlias) {
        return from.innerJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select<JOIN> innerJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty) {
        return from.innerJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select<JOIN> innerJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.innerJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select<JOIN> join(JOIN joinTable) {
        return from.join(joinTable);
    }

    @Override
    public final Select<JOIN> join(JOIN joinTable, String joinTableAlias) {
        return from.join(joinTable, joinTableAlias);
    }

    @Override
    public final Select<JOIN> leftOuterJoin(JOIN joinTable) {
        return from.leftOuterJoin(joinTable);
    }

    @Override
    public final Select<JOIN> leftOuterJoin(JOIN joinTable, String joinTableAlias) {
        return from.leftOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select<JOIN> leftOuterJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty) {
        return from.leftOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select<JOIN> leftOuterJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.leftOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select<JOIN> naturalJoin(JOIN joinTable) {
        return from.naturalJoin(joinTable);
    }

    @Override
    public final Select<JOIN> naturalJoin(JOIN joinTable, String joinTableAlias) {
        return from.naturalJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select<JOIN> rightOuterJoin(JOIN joinTable) {
        return from.rightOuterJoin(joinTable);
    }

    @Override
    public final Select<JOIN> rightOuterJoin(JOIN joinTable, String joinTableAlias) {
        return from.rightOuterJoin(joinTable, joinTableAlias);
    }

    @Override
    public final Select<JOIN> rightOuterJoin(JOIN joinTable, String onLeftProperty, String onRigthProperty) {
        return from.rightOuterJoin(joinTable, onLeftProperty, onRigthProperty);
    }

    @Override
    public final Select<JOIN> rightOuterJoin(JOIN joinTable, String joinTableAlias, String onLeftProperty, String onRigthProperty) {
        return from.rightOuterJoin(joinTable, joinTableAlias, onLeftProperty, onRigthProperty);
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

            final Matcher m = patternSelectClause.matcher(field);
            boolean loop = m.find();
            while (loop) {
                solveField(m.group().trim(), builder, propertiesProcessor);
                loop = m.find();
                if (loop) {
                    builder.append(", "); //$NON-NLS-1$
                }
            }
        }

        builder.append(" "); //$NON-NLS-1$
        from.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        where.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        groupBy.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        orderBy.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        render(SQL_UNION, unions, dbProfile, builder);
        render(SQL_UNION_ALL, unionAlls, dbProfile, builder);
        render(SQL_EXCEPT, excepts, dbProfile, builder);
        render(SQL_INTERSECT, intersects, dbProfile, builder);

        builder.append(lockMode.getMode());
    }

    /**
     * @param string
     * @return
     */
    private void solveField(final String field, final StringBuilder queryBuilder, final PropertiesProcessor propertiesProcessor) {
        if (field.contains("(") || StringUtil.containsIgnoreCase(field, " as ")) { //$NON-NLS-1$ //$NON-NLS-2$
            propertiesProcessor.solveAllPropertyNames(field, queryBuilder);
        } else {
            queryBuilder.append(propertiesProcessor.solvePropertyName(field));
            queryBuilder.append(" AS \""); //$NON-NLS-1$
            queryBuilder.append(field);
            queryBuilder.append("\""); //$NON-NLS-1$
        }
    }


    private void render(String clause, List<SelectCommon> selects, final DBProfile dbProfile, final StringBuilder queryBuilder) {
        for (SelectCommon selectCommon : selects) {
            queryBuilder.append(clause);
            selectCommon.sqlQuery(dbProfile, queryBuilder);
        }
    }



}
