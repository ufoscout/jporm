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
import java.util.function.Supplier;

import com.jporm.sql.dialect.SqlSelectRender;
import com.jporm.sql.query.processor.PropertiesProcessor;
import com.jporm.sql.query.processor.TableName;
import com.jporm.sql.query.processor.TablePropertiesProcessor;
import com.jporm.sql.query.select.from.FromImpl;
import com.jporm.sql.query.select.groupby.SelectGroupBy;
import com.jporm.sql.query.select.groupby.SelectGroupByImpl;
import com.jporm.sql.query.select.orderby.SelectOrderByImpl;
import com.jporm.sql.query.select.pagination.SelectPaginationProvider;
import com.jporm.sql.query.select.unions.SelectUnionsProvider;
import com.jporm.sql.query.select.where.SelectWhereImpl;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectImpl<TYPE> extends FromImpl<TYPE, Select<TYPE>> implements Select<TYPE> {

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
    private final Supplier<String[]> selectFields;
    private final SqlSelectRender selectRender;

    public SelectImpl(SqlSelectRender selectRender, Supplier<String[]> selectFields, final TYPE tableNameSource, final TablePropertiesProcessor<TYPE> propertiesProcessor) {
        this(selectRender, selectFields, propertiesProcessor.getTableName(tableNameSource), propertiesProcessor);
    }

    public SelectImpl(SqlSelectRender selectRender, Supplier<String[]> selectFields, final TYPE tableNameSource, final TablePropertiesProcessor<TYPE> propertiesProcessor, final String alias) {
        this(selectRender, selectFields, propertiesProcessor.getTableName(tableNameSource, alias), propertiesProcessor);
    }

    private SelectImpl(SqlSelectRender selectRender, Supplier<String[]> selectFields, final TableName tableName, final TablePropertiesProcessor<TYPE> propertiesProcessor) {
        super(tableName, propertiesProcessor);
        this.selectFields = selectFields;
        this.propertiesProcessor = propertiesProcessor;
        this.selectRender = selectRender;
        where = new SelectWhereImpl(this);
        orderBy = new SelectOrderByImpl(this);
        groupBy = new SelectGroupByImpl(this);
    }

    @Override
    public final void sqlValues(final List<Object> values) {
        sqlElementValues(values);
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
        selectRender.renderWithoutPagination(this, queryBuilder);
        queryBuilder.append(") a "); //$NON-NLS-1$
        return queryBuilder.toString();
    }

    @Override
    public final void sqlQuery(final StringBuilder queryBuilder) {
        selectRender.render(this, queryBuilder);
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
    public final SelectPaginationProvider forUpdate() {
        return lockMode(LockMode.FOR_UPDATE);
    }

    @Override
    public final SelectPaginationProvider forUpdateNoWait() {
        return lockMode(LockMode.FOR_UPDATE_NOWAIT);
    }

    @Override
    public SelectWhereImpl where() {
        return where;
    }

    @Override
    public SelectGroupBy groupBy(String... fields ) {
        return groupBy.fields(fields);
    }

    public SelectGroupByImpl groupByImpl() {
        return groupBy;
    }

    @Override
    public SelectOrderByImpl orderBy() {
        return orderBy;
    }

    /**
     * @return the selectFields
     */
    public Supplier<String[]> getSelectFields() {
        return selectFields;
    }

    /**
     * @return the propertiesProcessor
     */
    public PropertiesProcessor getPropertiesProcessor() {
        return propertiesProcessor;
    }

    /**
     * @return the maxRows
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * @return the firstRow
     */
    public int getFirstRow() {
        return firstRow;
    }

    /**
     * @return the unions
     */
    public List<SelectCommon> getUnions() {
        return unions;
    }

    /**
     * @return the unionAlls
     */
    public List<SelectCommon> getUnionAlls() {
        return unionAlls;
    }

    /**
     * @return the intersects
     */
    public List<SelectCommon> getIntersects() {
        return intersects;
    }

    /**
     * @return the excepts
     */
    public List<SelectCommon> getExcepts() {
        return excepts;
    }

}
