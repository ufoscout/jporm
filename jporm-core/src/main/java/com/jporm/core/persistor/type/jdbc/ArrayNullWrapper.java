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
package com.jporm.core.persistor.type.jdbc;

import java.sql.Array;

import com.jporm.persistor.type.TypeWrapper;


public class ArrayNullWrapper implements TypeWrapper<Array, Array> {

	@Override
	public Class<Array> jdbcType() {
		return Array.class;
	}

	@Override
	public Class<Array> propertyType() {
		return Array.class;
	}

	@Override
	public Array wrap(final Array value) {
		return value;
	}

	@Override
	public Array unWrap(final Array value) {
		return value;
	}

	@Override
	public Array clone(final Array source) {
		return source;
	}

}
