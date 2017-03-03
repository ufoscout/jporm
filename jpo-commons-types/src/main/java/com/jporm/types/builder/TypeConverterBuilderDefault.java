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
package com.jporm.types.builder;

import com.jporm.types.TypeConverter;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Nov 21, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class TypeConverterBuilderDefault<P, DB> implements TypeConverterBuilder<P, DB> {

	private final TypeConverter<P, DB> typeConverter;
	private final boolean strictAccept;

	public TypeConverterBuilderDefault(final TypeConverter<P, DB> typeConverter, boolean strictAccept) {
		this.typeConverter = typeConverter;
		this.strictAccept = strictAccept;
	}

	@Override
	public TypeConverter<P, DB> build(final Class<P> pClass) {
		return typeConverter;
	}

	@Override
	public Class<DB> jdbcType() {
		return typeConverter.jdbcType();
	}

	@Override
	public boolean acceptType(Class<?> type) {
		if (strictAccept) {
			return typeConverter.propertyType().equals(type);
		}
		return typeConverter.propertyType().isAssignableFrom(type);
	}

}
