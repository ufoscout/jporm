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
package com.jporm.core.query.delete.impl;

import com.jporm.commons.core.query.delete.impl.CommonDeleteQueryWhereImpl;
import com.jporm.core.query.delete.CustomDeleteQuery;
import com.jporm.core.query.delete.CustomDeleteQueryWhere;
import com.jporm.sql.query.clause.Where;

/**
 *
 * @author ufo
 *
 */
public class CustomDeleteQueryWhereImpl<BEAN> extends CommonDeleteQueryWhereImpl<CustomDeleteQuery<BEAN>, CustomDeleteQueryWhere<BEAN>> implements CustomDeleteQueryWhere<BEAN> {

	public CustomDeleteQueryWhereImpl(Where sqlWhere, final CustomDeleteQuery<BEAN> deleteQuery) {
		super(sqlWhere, deleteQuery);
	}

	@Override
	public void execute() {
		query().execute();
	}

	@Override
	public boolean isExecuted() {
		return query().isExecuted();
	}

	@Override
	public int now() {
		return query().now();
	}

}
