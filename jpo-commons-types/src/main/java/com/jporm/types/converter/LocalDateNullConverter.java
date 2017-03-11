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

import java.time.LocalDate;

import com.jporm.types.TypeConverter;
import com.jporm.types.jdbc.JdbcIO;
import com.jporm.types.jdbc.JdbcIOFactory;

public class LocalDateNullConverter implements TypeConverter<LocalDate, LocalDate> {

	private final JdbcIO<LocalDate> jdbcIO = JdbcIOFactory.getLocalDate();

	@Override
	public LocalDate clone(final LocalDate source) {
		return source;
	}

	@Override
	public LocalDate fromJdbcType(final LocalDate value) {
		return value;
	}

	@Override
	public JdbcIO<LocalDate> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<LocalDate> propertyType() {
		return LocalDate.class;
	}

	@Override
	public LocalDate toJdbcType(final LocalDate value) {
		return value;
	}

}
