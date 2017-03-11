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

import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.Statement;

/**
 *
 * @author ufo
 *
 */
class ObjectJdbcIO implements JdbcIO<Object> {

	@Override
	public Class<Object> getDBClass() {
		return Object.class;
	}

	@Override
	public Object getValueFromResultSet(final ResultEntry rs, final int rsColumnIndex) {
		return rs.getObject(rsColumnIndex);
	}

	@Override
	public Object getValueFromResultSet(final ResultEntry rs, final String rsColumnName) {
		return rs.getObject(rsColumnName);
	}

	@Override
	public void setValueToPreparedStatement(final Object value, final Statement ps, final int index) {
		ps.setObject(index, value);
	}

}
