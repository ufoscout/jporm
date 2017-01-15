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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.Test;

import com.jporm.types.BaseTestApi;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.exception.JpoWrongTypeException;
import com.jporm.types.jdbc.DateNullConverter;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class JPOAddConverterTest extends BaseTestApi {

	class DateTypeConverter implements TypeConverter<Date, Reader> {
		@Override
		public Date clone(final Date source) {
			return source;
		}

		@Override
		public Date fromJdbcType(final Reader value) {
			return null;
		}

		@Override
		public Class<Reader> jdbcType() {
			return Reader.class;
		}

		@Override
		public Class<Date> propertyType() {
			return Date.class;
		}

		@Override
		public Reader toJdbcType(final Date value) {
			return null;
		}
	}

	class Mock {// do nothing

	}

	class MockTypeConverter implements TypeConverter<Mock, InputStream> {
		@Override
		public Mock clone(final Mock source) {
			return source;
		}

		@Override
		public Mock fromJdbcType(final InputStream value) {
			return null;
		}

		@Override
		public Class<InputStream> jdbcType() {
			return InputStream.class;
		}

		@Override
		public Class<Mock> propertyType() {
			return Mock.class;
		}

		@Override
		public InputStream toJdbcType(final Mock value) {
			return null;
		}
	}

	@Test
	public void tesRegisterTypeConverter() {
		final TypeConverterFactory typeFactory = new TypeConverterFactory();
		assertNotNull(typeFactory);

		try {
			typeFactory.getTypeConverterFromClass(Mock.class);
			fail("An OrmException should be thrown"); //$NON-NLS-1$
		} catch (final JpoWrongTypeException e) {
			// do nothing
		}

		typeFactory.addTypeConverter(new MockTypeConverter());

		assertEquals(MockTypeConverter.class, typeFactory.getTypeConverterFromClass(Mock.class).getTypeConverter().getClass());
		assertEquals(new MockTypeConverter().propertyType(), typeFactory.getTypeConverterFromClass(Mock.class).propertyType());
	}

	@Test
	public void testOverrideTypeConverter() {
		final TypeConverterFactory typeFactory = new TypeConverterFactory();
		assertNotNull(typeFactory);

		assertEquals(DateNullConverter.class, typeFactory.getTypeConverterFromClass(java.util.Date.class).getTypeConverter().getClass());
		assertEquals(ZonedDateTimeToLocalDateTimeTimestampConverter.class, typeFactory.getTypeConverterFromClass(ZonedDateTime.class).getTypeConverter().getClass());

		typeFactory.addTypeConverter(new DateTypeConverter());

		assertEquals(DateTypeConverter.class, typeFactory.getTypeConverterFromClass(java.util.Date.class).getTypeConverter().getClass());
		assertEquals(new DateTypeConverter().jdbcType(), typeFactory.getTypeConverterFromClass(java.util.Date.class).getJdbcIO().getDBClass());

	}
}
