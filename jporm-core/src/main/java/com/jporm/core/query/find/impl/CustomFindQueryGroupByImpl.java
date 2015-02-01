/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.core.query.find.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.jporm.annotation.LockMode;
import com.jporm.core.exception.JpoException;
import com.jporm.core.exception.JpoNotUniqueResultException;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.query.ResultSetRowReader;
import com.jporm.core.query.clause.impl.GroupByImpl;
import com.jporm.core.query.find.CustomFindQuery;
import com.jporm.core.query.find.CustomFindQueryGroupBy;
import com.jporm.core.query.find.CustomFindQueryOrderBy;
import com.jporm.core.query.find.CustomFindQueryWhere;
import com.jporm.sql.query.clause.WhereExpressionElement;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CustomFindQueryGroupByImpl extends GroupByImpl<CustomFindQuery> implements CustomFindQueryGroupBy {

	private final CustomFindQuery customFindQuery;

	public CustomFindQueryGroupByImpl(com.jporm.sql.query.clause.GroupBy sqlGroupBy, final CustomFindQuery customFindQuery) {
		super(sqlGroupBy);
		this.customFindQuery = customFindQuery;
	}

	@Override
	public void appendValues(final List<Object> values) {
		customFindQuery.appendValues(values);
	}

	@Override
	public CustomFindQuery distinct(final boolean distinct) throws JpoException {
		return customFindQuery.distinct(distinct);
	}

	@Override
	public CustomFindQuery firstRow(final int firstRow) throws JpoException {
		return customFindQuery.firstRow(firstRow);
	}

	@Override
	public Object[] get() {
		return customFindQuery.get();
	}

	@Override
	public <T> T get(final ResultSetReader<T> rsr) throws JpoException {
		return customFindQuery.get(rsr);
	}

	@Override
	public <T> List<T> get(final ResultSetRowReader<T> rsrr) throws JpoException {
		return customFindQuery.get(rsrr);
	}

	@Override
	public BigDecimal getBigDecimal() {
		return customFindQuery.getBigDecimal();
	}

	@Override
	public Optional<BigDecimal> getBigDecimalOptional() throws JpoException {
		return customFindQuery.getBigDecimalOptional();
	}

	@Override
	public BigDecimal getBigDecimalUnique() throws JpoException {
		return customFindQuery.getBigDecimalUnique();
	}

	@Override
	public Boolean getBoolean() {
		return customFindQuery.getBoolean();
	}

	@Override
	public Optional<Boolean> getBooleanOptional() throws JpoException {
		return customFindQuery.getBooleanOptional();
	}

	@Override
	public Boolean getBooleanUnique() throws JpoException {
		return customFindQuery.getBooleanUnique();
	}

	@Override
	public Double getDouble() {
		return customFindQuery.getDouble();
	}

	@Override
	public Optional<Double> getDoubleOptional() {
		return customFindQuery.getDoubleOptional();
	}

	@Override
	public Double getDoubleUnique() throws JpoException {
		return customFindQuery.getDoubleUnique();
	}

	@Override
	public Float getFloat() {
		return customFindQuery.getFloat();
	}

	@Override
	public Optional<Float> getFloatOptional() {
		return customFindQuery.getFloatOptional();
	}

	@Override
	public Float getFloatUnique() throws JpoException {
		return customFindQuery.getFloatUnique();
	}

	@Override
	public Integer getInt() {
		return customFindQuery.getInt();
	}

	@Override
	public Optional<Integer> getIntOptional() {
		return customFindQuery.getIntOptional();
	}

	@Override
	public Integer getIntUnique() throws JpoException {
		return customFindQuery.getIntUnique();
	}

	@Override
	public List<Object[]> getList() throws JpoException {
		return customFindQuery.getList();
	}

	@Override
	public Long getLong() {
		return customFindQuery.getLong();
	}

	@Override
	public Optional<Long> getLongOptional() {
		return customFindQuery.getLongOptional();
	}

	@Override
	public Long getLongUnique() throws JpoException {
		return customFindQuery.getLongUnique();
	}

	@Override
	public Optional<Object[]> getOptional() {
		return customFindQuery.getOptional();
	}

	@Override
	public String getString() {
		return customFindQuery.getString();
	}

	@Override
	public Optional<String> getStringOptional() {
		return customFindQuery.getStringOptional();
	}

	@Override
	public String getStringUnique() throws JpoException {
		return customFindQuery.getStringUnique();
	}

	@Override
	public int getTimeout() {
		return customFindQuery.getTimeout();
	}

	@Override
	public Object[] getUnique() {
		return customFindQuery.getUnique();
	}

	@Override
	public <T> T getUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
		return customFindQuery.getUnique(rsrr);
	}

	@Override
	public CustomFindQuery lockMode(final LockMode lockMode) {
		return customFindQuery.lockMode(lockMode);
	}

	@Override
	public CustomFindQuery maxRows(final int maxRows) throws JpoException {
		return customFindQuery.maxRows(maxRows);
	}

	@Override
	public CustomFindQueryOrderBy orderBy() throws JpoException {
		return customFindQuery.orderBy();
	}

	@Override
	public String renderSql() {
		return customFindQuery.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder queryBuilder) {
		customFindQuery.renderSql(queryBuilder);
	}

	@Override
	protected CustomFindQuery sqlQuery() {
		return customFindQuery;
	}

	@Override
	public CustomFindQuery timeout(final int queryTimeout) {
		return customFindQuery.timeout(queryTimeout);
	}

	@Override
	public CustomFindQueryWhere where(final List<WhereExpressionElement> expressionElements) {
		return customFindQuery.where(expressionElements);
	}

	@Override
	public CustomFindQueryWhere where(final String customClause, final Object... args) {
		return customFindQuery.where(customClause, args);
	}

	@Override
	public CustomFindQueryWhere where(final WhereExpressionElement... expressionElements) {
		return customFindQuery.where(expressionElements);
	}

	@Override
	public int getVersion() {
		return customFindQuery.getVersion();
	}

}
