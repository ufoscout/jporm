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

public class IntegerPrimitiveNullConverter implements TypeConverter<Integer, Integer> {

	private final JdbcIO<Integer> jdbcIO = JdbcIOFactory.getInteger();

	@Override
	public Integer clone(final Integer source) {
		return source;
	}

	@Override
	public Integer fromJdbcType(final Integer value) {
		return value;
	}

	@Override
	public JdbcIO<Integer> getJdbcIO() {
		return jdbcIO ;
	}

	@Override
	public Class<Integer> propertyType() {
		return Integer.TYPE;
	}

	@Override
	public Integer toJdbcType(final Integer value) {
		return value;
	}

}
