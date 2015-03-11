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

import com.jporm.types.TypeConverter;

/**
 *
 * @author Francesco Cina'
 *
 * Apr 1, 2012
 */
public class CharacterToStringConverter implements TypeConverter<Character, String> {

	@Override
	public Class<String> jdbcType() {
		return String.class;
	}

	@Override
	public Class<Character> propertyType() {
		return Character.class;
	}

	@Override
	public Character fromJdbcType(final String value) {
		if ((value==null) || value.isEmpty()) {
			return null;
		}
		return value.charAt(0);
	}

	@Override
	public String toJdbcType(final Character value) {
		if (value==null) {
			return null;
		}
		return String.valueOf(value);
	}

	@Override
	public Character clone(final Character source) {
		return source;
	}

}
