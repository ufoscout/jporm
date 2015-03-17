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

import java.math.BigDecimal;
import java.sql.SQLException;

import com.jporm.types.JdbcIO;
import com.jporm.types.JpoResultSet;
import com.jporm.types.JpoStatement;

/**
 *
 * @author ufo
 *
 */
public class BigDecimalJdbcIO implements JdbcIO<BigDecimal> {

	@Override
	public BigDecimal getValueFromResultSet(final JpoResultSet rs, final String rsColumnName) throws SQLException {
		return rs.getBigDecimal(rsColumnName);
	}

	@Override
	public BigDecimal getValueFromResultSet(final JpoResultSet rs, final int rsColumnIndex) throws SQLException {
		return rs.getBigDecimal(rsColumnIndex);
	}

	@Override
	public void setValueToPreparedStatement(final BigDecimal value, final JpoStatement ps,
			final int index) throws SQLException {
		ps.setBigDecimal(index, value);
	}

	@Override
	public Class<BigDecimal> getDBClass() {
		return BigDecimal.class;
	}

}