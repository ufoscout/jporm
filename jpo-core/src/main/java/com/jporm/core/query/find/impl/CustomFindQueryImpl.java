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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.query.ResultSetRowReader;
import com.jporm.core.query.find.CustomFindQuery;
import com.jporm.core.query.find.CustomFindQueryGroupBy;
import com.jporm.core.query.find.CustomFindQueryOrderBy;
import com.jporm.core.query.find.CustomFindQueryWhere;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlExecutor;
import com.jporm.sql.query.clause.Select;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomFindQueryImpl extends CommonFindQueryImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy> implements CustomFindQuery {

	private final CustomFindQueryGroupByImpl groupBy;
	private final ServiceCatalog<Session> serviceCatalog;

	public CustomFindQueryImpl(final String[] selectFields, final ServiceCatalog<Session> serviceCatalog, final Class<?> clazz,
			final String alias) {
		super(serviceCatalog, clazz, alias);
		this.serviceCatalog = serviceCatalog;
		Select select = getSelect();
		select.selectFields(selectFields);
		groupBy = new CustomFindQueryGroupByImpl(select.groupBy(), this);
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new CustomFindQueryWhereImpl(select.where(), this));
		setOrderBy(new CustomFindQueryOrderByImpl(select.orderBy(), this));
	}

	@Override
	public Object[] get() {
		return getExecutor()
				.queryForArray(renderSql(), getValues());
	}

	@Override
	public <T> T get(final ResultSetReader<T> rse) throws JpoException {
		return getExecutor().query(renderSql(), rse, getValues());
	}

	@Override
	public <T> List<T> get(final ResultSetRowReader<T> rsrr) throws JpoException {
		return getExecutor().query(renderSql(), rsrr, getValues());
	}

	@Override
	public BigDecimal getBigDecimal() throws JpoException {
		return getExecutor().queryForBigDecimal(renderSql(), getValues());
	}

	@Override
	public Optional<BigDecimal> getBigDecimalOptional() throws JpoException {
		return Optional.ofNullable(getBigDecimal());
	}

	@Override
	public BigDecimal getBigDecimalUnique() throws JpoException {
		return getExecutor().queryForBigDecimalUnique(renderSql(),
				getValues());
	}

	@Override
	public Boolean getBoolean() throws JpoException {
		return getExecutor().queryForBoolean(renderSql(), getValues());
	}

	@Override
	public Optional<Boolean> getBooleanOptional() throws JpoException {
		return Optional.ofNullable(getBoolean());
	}

	@Override
	public Boolean getBooleanUnique() throws JpoException {
		return getExecutor().queryForBooleanUnique(renderSql(),
				getValues());
	}

	@Override
	public Double getDouble() {
		return getExecutor().queryForDouble(renderSql(), getValues());
	}

	@Override
	public Optional<Double> getDoubleOptional() {
		return Optional.ofNullable(getDouble());
	}

	@Override
	public Double getDoubleUnique() throws JpoException {
		return getExecutor().queryForDoubleUnique(renderSql(),
				getValues());
	}

	private SqlExecutor getExecutor() {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		return sqlExec;
	}

	@Override
	public Float getFloat() {
		return getExecutor().queryForFloat(renderSql(), getValues());
	}

	@Override
	public Optional<Float> getFloatOptional() {
		return Optional.ofNullable(getFloat());
	}

	@Override
	public Float getFloatUnique() throws JpoException {
		return getExecutor()
				.queryForFloatUnique(renderSql(), getValues());
	}

	@Override
	public Integer getInt() {
		return getExecutor().queryForInt(renderSql(), getValues());
	}

	@Override
	public Optional<Integer> getIntOptional() {
		return Optional.ofNullable(getInt());
	}

	@Override
	public Integer getIntUnique() throws JpoException {
		return getExecutor().queryForIntUnique(renderSql(), getValues());
	}

	@Override
	public List<Object[]> getList() {
		return getExecutor().queryForList(renderSql(), getValues());
	}

	@Override
	public Long getLong() {
		return getExecutor().queryForLong(renderSql(), getValues());
	}

	@Override
	public Optional<Long> getLongOptional() {
		return Optional.ofNullable(getLong());
	}

	@Override
	public Long getLongUnique() throws JpoException {
		return getExecutor().queryForLongUnique(renderSql(), getValues());
	}

	@Override
	public Optional<Object[]> getOptional() {
		return Optional.ofNullable(get());
	}

	@Override
	public String getString() {
		return getExecutor().queryForString(renderSql(), getValues());
	}

	@Override
	public Optional<String> getStringOptional() {
		return Optional.ofNullable(getString());
	}

	@Override
	public String getStringUnique() throws JpoException {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		return sqlExec.queryForStringUnique(renderSql(), values);
	}

	@Override
	public Object[] getUnique() {
		return getExecutor()
				.queryForArrayUnique(renderSql(), getValues());
	}

	@Override
	public <T> T getUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
		return sqlExec.queryForUnique(renderSql(), rsrr, values);
	}

	private List<Object> getValues() {
		final List<Object> values = new ArrayList<Object>();
		appendValues(values);
		return values;
	}

	@Override
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		groupBy.fields(fields);
		return groupBy;
	}

}
