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
package com.jporm.sql.query.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.select.from.FromImpl;
import com.jporm.sql.query.select.from.SelectFrom;
import com.jporm.sql.query.select.groupby.SelectGroupBy;
import com.jporm.sql.query.select.groupby.SelectGroupByImpl;
import com.jporm.sql.query.select.orderby.SelectOrderBy;
import com.jporm.sql.query.select.orderby.SelectOrderByImpl;
import com.jporm.sql.query.select.pagination.SelectPaginationProvider;
import com.jporm.sql.query.select.unions.SelectUnionsProvider;
import com.jporm.sql.query.select.where.SelectWhere;
import com.jporm.sql.query.select.where.SelectWhereImpl;
import com.jporm.sql.util.StringUtil;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectImpl<TYPE> extends FromImpl<TYPE, SelectFrom<TYPE>> implements Select<TYPE> {

    public static String[] NO_FIELDS = new String[0];
    public static String SQL_SELECT_SPLIT_PATTERN = "[^,]*[\\(][^\\)]*[\\)][^,]*|[^,]+";
    private static Pattern patternSelectClause = Pattern.compile(SQL_SELECT_SPLIT_PATTERN);

    private static String SQL_EXCEPT = "\nEXCEPT \n";
    private static String SQL_INTERSECT = "\nINTERSECT \n";
    private static String SQL_UNION = "\nUNION \n";
    private static String SQL_UNION_ALL = "\nUNION ALL \n";

    private final PropertiesProcessor propertiesProcessor;

    private final SelectWhereImpl where;
    private final SelectOrderByImpl orderBy;
    private final SelectGroupByImpl groupBy;
    private final List<SelectCommon> unions = new ArrayList<>();
    private final List<SelectCommon> unionAlls = new ArrayList<>();
    private final List<SelectCommon> intersects = new ArrayList<>();
    private final List<SelectCommon> excepts = new ArrayList<>();

    private boolean distinct = false;
    private LockMode lockMode = LockMode.NO_LOCK;
    private int maxRows = 0;
    private int firstRow = -1;
    private String[] selectFields;
    private final DBProfile dbProfile;

    public SelectImpl(DBProfile dbProfile, String[] selectFields, final TYPE tableNameSource, final TablePropertiesProcessor<TYPE> propertiesProcessor) {
        this(dbProfile, selectFields, propertiesProcessor.getTableName(tableNameSource), propertiesProcessor);
    }

    public SelectImpl(DBProfile dbProfile, String[] selectFields, final TYPE tableNameSource, final TablePropertiesProcessor<TYPE> propertiesProcessor, final String alias) {
        this(dbProfile, selectFields, propertiesProcessor.getTableName(tableNameSource, alias), propertiesProcessor);
    }

    private SelectImpl(DBProfile dbProfile, String[] selectFields, final TableName tableName, final TablePropertiesProcessor<TYPE> propertiesProcessor) {
        super(tableName, propertiesProcessor);
        this.dbProfile = dbProfile;
        this.selectFields = selectFields;
        this.propertiesProcessor = propertiesProcessor;
        where = new SelectWhereImpl(this);
        orderBy = new SelectOrderByImpl(this);
        groupBy = new SelectGroupByImpl(this);
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        where.sqlElementValues(values);
        groupBy.sqlElementValues(values);

        unions.forEach(select -> select.sqlValues(values));
        unionAlls.forEach(select -> select.sqlValues(values));
        excepts.forEach(select -> select.sqlValues(values));
        intersects.forEach(select -> select.sqlValues(values));

    }

    @Override
    public final Select<TYPE> distinct(final boolean distinct) {
        this.distinct = distinct;
        return this;
    }

	@Override
	public final Select<TYPE> distinct() {
		return distinct(true);
	}

    public final LockMode getLockMode() {
        return lockMode;
    }

    public final String[] getSelectFields() {
        return selectFields;
    }

    public final boolean isDistinct() {
        return distinct;
    }

    @Override
    public final Select<TYPE> limit(final int limit) {
        maxRows = limit;
        return this;
    }

    @Override
    public final Select<TYPE> lockMode(final LockMode lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    @Override
    public final Select<TYPE> offset(final int offset) {
        firstRow = offset;
        return this;
    }

    @Override
    public final String sqlRowCountQuery() {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM ( "); //$NON-NLS-1$
        renderSQLWithoutPagination(dbProfile, queryBuilder);
        queryBuilder.append(") a "); //$NON-NLS-1$
        return queryBuilder.toString();
    }

    @Override
    public final void sqlQuery(final StringBuilder queryBuilder) {
        dbProfile.getSqlStrategy().paginateSQL(queryBuilder, firstRow, maxRows, builder -> renderSQLWithoutPagination(dbProfile, builder));
    }

    @Override
    public final SelectUnionsProvider union(SelectCommon select) {
        unions.add(select);
        return this;
    }

    @Override
    public final SelectUnionsProvider unionAll(SelectCommon select) {
        unionAlls.add(select);
        return this;
    }

    @Override
    public final  SelectUnionsProvider except(SelectCommon select) {
        excepts.add(select);
        return this;
    }

    @Override
    public final SelectUnionsProvider intersect(SelectCommon select) {
        intersects.add(select);
        return this;
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
        sqlElementQuery(builder, dbProfile, propertiesProcessor);
        where.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        groupBy.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        orderBy.sqlElementQuery(builder, dbProfile, propertiesProcessor);
        render(SQL_UNION, unions, builder);
        render(SQL_UNION_ALL, unionAlls, builder);
        render(SQL_EXCEPT, excepts, builder);
        render(SQL_INTERSECT, intersects, builder);

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


    private void render(String clause, List<SelectCommon> selects, final StringBuilder queryBuilder) {
        for (SelectCommon selectCommon : selects) {
            queryBuilder.append(clause);
            selectCommon.sqlQuery(queryBuilder);
        }
    }

    @Override
    public final SelectPaginationProvider forUpdate() {
        return lockMode(LockMode.FOR_UPDATE);
    }

    @Override
    public final SelectPaginationProvider forUpdateNoWait() {
        return lockMode(LockMode.FOR_UPDATE_NOWAIT);
    }

    @Override
    public SelectWhere where() {
        return where;
    }

    @Override
    public SelectGroupBy groupBy(String... fields ) {
        return groupBy.fields(fields);
    }

    @Override
    public SelectOrderBy orderBy() {
        return orderBy;
    }

    @Override
    public Select<TYPE> selectFields(String[] fields) {
        selectFields = fields;
        return this;
    }

}
