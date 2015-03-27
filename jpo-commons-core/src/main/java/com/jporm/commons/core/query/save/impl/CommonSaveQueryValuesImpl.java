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

import com.jporm.commons.core.query.clause.impl.ValuesImpl;
import com.jporm.commons.core.query.save.CommonSaveQuery;
import com.jporm.commons.core.query.save.CommonSaveQueryValues;

/**
 *
 * @author ufo
 *
 */
public class CommonSaveQueryValuesImpl<SAVE extends CommonSaveQuery<SAVE, VALUES>,
										VALUES extends CommonSaveQueryValues<SAVE, VALUES>>
									extends ValuesImpl<VALUES> implements CommonSaveQueryValues<SAVE, VALUES> {

	private final SAVE query;

	public CommonSaveQueryValuesImpl(com.jporm.sql.query.clause.Values sqlValues, final SAVE query) {
		super(sqlValues);
		this.query = query;
	}

	@Override
	public final SAVE root() {
		return query;
	}

	@Override
	protected final VALUES values() {
		return query.values();
	}

}
