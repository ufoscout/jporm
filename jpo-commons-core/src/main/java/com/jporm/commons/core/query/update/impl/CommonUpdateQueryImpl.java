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
package com.jporm.commons.core.query.update.impl;

import com.jporm.commons.core.query.AQueryRoot;
import com.jporm.commons.core.query.cache.SqlCache;
import com.jporm.commons.core.query.update.CommonUpdateQuery;
import com.jporm.commons.core.query.update.CommonUpdateQuerySet;
import com.jporm.commons.core.query.update.CommonUpdateQueryWhere;
import com.jporm.sql.SqlFactory;
import com.jporm.sql.query.SqlRoot;
import com.jporm.sql.query.clause.Update;

/**
 *
 * @author Francesco Cina
 *
 * 10/lug/2011
 */
public class CommonUpdateQueryImpl<UPDATE extends CommonUpdateQuery<UPDATE, WHERE, SET>,
									WHERE extends CommonUpdateQueryWhere<UPDATE, WHERE, SET>,
									SET extends CommonUpdateQuerySet<UPDATE, WHERE, SET>>
								extends AQueryRoot implements CommonUpdateQuery<UPDATE, WHERE, SET> {

	private SET set;
	private WHERE where;
	private final Update update;

	public CommonUpdateQueryImpl(final Class<?> clazz, SqlCache sqlCache, SqlFactory sqlFactory) {
		super(sqlCache);
		update = sqlFactory.update(clazz);
	}

	@Override
	public final WHERE where() {
		return where;
	}

	@Override
	public final int getVersion() {
		return update.getVersion();
	}

	@Override
	public final SET set() {
		return set;
	}

	/**
	 * @param set the set to set
	 */
	public final void setSet(SET set) {
		this.set = set;
	}

	/**
	 * @param where the where to set
	 */
	public final void setWhere(WHERE where) {
		this.where = where;
	}

	/**
	 * @return the update
	 */
	public Update query() {
		return update;
	}

	@Override
	public SqlRoot sql() {
		return update;
	}
}
