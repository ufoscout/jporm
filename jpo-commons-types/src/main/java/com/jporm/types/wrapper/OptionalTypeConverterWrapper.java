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
import com.jporm.types.TypeConverterWrapper;

public class OptionalTypeConverterWrapper<P, DB> implements TypeConverterWrapper<Optional<P>, DB, P> {

	private final TypeConverter<P, DB> typeConverter;

	public OptionalTypeConverterWrapper(final TypeConverter<P, DB> typeConverter) {
		this.typeConverter = typeConverter;
	}

	@Override
	public Optional<P> clone(final Optional<P> source) {
		if (source == null) {
			return null;
		}
		if (source.isPresent()) {
			return Optional.of(typeConverter.clone(source.get()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<P> fromJdbcType(final DB value) {
		return Optional.ofNullable(typeConverter.fromJdbcType(value));
	}

	@Override
	public Class<DB> jdbcType() {
		return typeConverter.jdbcType();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<Optional<P>> propertyType() {
		return (Class) Optional.class;
	}

	@Override
	public DB toJdbcType(final Optional<P> value) {
		if (value == null || !value.isPresent()) {
			return typeConverter.toJdbcType(null);
		}
		return typeConverter.toJdbcType(value.get());
	}

	@Override
	public Class<P> wrappedType() {
		return typeConverter.propertyType();
	}

}
