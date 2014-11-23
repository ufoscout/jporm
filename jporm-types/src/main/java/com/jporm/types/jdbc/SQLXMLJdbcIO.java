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

import java.sql.PreparedStatement;
import java.sql.SQLXML;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jporm.types.JdbcIO;

/**
 * 
 * @author ufo
 *
 */
public class SQLXMLJdbcIO implements JdbcIO<SQLXML> {

	@Override
	public SQLXML getValueFromResultSet(final ResultSet rs, final String rsColumnName) throws SQLException {
		return rs.getSQLXML(rsColumnName);
	}

	@Override
	public SQLXML getValueFromResultSet(final ResultSet rs, final int rsColumnIndex) throws SQLException {
		return rs.getSQLXML(rsColumnIndex);
	}

	@Override
	public void setValueToPreparedStatement(final SQLXML value, final PreparedStatement ps,
			final int index) throws SQLException {
		ps.setSQLXML(index, value);
	}

	@Override
	public Class<SQLXML> getDBClass() {
		return SQLXML.class;
	}
}
