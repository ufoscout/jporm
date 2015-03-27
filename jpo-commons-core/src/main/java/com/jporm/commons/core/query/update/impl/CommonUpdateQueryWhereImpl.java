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

import com.jporm.commons.core.query.clause.impl.WhereImpl;
import com.jporm.commons.core.query.update.CommonUpdateQuery;
import com.jporm.commons.core.query.update.CommonUpdateQuerySet;
import com.jporm.commons.core.query.update.CommonUpdateQueryWhere;

/**
 *
 * @author ufo
 *
 */
public class CommonUpdateQueryWhereImpl <UPDATE extends CommonUpdateQuery<UPDATE, WHERE, SET>,
										WHERE extends CommonUpdateQueryWhere<UPDATE, WHERE, SET>,
										SET extends CommonUpdateQuerySet<UPDATE, WHERE, SET>>
								extends WhereImpl<WHERE> implements CommonUpdateQueryWhere<UPDATE, WHERE, SET> {

	private final UPDATE updateQuery;

	public CommonUpdateQueryWhereImpl(com.jporm.sql.query.clause.Where sqlWhere, final UPDATE updateQuery) {
		super(sqlWhere);
		this.updateQuery = updateQuery;

	}

	@Override
	public final UPDATE root() {
		return updateQuery;
	}

	@Override
	public final SET set() {
		return updateQuery.set();
	}

	@Override
	protected final WHERE where() {
		return updateQuery.where();
	}

}
