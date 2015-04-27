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
package com.jporm.core.query.save.impl;

import com.jporm.commons.core.query.save.impl.CommonSaveQueryValuesImpl;
import com.jporm.core.query.save.CustomSaveQuery;
import com.jporm.core.query.save.CustomSaveQueryValues;

/**
 *
 * @author ufo
 *
 */
public class CustomSaveQueryValuesImpl<BEAN> extends CommonSaveQueryValuesImpl<CustomSaveQuery, CustomSaveQueryValues> implements CustomSaveQueryValues {


	public CustomSaveQueryValuesImpl(com.jporm.sql.query.clause.Values sqlValues, final CustomSaveQuery query) {
		super(sqlValues, query);
	}

	@Override
	public final int execute() {
		return root().execute();
	}

}
