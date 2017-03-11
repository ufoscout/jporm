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
package com.jporm.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.jporm.commons.json.JsonService;
import com.jporm.types.builder.TypeConverterBuilder;
import com.jporm.types.builder.TypeConverterBuilderDefault;
import com.jporm.types.builder.TypeConverterBuilderEnum;
import com.jporm.types.builder.TypeConverterBuilderJson;
import com.jporm.types.converter.BigDecimalNullConverter;
import com.jporm.types.converter.BooleanPrimitiveNullConverter;
import com.jporm.types.converter.BooleanToBigDecimalConverter;
import com.jporm.types.converter.BytePrimitiveNullConverter;
import com.jporm.types.converter.ByteToBigDecimalConverter;
import com.jporm.types.converter.BytesNullConverter;
import com.jporm.types.converter.CharacterToStringConverter;
import com.jporm.types.converter.DateNullConverter;
import com.jporm.types.converter.DoublePrimitiveNullConverter;
import com.jporm.types.converter.DoubleToBigDecimalConverter;
import com.jporm.types.converter.FloatPrimitiveNullConverter;
import com.jporm.types.converter.FloatToBigDecimalConverter;
import com.jporm.types.converter.InputStreamNullConverter;
import com.jporm.types.converter.InstantNullConverter;
import com.jporm.types.converter.IntegerPrimitiveNullConverter;
import com.jporm.types.converter.IntegerToBigDecimalConverter;
import com.jporm.types.converter.JsonConverter;
import com.jporm.types.converter.LocalDateNullConverter;
import com.jporm.types.converter.LocalDateTimeNullConverter;
import com.jporm.types.converter.LongPrimitiveNullConverter;
import com.jporm.types.converter.LongToBigDecimalConverter;
import com.jporm.types.converter.ObjectNullConverter;
import com.jporm.types.converter.OffsetDateTimeToLocalDateTimeTimestampConverter;
import com.jporm.types.converter.ReaderNullConverter;
import com.jporm.types.converter.ShortPrimitiveNullConverter;
import com.jporm.types.converter.ShortToBigDecimalConverter;
import com.jporm.types.converter.SqlDateNullConverter;
import com.jporm.types.converter.StringNullConverter;
import com.jporm.types.converter.TimestampNullConverter;
import com.jporm.types.converter.ZonedDateTimeToLocalDateTimeTimestampConverter;
import com.jporm.types.exception.JpoWrongTypeException;

/**
 *
 * @author ufo
 *
 */
public class TypeConverterFactory {

	private final List<TypeConverterBuilder<?, ?>> typeConverterBuilders = Collections.synchronizedList(new ArrayList<>());
	private final Map<Class<?>, TypeConverterBuilder<?, ?>> typeConverterBuilderCache = new ConcurrentHashMap<>();
	private final TypeConverterBuilderJson<?> jsonTypeConverterBuilder;

	public TypeConverterFactory(Supplier<JsonService> jsonService) {
		jsonTypeConverterBuilder = new TypeConverterBuilderJson<>(jsonService);
		registerJdbcType();
		registerExtendedType();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <TYPE> void addTypeConverter(final TypeConverter<TYPE, ?> typeConverter) {
		addTypeConverter(new TypeConverterBuilderDefault(typeConverter, false));
	}

	public <TYPE> void addTypeConverter(final TypeConverterBuilder<TYPE, ?> typeConverterBuilder) {
		typeConverterBuilderCache.clear();
		typeConverterBuilders.add(0, typeConverterBuilder);
	}

	@SuppressWarnings({ "unchecked" })
	public <P, DB> TypeConverter<P, DB> getTypeConverter(final Class<P> clazz) {
		TypeConverterBuilder<P, DB> cachedBuilder = (TypeConverterBuilder<P, DB>) typeConverterBuilderCache.get(clazz);
		if (cachedBuilder == null) {
			for ( final TypeConverterBuilder<?, ?> builder : typeConverterBuilders) {
				if (builder.acceptType(clazz)) {
					cachedBuilder = (TypeConverterBuilder<P, DB>) builder;
					typeConverterBuilderCache.put(clazz, builder);
					break;
				}
			}
			if (cachedBuilder == null) {
				throw new JpoWrongTypeException("Cannot manipulate properties of type [" + clazz + "].");
			}
		}
		return cachedBuilder.build(clazz);
	}

	/**
	 * Special case to get a {@link JsonConverter}. It should be maybe refactored to be more generic.
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <P, DB> TypeConverter<P, DB> getJsonTypeConverter(final Class<P> clazz, final boolean deepCopy) {
		final JsonConverter typeConverter = jsonTypeConverterBuilder.build((Class) clazz);
		typeConverter.setDeepCopy(deepCopy);
		return typeConverter;
	}


	private void registerExtendedType() {
		addTypeConverter(new BooleanToBigDecimalConverter());
		addTypeConverter(new ByteToBigDecimalConverter());
		addTypeConverter(new CharacterToStringConverter());
		addTypeConverter(new DoubleToBigDecimalConverter());
		addTypeConverter(new FloatToBigDecimalConverter());
		addTypeConverter(new IntegerToBigDecimalConverter());
		addTypeConverter(new LongToBigDecimalConverter());
		addTypeConverter(new ShortToBigDecimalConverter());
		addTypeConverter(new TypeConverterBuilderEnum());
		addTypeConverter(new ZonedDateTimeToLocalDateTimeTimestampConverter());
		addTypeConverter(new OffsetDateTimeToLocalDateTimeTimestampConverter());
	}

	private void registerJdbcType() {
		addTypeConverter(new BigDecimalNullConverter());
		addTypeConverter(new BooleanPrimitiveNullConverter());
		addTypeConverter(new BytesNullConverter());
		addTypeConverter(new BytePrimitiveNullConverter());
		addTypeConverter(new DateNullConverter());
		addTypeConverter(new DoublePrimitiveNullConverter());
		addTypeConverter(new FloatPrimitiveNullConverter());
		addTypeConverter(new InputStreamNullConverter());
		addTypeConverter(new IntegerPrimitiveNullConverter());
		addTypeConverter(new InstantNullConverter());
		addTypeConverter(new LongPrimitiveNullConverter());
		addTypeConverter(new TypeConverterBuilderDefault<>(new ObjectNullConverter(), true));
		addTypeConverter(new ReaderNullConverter());
		addTypeConverter(new ShortPrimitiveNullConverter());
		addTypeConverter(new StringNullConverter());
		addTypeConverter(new LocalDateNullConverter());
		addTypeConverter(new LocalDateTimeNullConverter());
		addTypeConverter(new SqlDateNullConverter());
		addTypeConverter(new TimestampNullConverter());
	}
}
