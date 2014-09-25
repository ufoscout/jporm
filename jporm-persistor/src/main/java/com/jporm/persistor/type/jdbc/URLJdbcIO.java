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
package com.jporm.persistor.type.jdbc;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jporm.persistor.type.JdbcIO;

/**
 * 
 * @author ufo
 *
 */
public class URLJdbcIO implements JdbcIO<URL> {

	@Override
	public URL getValueFromResultSet(final ResultSet rs, final String rsColumnName) throws SQLException {
		return rs.getURL(rsColumnName);
	}

	@Override
	public URL getValueFromResultSet(final ResultSet rs, final int rsColumnIndex) throws SQLException {
		return rs.getURL(rsColumnIndex);
	}

	@Override
	public void setValueToPreparedStatement(final URL value, final PreparedStatement ps,
			final int index) throws SQLException {
		ps.setURL(index, value);
	}

	@Override
	public Class<URL> getDBClass() {
		return URL.class;
	}

}
