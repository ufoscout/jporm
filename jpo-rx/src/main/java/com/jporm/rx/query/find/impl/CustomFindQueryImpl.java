/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rx.query.find.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.find.impl.CommonFindFromImpl;
import com.jporm.commons.core.query.find.impl.CommonFindQueryImpl;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.query.find.CustomFindQueryGroupBy;
import com.jporm.rx.query.find.CustomFindQueryOrderBy;
import com.jporm.rx.query.find.CustomFindQueryWhere;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.sql.SqlFactory;
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

	public CustomFindQueryImpl(final String[] selectFields, final ServiceCatalog serviceCatalog, final Class<?> clazz,
			final String alias, SqlExecutor sqlExecutor, SqlFactory sqlFactory) {
		super(clazz, alias, sqlFactory, serviceCatalog.getClassToolMap());
		this.sqlExecutor = sqlExecutor;
		Select select = getSelect();
		select.selectFields(selectFields);
		groupBy = new CustomFindQueryGroupByImpl(select.groupBy(), this);
		setFrom(new CommonFindFromImpl<>(select.from(), this));
		setWhere(new CustomFindQueryWhereImpl(select.where(), this));
		setOrderBy(new CustomFindQueryOrderByImpl(select.orderBy(), this));
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
	public <T> CompletableFuture<T> fetch(ResultSetReader<T> rsr) {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.query(sql().renderSql(dbType.getDBProfile()), rsr, getParams());
		});
	}

	private List<Object> getParams() {
		final List<Object> params = new ArrayList<>();
		sql().appendValues(params);
		return params;
	}

	private <T> CompletableFuture<Optional<T>> toOptional(CompletableFuture<T> future) {
		return future.thenApply(value -> Optional.ofNullable(value));
	}

	@Override
	public <T> CompletableFuture<List<T>> fetch(ResultSetRowReader<T> rsrr) {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.query(sql().renderSql(dbType.getDBProfile()), rsrr, getParams());
		});
	}

	@Override
	public <T> CompletableFuture<T> fetchUnique(ResultSetRowReader<T> rsrr) {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForUnique(sql().renderSql(dbType.getDBProfile()), rsrr, getParams());
		});
	}

	@Override
	public CompletableFuture<BigDecimal> fetchBigDecimal() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForBigDecimal(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Optional<BigDecimal>> fetchBigDecimalOptional() {
		return toOptional(fetchBigDecimal());
	}

	@Override
	public CompletableFuture<BigDecimal> fetchBigDecimalUnique() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForBigDecimalUnique(sql().renderSql(dbType.getDBProfile()), getParams());
		});	}

	@Override
	public CompletableFuture<Boolean> fetchBoolean() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForBoolean(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Optional<Boolean>> fetchBooleanOptional() {
		return toOptional(fetchBoolean());
	}

	@Override
	public CompletableFuture<Boolean> fetchBooleanUnique() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForBooleanUnique(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Double> fetchDouble() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForDouble(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Optional<Double>> fetchDoubleOptional() {
		return toOptional(fetchDouble());
	}

	@Override
	public CompletableFuture<Double> fetchDoubleUnique() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForDoubleUnique(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Float> fetchFloat() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForFloat(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Optional<Float>> fetchFloatOptional() {
		return toOptional(fetchFloat());
	}

	@Override
	public CompletableFuture<Float> fetchFloatUnique() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForFloatUnique(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Integer> fetchInt() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForInt(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Optional<Integer>> fetchIntOptional() {
		return toOptional(fetchInt());
	}

	@Override
	public CompletableFuture<Integer> fetchIntUnique() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForIntUnique(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Long> fetchLong() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForLong(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Optional<Long>> fetchLongOptional() {
		return toOptional(fetchLong());
	}

	@Override
	public CompletableFuture<Long> fetchLongUnique() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForLongUnique(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<String> fetchString() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForString(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

	@Override
	public CompletableFuture<Optional<String>> fetchStringOptional() {
		return toOptional(fetchString());
	}

	@Override
	public CompletableFuture<String> fetchStringUnique() {
		return sqlExecutor.dbType().thenCompose(dbType -> {
			return sqlExecutor.queryForStringUnique(sql().renderSql(dbType.getDBProfile()), getParams());
		});
	}

}
