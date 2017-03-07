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

import java.time.LocalDateTime;

import com.jporm.types.JdbcIO;
import com.jporm.types.TypeConverter;

public class LocalDateTimeNullConverter implements TypeConverter<LocalDateTime, LocalDateTime> {

	private final JdbcIO<LocalDateTime> jdbcIO = new LocalDateTimeJdbcIO();

	@Override
	public LocalDateTime clone(final LocalDateTime source) {
		return source;
	}

	@Override
	public LocalDateTime fromJdbcType(final LocalDateTime value) {
		return value;
	}

	@Override
	public JdbcIO<LocalDateTime> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<LocalDateTime> propertyType() {
		return LocalDateTime.class;
	}

	@Override
	public LocalDateTime toJdbcType(final LocalDateTime value) {
		return value;
	}

}
