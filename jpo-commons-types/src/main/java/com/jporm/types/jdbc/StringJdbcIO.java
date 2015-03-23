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
package com.jporm.types.jdbc;

import com.jporm.types.JdbcIO;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

/**
 *
 * @author ufo
 *
 */
public class StringJdbcIO implements JdbcIO<String> {

	@Override
	public String getValueFromResultSet(final ResultSet rs, final String rsColumnName) {
		return rs.getString(rsColumnName);
	}

	@Override
	public String getValueFromResultSet(final ResultSet rs, final int rsColumnIndex) {
		return rs.getString(rsColumnIndex);
	}

	@Override
	public void setValueToPreparedStatement(final String value, final Statement ps,
			final int index) {
		ps.setString(index, value);
	}

	@Override
	public Class<String> getDBClass() {
		return String.class;
	}

}
