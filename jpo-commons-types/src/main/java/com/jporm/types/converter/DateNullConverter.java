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
package com.jporm.types.converter;

import java.util.Date;

import com.jporm.types.TypeConverter;
import com.jporm.types.jdbc.JdbcIO;
import com.jporm.types.jdbc.JdbcIOFactory;

public class DateNullConverter implements TypeConverter<Date, Date> {

	private final JdbcIO<Date> jdbcIO = JdbcIOFactory.getDate();

	@Override
	public Date clone(final Date source) {
		return source;
	}

	@Override
	public Date fromJdbcType(final Date value) {
		return value;
	}

	@Override
	public JdbcIO<Date> getJdbcIO() {
		return jdbcIO ;
	}

	@Override
	public Class<Date> propertyType() {
		return Date.class;
	}

	@Override
	public Date toJdbcType(final Date value) {
		return value;
	}

}
