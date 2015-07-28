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

import com.jporm.rx.query.find.CustomFindQueryCommon;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.query.find.CustomFindQueryCommonSync;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

public abstract class CustomFindQueryCommonSyncAbstract implements CustomFindQueryCommonSync {


	@Override
	public <T> T fetch(ResultSetReader<T> rsr) {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetch(rsr));
	}

	@Override
	public <T> List<T> fetch(ResultSetRowReader<T> rsrr) {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetch(rsrr));

	}

	@Override
	public BigDecimal fetchBigDecimal() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchBigDecimal());

	}

	@Override
	public Optional<BigDecimal> fetchBigDecimalOptional() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchBigDecimalOptional());
	}

	@Override
	public BigDecimal fetchBigDecimalUnique() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchBigDecimalUnique());
	}

	@Override
	public Boolean fetchBoolean() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchBoolean());
	}

	@Override
	public Optional<Boolean> fetchBooleanOptional() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchBooleanOptional());
	}

	@Override
	public Boolean fetchBooleanUnique() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchBooleanUnique());
	}

	@Override
	public Double fetchDouble() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchDouble());
	}

	@Override
	public Optional<Double> fetchDoubleOptional() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchDoubleOptional());
	}

	@Override
	public Double fetchDoubleUnique() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchDoubleUnique());
	}

	@Override
	public Float fetchFloat() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchFloat());
	}

	@Override
	public Optional<Float> fetchFloatOptional() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchFloatOptional());
	}

	@Override
	public Float fetchFloatUnique() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchFloatUnique());
	}

	@Override
	public Integer fetchInt() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchInt());
	}

	@Override
	public Optional<Integer> fetchIntOptional() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchIntOptional());
	}

	@Override
	public Integer fetchIntUnique() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchIntUnique());
	}

	@Override
	public Long fetchLong() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchLong());
	}

	@Override
	public Optional<Long> fetchLongOptional() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchLongOptional());
	}

	@Override
	public Long fetchLongUnique() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchLongUnique());
	}

	@Override
	public String fetchString() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchString());
	}

	@Override
	public Optional<String> fetchStringOptional() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchStringOptional());
	}

	@Override
	public String fetchStringUnique() {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchStringUnique());
	}

	@Override
	public <T> T fetchUnique(ResultSetRowReader<T> rsrr) {
		return JpoCompletableWrapper.get(getCustomFindQuery().fetchUnique(rsrr));
	}

	protected abstract CustomFindQueryCommon getCustomFindQuery();

}
