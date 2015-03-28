/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.core.query.find.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.find.impl.CommonFindQueryOrderByImpl;
import com.jporm.rx.core.query.find.CustomFindQuery;
import com.jporm.rx.core.query.find.CustomFindQueryGroupBy;
import com.jporm.rx.core.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.core.query.find.CustomFindQueryWhere;
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
	public CustomFindQueryGroupBy groupBy(final String... fields) throws JpoException {
		return root().groupBy(fields);
	}


	@Override
	public <T> CompletableFuture<T> get(ResultSetReader<T> rsr) {
		return root().get(rsr);
	}

	@Override
	public <T> CompletableFuture<List<T>> get(ResultSetRowReader<T> rsrr) {
		return root().get(rsrr);
	}

	@Override
	public CompletableFuture<BigDecimal> getBigDecimal() {
		return root().getBigDecimal();
	}

	@Override
	public CompletableFuture<Optional<BigDecimal>> getBigDecimalOptional() {
		return root().getBigDecimalOptional();
	}

	@Override
	public CompletableFuture<BigDecimal> getBigDecimalUnique() {
		return root().getBigDecimalUnique();
	}

	@Override
	public CompletableFuture<Boolean> getBoolean() {
		return root().getBoolean();
	}

	@Override
	public CompletableFuture<Optional<Boolean>> getBooleanOptional() {
		return root().getBooleanOptional();
	}

	@Override
	public CompletableFuture<Boolean> getBooleanUnique() {
		return root().getBooleanUnique();
	}

	@Override
	public CompletableFuture<Double> getDouble() {
		return root().getDouble();
	}

	@Override
	public CompletableFuture<Optional<Double>> getDoubleOptional() {
		return root().getDoubleOptional();
	}

	@Override
	public CompletableFuture<Double> getDoubleUnique() {
		return root().getDoubleUnique();
	}

	@Override
	public CompletableFuture<Float> getFloat() {
		return root().getFloat();
	}

	@Override
	public CompletableFuture<Optional<Float>> getFloatOptional() {
		return root().getFloatOptional();
	}

	@Override
	public CompletableFuture<Float> getFloatUnique() {
		return root().getFloatUnique();
	}

	@Override
	public CompletableFuture<Integer> getInt() {
		return root().getInt();
	}

	@Override
	public CompletableFuture<Optional<Integer>> getIntOptional() {
		return root().getIntOptional();
	}

	@Override
	public CompletableFuture<Integer> getIntUnique() {
		return root().getIntUnique();
	}

	@Override
	public CompletableFuture<Long> getLong() {
		return root().getLong();
	}

	@Override
	public CompletableFuture<Optional<Long>> getLongOptional() {
		return root().getLongOptional();
	}

	@Override
	public CompletableFuture<Long> getLongUnique() {
		return root().getLongUnique();
	}

	@Override
	public CompletableFuture<String> getString() {
		return root().getString();
	}

	@Override
	public CompletableFuture<Optional<String>> getStringOptional() {
		return root().getStringOptional();
	}

	@Override
	public CompletableFuture<String> getStringUnique() {
		return root().getStringUnique();
	}

	@Override
	public <T> CompletableFuture<T> getUnique(ResultSetRowReader<T> rsrr) {
		return root().getUnique(rsrr);
	}

}
