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
package com.jporm.persistor.type.ext;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.jporm.persistor.type.TypeWrapper;

/**
 *
 * @author Francesco Cina'
 *
 * Mar 27, 2012
 */
public class ZoneDateTimeToSqlTimestampWrapper implements TypeWrapper<ZonedDateTime, Timestamp> {

	@Override
	public Class<Timestamp> jdbcType() {
		return Timestamp.class;
	}

	@Override
	public Class<ZonedDateTime> propertyType() {
		return ZonedDateTime.class;
	}

	@Override
	public ZonedDateTime wrap(final Timestamp value) {
		if (value==null) {
			return null;
		}
		return value.toLocalDateTime().atZone(ZoneId.systemDefault());
	}

	@Override
	public Timestamp unWrap(final ZonedDateTime value) {
		if (value==null) {
			return null;
		}
		return  Timestamp.valueOf(value.toLocalDateTime());
	}

	@Override
	public ZonedDateTime clone(final ZonedDateTime source) {
		return source;
	}

}
