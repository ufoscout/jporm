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

import com.jporm.types.TypeConverter;
import com.jporm.types.jdbc.JdbcIO;
import com.jporm.types.jdbc.JdbcIOFactory;

public class BooleanPrimitiveNullConverter implements TypeConverter<Boolean, Boolean> {

	private final JdbcIO<Boolean> jdbcIO = JdbcIOFactory.getBoolean();

	@Override
	public Boolean clone(final Boolean source) {
		return source;
	}

	@Override
	public Boolean fromJdbcType(final Boolean value) {
		return value;
	}

	@Override
	public JdbcIO<Boolean> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<Boolean> propertyType() {
		return Boolean.TYPE;
	}

	@Override
	public Boolean toJdbcType(final Boolean value) {
		return value;
	}

}
