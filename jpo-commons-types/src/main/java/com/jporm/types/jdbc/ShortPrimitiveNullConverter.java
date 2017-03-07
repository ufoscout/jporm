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

import com.jporm.types.JdbcIO;
import com.jporm.types.TypeConverter;

public class ShortPrimitiveNullConverter implements TypeConverter<Short, Short> {

	private final JdbcIO<Short> jdbcIO = new ShortPrimitiveJdbcIO();

	@Override
	public Short clone(final Short source) {
		return source;
	}

	@Override
	public Short fromJdbcType(final Short value) {
		return value;
	}

	@Override
	public JdbcIO<Short> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<Short> propertyType() {
		return Short.TYPE;
	}

	@Override
	public Short toJdbcType(final Short value) {
		return value;
	}

}
