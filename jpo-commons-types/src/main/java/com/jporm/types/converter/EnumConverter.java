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

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Nov 22, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
@SuppressWarnings("rawtypes")
public class EnumConverter implements TypeConverter<Enum, String> {

	private final Class<Enum> enumType;
	private final JdbcIO<String> jdbcIO = JdbcIOFactory.getString();

	public EnumConverter(final Class<Enum> enumType) {
		this.enumType = enumType;
	}

	@Override
	public Enum clone(final Enum source) {
		return source;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enum fromJdbcType(final String value) {
		if (value==null) {
			return null;
		}
		return Enum.valueOf(enumType, value);
	}

	@Override
	public JdbcIO<String> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<Enum> propertyType() {
		return Enum.class;
	}

	@Override
	public String toJdbcType(final Enum value) {
		if (value==null) {
			return null;
		}
		return value.name();
	}

}
