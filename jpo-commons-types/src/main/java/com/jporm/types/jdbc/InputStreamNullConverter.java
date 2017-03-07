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

import java.io.InputStream;

import com.jporm.types.JdbcIO;
import com.jporm.types.TypeConverter;

public class InputStreamNullConverter implements TypeConverter<InputStream, InputStream> {

	private final JdbcIO<InputStream> jdbcIO = new InputStreamJdbcIO();

	@Override
	public InputStream clone(final InputStream source) {
		return source;
	}

	@Override
	public InputStream fromJdbcType(final InputStream value) {
		return value;
	}

	@Override
	public JdbcIO<InputStream> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<InputStream> propertyType() {
		return InputStream.class;
	}

	@Override
	public InputStream toJdbcType(final InputStream value) {
		return value;
	}

}
