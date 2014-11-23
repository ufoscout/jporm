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
import java.time.Instant;

import com.jporm.type.TypeWrapper;

/**
 *
 * @author Francesco Cina'
 *
 * Mar 27, 2012
 */
public class InstantToTimestampWrapper implements TypeWrapper<Instant, Timestamp> {

	@Override
	public Class<Timestamp> jdbcType() {
		return Timestamp.class;
	}

	@Override
	public Class<Instant> propertyType() {
		return Instant.class;
	}

	@Override
	public Instant wrap(final Timestamp value) {
		if (value==null) {
			return null;
		}
		return value.toInstant();
	}

	@Override
	public Timestamp unWrap(final Instant value) {
		if (value==null) {
			return null;
		}
		return Timestamp.from(value);
	}

	@Override
	public Instant clone(final Instant source) {
		return source;
	}

}
