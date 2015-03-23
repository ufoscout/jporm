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
package com.jporm.core.session.jdbctemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.jdbc.JdbcResultSet;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 */
public class ResultSetReaderWrapper<T> implements ResultSetExtractor<T> {

	private final ResultSetReader<T> rse;

	public ResultSetReaderWrapper(final ResultSetReader<T> rse) {
		this.rse = rse;
	}

	@Override
	public T extractData(final ResultSet rs) throws SQLException, DataAccessException {
		return rse.read(new JdbcResultSet(rs));
	}

}
