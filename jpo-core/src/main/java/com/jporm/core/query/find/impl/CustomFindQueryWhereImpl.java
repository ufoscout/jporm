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
import com.jporm.commons.core.query.find.impl.CommonFindQueryWhereImpl;
import com.jporm.core.query.find.CustomFindQuery;
import com.jporm.core.query.find.CustomFindQueryGroupBy;
import com.jporm.core.query.find.CustomFindQueryOrderBy;
import com.jporm.core.query.find.CustomFindQueryWhere;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 * @author ufo
 */
public class CustomFindQueryWhereImpl extends CommonFindQueryWhereImpl<CustomFindQuery, CustomFindQueryWhere, CustomFindQueryOrderBy> implements CustomFindQueryWhere {

	public CustomFindQueryWhereImpl(com.jporm.sql.query.clause.Where sqlWhere, final CustomFindQuery customFindQuery) {
		super(sqlWhere, customFindQuery);
	}

	@Override
	public <T> T get(final ResultSetReader<T> rse) throws JpoException {
		return  root().get(rse);
	}

	@Override
	public <T> List<T> get(final ResultSetRowReader<T> rsrr) throws JpoException {
		return  root().get(rsrr);
	}

	@Override
	public BigDecimal getBigDecimal() {
		return  root().getBigDecimal();
	}

	@Override
	public Optional<BigDecimal> getBigDecimalOptional() throws JpoException {
		return  root().getBigDecimalOptional();
	}

	@Override
	public BigDecimal getBigDecimalUnique() throws JpoException {
		return  root().getBigDecimalUnique();
	}

	@Override
	public Boolean getBoolean() {
		return  root().getBoolean();
	}

	@Override
	public Optional<Boolean> getBooleanOptional() throws JpoException {
		return  root().getBooleanOptional();
	}

	@Override
	public Boolean getBooleanUnique() throws JpoException {
		return  root().getBooleanUnique();
	}

	@Override
	public Double getDouble() {
		return  root().getDouble();
	}

	@Override
	public Optional<Double> getDoubleOptional() {
		return  root().getDoubleOptional();
	}

	@Override
	public Double getDoubleUnique() throws JpoException {
		return  root().getDoubleUnique();
	}

	@Override
	public Float getFloat() {
		return  root().getFloat();
	}

	@Override
	public Optional<Float> getFloatOptional() {
		return  root().getFloatOptional();
	}

	@Override
	public Float getFloatUnique() throws JpoException {
		return  root().getFloatUnique();
	}

	@Override
	public Integer getInt() {
		return  root().getInt();
	}

	@Override
	public Optional<Integer> getIntOptional() {
		return  root().getIntOptional();
	}

	@Override
	public Integer getIntUnique() throws JpoException {
		return  root().getIntUnique();
	}

	@Override
	public Long getLong() {
		return  root().getLong();
	}

	@Override
	public Optional<Long> getLongOptional() {
		return  root().getLongOptional();
	}

	@Override
	public Long getLongUnique() throws JpoException {
		return  root().getLongUnique();
	}

	@Override
	public String getString() {
		return  root().getString();
	}

	@Override
	public Optional<String> getStringOptional() {
		return  root().getStringOptional();
	}

	@Override
	public String getStringUnique() throws JpoException {
		return  root().getStringUnique();
	}

	@Override
	public <T> T getUnique(final ResultSetRowReader<T> rsrr) throws JpoException, JpoNotUniqueResultException {
		return  root().getUnique(rsrr);
	}

	@Override
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		return  root().groupBy(fields);
	}

}
