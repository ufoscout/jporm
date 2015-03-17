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
package com.jporm.core.query.update.impl;

import com.jporm.commons.core.query.update.impl.CommonUpdateQuerySetImpl;
import com.jporm.core.query.update.CustomUpdateQuery;
import com.jporm.core.query.update.CustomUpdateQuerySet;
import com.jporm.core.query.update.CustomUpdateQueryWhere;

/**
 *
 * @author ufo
 *
 */
public class CustomUpdateQuerySetImpl extends CommonUpdateQuerySetImpl<CustomUpdateQuery, CustomUpdateQueryWhere, CustomUpdateQuerySet>  implements CustomUpdateQuerySet {


	public CustomUpdateQuerySetImpl(com.jporm.sql.query.clause.Set sqlSet, final CustomUpdateQuery query) {
		super(sqlSet, query);
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