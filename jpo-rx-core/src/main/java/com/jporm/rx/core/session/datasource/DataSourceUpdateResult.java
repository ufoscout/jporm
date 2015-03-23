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
package com.jporm.rx.core.session.datasource;

import com.jporm.rx.core.connection.UpdateResult;
import com.jporm.types.ResultSet;

public class DataSourceUpdateResult implements UpdateResult {

	private final int updated;
	private final ResultSet generatedKeys;

	public DataSourceUpdateResult(int updated, ResultSet generatedKeys) {
		this.updated = updated;
		this.generatedKeys = generatedKeys;
	}

	@Override
	public int updated() {
		return updated;
	}

	@Override
	public ResultSet getGeneratedKeys() {
		return generatedKeys;
	}

}
