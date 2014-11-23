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

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.jporm.type.TypeWrapper;

/**
 *
 * @author Francesco Cina'
 *
 * Mar 27, 2012
 */
public class LocalDateTimeToSqlTimestampWrapper implements TypeWrapper<LocalDateTime, Timestamp> {

	@Override
	public Class<Timestamp> jdbcType() {
		return Timestamp.class;
	}

	@Override
	public Class<LocalDateTime> propertyType() {
		return LocalDateTime.class;
	}

	@Override
	public LocalDateTime wrap(final Timestamp value) {
		if (value==null) {
			return null;
		}
		return value.toLocalDateTime();
	}

	@Override
	public Timestamp unWrap(final LocalDateTime value) {
		if (value==null) {
			return null;
		}
		return  Timestamp.valueOf(value);
	}

	@Override
	public LocalDateTime clone(final LocalDateTime source) {
		return source;
	}

}
