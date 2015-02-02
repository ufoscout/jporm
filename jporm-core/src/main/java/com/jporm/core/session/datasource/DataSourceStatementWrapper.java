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
package com.jporm.core.session.datasource;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ufo
 *
 */
public class DataSourceStatementWrapper implements DataSourceStatement {

	private final Statement statement;
	private final DataSourceConnection conn;

	DataSourceStatementWrapper(Statement statement, DataSourceConnection conn) {
		this.statement = statement;
		this.conn = conn;
	}

	@Override
	public void addBatch(String sql) {
		try {
			statement.addBatch(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int[] executeBatch() throws SQLException {
		conn.setReadOnly(false);
		return statement.executeBatch();
	}

	@Override
	public void close() throws SQLException {
		statement.close();
	}

}
