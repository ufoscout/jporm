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

import java.io.Reader;
import java.sql.SQLException;

import com.jporm.types.JdbcIO;
import com.jporm.types.ResultSet;
import com.jporm.types.Statement;

/**
 *
 * @author ufo
 *
 */
public class ReaderJdbcIO implements JdbcIO<Reader> {

	@Override
	public Reader getValueFromResultSet(final ResultSet rs, final String rsColumnName) throws SQLException {
		return rs.getCharacterStream(rsColumnName);
	}

	@Override
	public Reader getValueFromResultSet(final ResultSet rs, final int rsColumnIndex) throws SQLException {
		return rs.getCharacterStream(rsColumnIndex);
	}

	@Override
	public void setValueToPreparedStatement(final Reader value, final Statement ps,
			final int index) throws SQLException {
		if (value!=null) {
			ps.setCharacterStream(index, value);
		} else {
			ps.setObject(index, value);
		}
	}

	@Override
	public Class<Reader> getDBClass() {
		return Reader.class;
	}

}
