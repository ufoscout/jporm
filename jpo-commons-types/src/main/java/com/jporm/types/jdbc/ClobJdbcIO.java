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

import java.sql.Clob;

import com.jporm.types.JdbcIO;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.Statement;

/**
 *
 * @author ufo
 *
 */
public class ClobJdbcIO implements JdbcIO<Clob> {

	@Override
	public Clob getValueFromResultSet(final ResultEntry rs, final String rsColumnName) {
		return rs.getClob(rsColumnName);
	}

	@Override
	public Clob getValueFromResultSet(final ResultEntry rs, final int rsColumnIndex) {
		return rs.getClob(rsColumnIndex);
	}

	@Override
	public void setValueToPreparedStatement(final Clob value, final Statement ps,
			final int index) {
		if (value!=null) {
			ps.setClob(index, value);
		} else {
			ps.setObject(index, value);
		}
	}

	@Override
	public Class<Clob> getDBClass() {
		return Clob.class;
	}

}
