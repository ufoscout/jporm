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
package com.jporm.types.ext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.jporm.types.JdbcIO;
import com.jporm.types.TypeConverter;
import com.jporm.types.jdbc.LocalDateTimeJdbcIO;

/**
 *
 * @author Francesco Cina'
 *
 *         Mar 27, 2012
 */
public class ZonedDateTimeToLocalDateTimeTimestampConverter implements TypeConverter<ZonedDateTime, LocalDateTime> {

	private final JdbcIO<LocalDateTime> jdbcIO =  new LocalDateTimeJdbcIO();

	@Override
	public ZonedDateTime clone(final ZonedDateTime source) {
		return source;
	}

	@Override
	public ZonedDateTime fromJdbcType(final LocalDateTime value) {
		if (value == null) {
			return null;
		}
		return value.atZone(ZoneId.systemDefault());
	}

	@Override
	public JdbcIO<LocalDateTime> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<ZonedDateTime> propertyType() {
		return ZonedDateTime.class;
	}

	@Override
	public LocalDateTime toJdbcType(final ZonedDateTime value) {
		if (value == null) {
			return null;
		}
		return value.toLocalDateTime();
	}

}
