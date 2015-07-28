/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.sync.query.find.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.query.find.CustomFindQuery;
import com.jporm.rx.sync.query.common.CommonWhereSync;
import com.jporm.rx.sync.query.find.CustomFindQueryGroupBySync;
import com.jporm.rx.sync.query.find.CustomFindQueryOrderBySync;
import com.jporm.rx.sync.query.find.CustomFindQuerySync;
import com.jporm.rx.sync.query.find.CustomFindQueryWhereSync;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

import co.paralleluniverse.fibers.Suspendable;

@Suspendable
public class CustomFindQueryWhereSyncImpl extends CommonWhereSync<CustomFindQueryWhereSync> implements CustomFindQueryWhereSync {

	private final CustomFindQuerySync customFindQuerySync;

	public CustomFindQueryWhereSyncImpl(CustomFindQuerySync customFindQuerySync, CustomFindQuery customFindQuery) {
		super(customFindQuery.where());
		this.customFindQuerySync = customFindQuerySync;
	}

	@Override
	public CustomFindQuerySync root() {
		return customFindQuerySync;
	}

	@Override
	public CustomFindQuerySync distinct() throws JpoException {
		return customFindQuerySync.distinct();
	}

	@Override
	public CustomFindQuerySync forUpdate() {
		return customFindQuerySync.forUpdate();
	}

	@Override
	public CustomFindQuerySync forUpdateNoWait() {
		return customFindQuerySync.forUpdateNoWait();
	}

	@Override
	public CustomFindQuerySync limit(int limit) throws JpoException {
		return customFindQuerySync.limit(limit);
	}

	@Override
	public CustomFindQuerySync offset(int offset) throws JpoException {
		return customFindQuerySync.offset(offset);
	}

	@Override
	public CustomFindQueryOrderBySync orderBy() throws JpoException {
		return customFindQuerySync.orderBy();
	}


	@Override
	public <T> T fetch(ResultSetReader<T> rsr) {
		return customFindQuerySync.fetch(rsr);
	}

	@Override
	public <T> List<T> fetch(ResultSetRowReader<T> rsrr) {
		return customFindQuerySync.fetch(rsrr);

	}

	@Override
	public BigDecimal fetchBigDecimal() {
		return customFindQuerySync.fetchBigDecimal();

	}

	@Override
	public Optional<BigDecimal> fetchBigDecimalOptional() {
		return customFindQuerySync.fetchBigDecimalOptional();
	}

	@Override
	public BigDecimal fetchBigDecimalUnique() {
		return customFindQuerySync.fetchBigDecimalUnique();
	}

	@Override
	public Boolean fetchBoolean() {
		return customFindQuerySync.fetchBoolean();
	}

	@Override
	public Optional<Boolean> fetchBooleanOptional() {
		return customFindQuerySync.fetchBooleanOptional();
	}

	@Override
	public Boolean fetchBooleanUnique() {
		return customFindQuerySync.fetchBooleanUnique();
	}

	@Override
	public Double fetchDouble() {
		return customFindQuerySync.fetchDouble();
	}

	@Override
	public Optional<Double> fetchDoubleOptional() {
		return customFindQuerySync.fetchDoubleOptional();
	}

	@Override
	public Double fetchDoubleUnique() {
		return customFindQuerySync.fetchDoubleUnique();
	}

	@Override
	public Float fetchFloat() {
		return customFindQuerySync.fetchFloat();
	}

	@Override
	public Optional<Float> fetchFloatOptional() {
		return customFindQuerySync.fetchFloatOptional();
	}

	@Override
	public Float fetchFloatUnique() {
		return customFindQuerySync.fetchFloatUnique();
	}

	@Override
	public Integer fetchInt() {
		return customFindQuerySync.fetchInt();
	}

	@Override
	public Optional<Integer> fetchIntOptional() {
		return customFindQuerySync.fetchIntOptional();
	}

	@Override
	public Integer fetchIntUnique() {
		return customFindQuerySync.fetchIntUnique();
	}

	@Override
	public Long fetchLong() {
		return customFindQuerySync.fetchLong();
	}

	@Override
	public Optional<Long> fetchLongOptional() {
		return customFindQuerySync.fetchLongOptional();
	}

	@Override
	public Long fetchLongUnique() {
		return customFindQuerySync.fetchLongUnique();
	}

	@Override
	public String fetchString() {
		return customFindQuerySync.fetchString();
	}

	@Override
	public Optional<String> fetchStringOptional() {
		return customFindQuerySync.fetchStringOptional();
	}

	@Override
	public String fetchStringUnique() {
		return customFindQuerySync.fetchStringUnique();
	}

	@Override
	public <T> T fetchUnique(ResultSetRowReader<T> rsrr) {
		return customFindQuerySync.fetchUnique(rsrr);
	}

	@Override
	public CustomFindQueryGroupBySync groupBy(String... fields) throws JpoException {
		return customFindQuerySync.groupBy(fields);
	}

	@Override
	protected CustomFindQueryWhereSync where() {
		return customFindQuerySync.where();
	}

}
