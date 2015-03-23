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

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.query.find.impl.CommonFindQueryOrderByImpl;
import com.jporm.core.query.find.CustomFindQuery;
import com.jporm.core.query.find.CustomFindQueryGroupBy;
import com.jporm.core.query.find.CustomFindQueryOrderBy;
import com.jporm.core.query.find.CustomFindQueryWhere;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 * @author ufo
 */
public class CustomFindQueryOrderByImpl extends CommonFindQueryOrderByImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy> implements CustomFindQueryOrderBy {

	public CustomFindQueryOrderByImpl(com.jporm.sql.query.clause.OrderBy sqlOrderBy, final CustomFindQuery customFindQuery) {
		super(sqlOrderBy, customFindQuery);
	}

	@Override
	public <T> T get(final ResultSetReader<T> rse) throws JpoException {
		return query().get(rse);
	}

	@Override
	public <T> List<T> get(final ResultSetRowReader<T> rsrr) throws JpoException {
		return query().get(rsrr);
	}

	@Override
	public BigDecimal getBigDecimal() {
		return query().getBigDecimal();
	}

	@Override
	public Optional<BigDecimal> getBigDecimalOptional() throws JpoException {
		return query().getBigDecimalOptional();
	}

	@Override
	public BigDecimal getBigDecimalUnique() throws JpoException {
		return query().getBigDecimalUnique();
	}

	@Override
	public Boolean getBoolean() {
		return query().getBoolean();
	}

	@Override
	public Optional<Boolean> getBooleanOptional() throws JpoException {
		return query().getBooleanOptional();
	}

	@Override
	public Boolean getBooleanUnique() throws JpoException {
		return query().getBooleanUnique();
	}

	@Override
	public Double getDouble() {
		return query().getDouble();
	}

	@Override
	public Optional<Double> getDoubleOptional() {
		return query().getDoubleOptional();
	}

	@Override
	public Double getDoubleUnique() throws JpoException {
		return query().getDoubleUnique();
	}

	@Override
	public Float getFloat() {
		return query().getFloat();
	}

	@Override
	public Optional<Float> getFloatOptional() {
		return query().getFloatOptional();
	}

	@Override
	public Float getFloatUnique() throws JpoException {
		return query().getFloatUnique();
	}

	@Override
	public Integer getInt() {
		return query().getInt();
	}

	@Override
	public Optional<Integer> getIntOptional() {
		return query().getIntOptional();
	}

	@Override
	public Integer getIntUnique() throws JpoException {
		return query().getIntUnique();
	}

	@Override
	public Long getLong() {
		return query().getLong();
	}

	@Override
	public Optional<Long> getLongOptional() {
		return query().getLongOptional();
	}

	@Override
	public Long getLongUnique() throws JpoException {
		return query().getLongUnique();
	}

	@Override
	public String getString() {
		return query().getString();
	}

	@Override
	public Optional<String> getStringOptional() {
		return query().getStringOptional();
	}

	@Override
	public String getStringUnique() throws JpoException {
		return query().getStringUnique();
	}

	@Override
	public <T> T getUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
		return query().getUnique(rsrr);
	}

	@Override
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		return query().groupBy(fields);
	}

}
