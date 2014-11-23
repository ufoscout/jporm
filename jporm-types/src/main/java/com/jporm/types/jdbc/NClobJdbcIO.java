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
package com.jporm.core.persistor.type.jdbc;

import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jporm.core.persistor.type.JdbcIO;

/**
 * 
 * @author ufo
 *
 */
public class NClobJdbcIO implements JdbcIO<NClob> {

	@Override
	public NClob getValueFromResultSet(final ResultSet rs, final String rsColumnName) throws SQLException {
		return rs.getNClob(rsColumnName);
	}

	@Override
	public NClob getValueFromResultSet(final ResultSet rs, final int rsColumnIndex) throws SQLException {
		return rs.getNClob(rsColumnIndex);
	}

	@Override
	public void setValueToPreparedStatement(final NClob value, final PreparedStatement ps,
			final int index) throws SQLException {
		if (value!=null) {
			ps.setNClob(index, value);
		} else {
			ps.setObject(index, value);
		}
	}

	@Override
	public Class<NClob> getDBClass() {
		return NClob.class;
	}

}
