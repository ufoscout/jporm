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

import java.math.BigDecimal;

import com.jporm.types.TypeConverter;
import com.jporm.types.jdbc.JdbcIO;
import com.jporm.types.jdbc.JdbcIOFactory;

/**
 *
 * @author Francesco Cina'
 *
 *         Apr 1, 2012
 */
public class ByteToBigDecimalConverter implements TypeConverter<Byte, BigDecimal> {

	private final JdbcIO<BigDecimal> jdbcIO = JdbcIOFactory.getBigDecimal();

	@Override
	public Byte clone(final Byte source) {
		return source;
	}

	@Override
	public Byte fromJdbcType(final BigDecimal value) {
		if (value == null) {
			return null;
		}
		return value.byteValue();
	}

	@Override
	public JdbcIO<BigDecimal> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<Byte> propertyType() {
		return Byte.class;
	}

	@Override
	public BigDecimal toJdbcType(final Byte value) {
		if (value == null) {
			return null;
		}
		return BigDecimal.valueOf(value);
	}

}
