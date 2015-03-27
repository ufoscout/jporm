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
package com.jporm.commons.core.query.delete.impl;

import com.jporm.commons.core.query.clause.impl.WhereImpl;
import com.jporm.commons.core.query.delete.CommonDeleteQuery;
import com.jporm.commons.core.query.delete.CommonDeleteQueryWhere;
import com.jporm.sql.query.clause.Where;

/**
 *
 * @author ufo
 *
 */
public class CommonDeleteQueryWhereImpl<DELETE extends CommonDeleteQuery<DELETE, WHERE>,
										WHERE extends CommonDeleteQueryWhere<DELETE, WHERE>>
							extends WhereImpl<WHERE> implements CommonDeleteQueryWhere<DELETE,WHERE> {

	private final DELETE deleteQuery;

	public CommonDeleteQueryWhereImpl(Where sqlWhere, final DELETE deleteQuery) {
		super(sqlWhere);
		this.deleteQuery = deleteQuery;
	}

	@Override
	public final DELETE root() {
		return deleteQuery;
	}

	@Override
	protected final WHERE where() {
		return deleteQuery.where();
	}

}
