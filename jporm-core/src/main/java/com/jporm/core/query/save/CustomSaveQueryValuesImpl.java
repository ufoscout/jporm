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
package com.jporm.core.query.save;

import java.util.List;

import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.clause.ValuesImpl;
import com.jporm.query.save.CustomSaveQuery;
import com.jporm.query.save.CustomSaveQueryValues;

/**
 *
 * @author ufo
 *
 */
public class CustomSaveQueryValuesImpl<BEAN> extends ValuesImpl<BEAN, CustomSaveQueryValues> implements CustomSaveQueryValues {

	private final CustomSaveQuery query;

	public CustomSaveQueryValuesImpl(final CustomSaveQuery query, Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		super(clazz, serviceCatalog);
		this.query = query;
	}

	@Override
	public String renderSql() {
		return query.renderSql();
	}

	@Override
	public void renderSql(final StringBuilder stringBuilder) {
		query.renderSql(stringBuilder);
	}

	@Override
	public void appendValues(final List<Object> values) {
		query.appendValues(values);
	}

	@Override
	public final int now() {
		return query.now();
	}

	@Override
	public CustomSaveQuery query() {
		return query;
	}

	@Override
	public int getTimeout() {
		return query.getTimeout();
	}

	@Override
	public void execute() {
		query.execute();
	}

	@Override
	public boolean isExecuted() {
		return query.isExecuted();
	}

	@Override
	public CustomSaveQuery timeout(int seconds) {
		return query.timeout(seconds);
	}

	@Override
	protected CustomSaveQueryValues values() {
		return this;
	}

}
