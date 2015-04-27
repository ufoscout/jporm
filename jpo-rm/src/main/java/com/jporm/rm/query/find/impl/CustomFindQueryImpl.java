/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rm.query.find.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.rm.query.find.CustomFindQuery;
import com.jporm.rm.query.find.CustomFindQueryGroupBy;
import com.jporm.rm.query.find.CustomFindQueryOrderBy;
import com.jporm.rm.query.find.CustomFindQueryWhere;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.dialect.DBType;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.SelectCommon;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 * @author Francesco Cina 20/giu/2011
 */
public class CustomFindQueryImpl extends CommonFindQueryImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy> implements CustomFindQuery {

	private final CustomFindQueryGroupByImpl groupBy;
	private final SqlExecutor sqlExecutor;
	private final DBType dbType;

	public CustomFindQueryImpl(final String[] selectFields, final ServiceCatalog serviceCatalog, SqlExecutor sqlExecutor, final Class<?> clazz,
			final String alias, SqlFactory sqlFactory, DBType dbType) {
		super(clazz, alias, sqlFactory, serviceCatalog.getClassToolMap());
		this.sqlExecutor = sqlExecutor;
		this.dbType = dbType;
		Select select = getSelect();
		select.selectFields(selectFields);
		groupBy = new CustomFindQueryGroupByImpl(select.groupBy(), this);
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new CustomFindQueryWhereImpl(select.where(), this));
		setOrderBy(new CustomFindQueryOrderByImpl(select.orderBy(), this));
	}

	@Override
	public <T> T fetch(final ResultSetReader<T> rse) throws JpoException {
		return getExecutor().query(renderSql(), rse, getValues());
	}

	@Override
	public <T> List<T> fetch(final ResultSetRowReader<T> rsrr) throws JpoException {
		return getExecutor().query(renderSql(), rsrr, getValues());
	}

	@Override
	public BigDecimal fetchBigDecimal() throws JpoException {
		return getExecutor().queryForBigDecimal(renderSql(), getValues());
	}

	@Override
	public Optional<BigDecimal> fetchBigDecimalOptional() throws JpoException {
		return Optional.ofNullable(fetchBigDecimal());
	}

	@Override
	public BigDecimal fetchBigDecimalUnique() throws JpoException {
		return getExecutor().queryForBigDecimalUnique(renderSql(),
				getValues());
	}

	@Override
	public Boolean fetchBoolean() throws JpoException {
		return getExecutor().queryForBoolean(renderSql(), getValues());
	}

	@Override
	public Optional<Boolean> fetchBooleanOptional() throws JpoException {
		return Optional.ofNullable(fetchBoolean());
	}

	@Override
	public Boolean fetchBooleanUnique() throws JpoException {
		return getExecutor().queryForBooleanUnique(renderSql(),
				getValues());
	}

	@Override
	public Double fetchDouble() {
		return getExecutor().queryForDouble(renderSql(), getValues());
	}

	@Override
	public Optional<Double> fetchDoubleOptional() {
		return Optional.ofNullable(fetchDouble());
	}

	@Override
	public Double fetchDoubleUnique() throws JpoException {
		return getExecutor().queryForDoubleUnique(renderSql(),
				getValues());
	}

	private SqlExecutor getExecutor() {
		return sqlExecutor;
	}

	@Override
	public Float fetchFloat() {
		return getExecutor().queryForFloat(renderSql(), getValues());
	}

	@Override
	public Optional<Float> fetchFloatOptional() {
		return Optional.ofNullable(fetchFloat());
	}

	@Override
	public Float fetchFloatUnique() throws JpoException {
		return getExecutor()
				.queryForFloatUnique(renderSql(), getValues());
	}

	@Override
	public Integer fetchInt() {
		return getExecutor().queryForInt(renderSql(), getValues());
	}

	@Override
	public Optional<Integer> fetchIntOptional() {
		return Optional.ofNullable(fetchInt());
	}

	@Override
	public Integer fetchIntUnique() throws JpoException {
		return getExecutor().queryForIntUnique(renderSql(), getValues());
	}

	@Override
	public Long fetchLong() {
		return getExecutor().queryForLong(renderSql(), getValues());
	}

	@Override
	public Optional<Long> fetchLongOptional() {
		return Optional.ofNullable(fetchLong());
	}

	@Override
	public Long fetchLongUnique() throws JpoException {
		return getExecutor().queryForLongUnique(renderSql(), getValues());
	}

	@Override
	public String fetchString() {
		return getExecutor().queryForString(renderSql(), getValues());
	}

	@Override
	public Optional<String> fetchStringOptional() {
		return Optional.ofNullable(fetchString());
	}

	@Override
	public String fetchStringUnique() throws JpoException {
		final List<Object> values = new ArrayList<Object>();
		sql().appendValues(values);
		return getExecutor().queryForStringUnique(renderSql(), values);
	}

	@Override
	public <T> T fetchUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
		final List<Object> values = new ArrayList<Object>();
		sql().appendValues(values);
		return getExecutor().queryForUnique(renderSql(), rsrr, values);
	}

	private List<Object> getValues() {
		final List<Object> values = new ArrayList<Object>();
		sql().appendValues(values);
		return values;
	}

	@Override
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		groupBy.fields(fields);
		return groupBy;
	}

	@Override
	public SelectCommon sql() {
		return getSelect();
	}

	@Override
	public String renderSql() {
		return sql().renderSql(dbType.getDBProfile());
	}

}
