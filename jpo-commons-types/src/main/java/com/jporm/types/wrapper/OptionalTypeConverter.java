/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.types.wrapper;

import java.util.Optional;

import com.jporm.types.TypeConverter;

public class OptionalTypeConverter<P, DB> implements TypeConverter<Optional<P>, DB> {

	private final Class<Optional<P>> propertyClass = (Class<Optional<P>>) Optional.empty().getClass();
	private final TypeConverter<P, DB> typeConverter;

	public OptionalTypeConverter(TypeConverter<P, DB> typeConverter) {
		this.typeConverter = typeConverter;
	}

	@Override
	public Class<DB> jdbcType() {
		return typeConverter.jdbcType();
	}

	@Override
	public Class<Optional<P>> propertyType() {
		return propertyClass;
	}

	@Override
	public Optional<P> fromJdbcType(DB value) {
		return Optional.ofNullable(typeConverter.fromJdbcType(value));
	}

	@Override
	public DB toJdbcType(Optional<P> value) {
		if ((value==null) || !value.isPresent()) {
			return null;
		}
		return typeConverter.toJdbcType(value.get());
	}

	@Override
	public Optional<P> clone(Optional<P> source) {
		if (source.isPresent()) {
			return Optional.of(typeConverter.clone(source.get()));
		}
		return Optional.empty();
	}


}
