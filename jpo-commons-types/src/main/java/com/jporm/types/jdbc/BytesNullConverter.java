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

import com.jporm.types.TypeConverter;


public class BytesNullConverter implements TypeConverter<byte[], byte[]> {

	@Override
	public Class<byte[]> jdbcType() {
		return byte[].class;
	}

	@Override
	public Class<byte[]> propertyType() {
		return byte[].class;
	}

	@Override
	public byte[] fromJdbcType(final byte[] value) {
		return value;
	}

	@Override
	public byte[] toJdbcType(final byte[] value) {
		return value;
	}

	@Override
	public byte[] clone(final byte[] source) {
		if(source!=null) {
			return source.clone();
		}
		return null;
	}

}
