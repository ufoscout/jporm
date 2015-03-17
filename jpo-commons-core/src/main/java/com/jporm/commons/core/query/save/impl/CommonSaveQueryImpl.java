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
package com.jporm.commons.core.query.save.impl;

import java.util.List;

import com.jporm.commons.core.inject.ServiceCatalog;
import com.jporm.commons.core.query.AQueryRoot;
import com.jporm.commons.core.query.save.CommonSaveQuery;
import com.jporm.commons.core.query.save.CommonSaveQueryValues;
import com.jporm.sql.query.clause.Insert;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CommonSaveQueryImpl<SAVE extends CommonSaveQuery<SAVE, VALUES>,
								VALUES extends CommonSaveQueryValues<SAVE, VALUES>>
							extends AQueryRoot implements CommonSaveQuery<SAVE, VALUES> {

	private VALUES elemValues;
	private final Insert insert;

	public CommonSaveQueryImpl(final Class<?> clazz, final ServiceCatalog<?> serviceCatalog) {
		super(serviceCatalog.getSqlCache());
		insert = serviceCatalog.getSqlFactory().insert(clazz);
	}

	@Override
	public final void appendValues(final List<Object> values) {
		insert.appendValues(values);
	}

	@Override
	public final int getVersion() {
		return insert.getVersion();
	}

	@Override
	public final void renderSql(final StringBuilder queryBuilder) {
		insert.renderSql(queryBuilder);
	}

	@Override
	public final VALUES values() {
		return elemValues;
	}

	@Override
	public final SAVE useGenerators(boolean useGenerators) {
		insert.useGenerators(useGenerators);
		return (SAVE) this;
	}

	/**
	 * @param elemValues the elemValues to set
	 */
	public final void setElemValues(VALUES elemValues) {
		this.elemValues = elemValues;
	}

	/**
	 * @return the insert
	 */
	public Insert query() {
		return insert;
	}
}