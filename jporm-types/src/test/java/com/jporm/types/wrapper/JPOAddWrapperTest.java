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
package com.jporm.types.wrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.sql.SQLXML;
import java.util.Date;

import org.junit.Test;

import com.jporm.types.BaseTestApi;
import com.jporm.types.TypeFactory;
import com.jporm.types.TypeWrapper;
import com.jporm.types.exception.JpoWrongTypeException;
import com.jporm.types.ext.UtilDateToSqlTimestampWrapper;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class JPOAddWrapperTest extends BaseTestApi {

	@Test
	public void tesRegisterTypeWrapper() {
		TypeFactory typeFactory = new TypeFactory();
		assertNotNull(typeFactory);

		try {
			typeFactory.getTypeWrapper(Mock.class);
			fail("An OrmException should be thrown"); //$NON-NLS-1$
		} catch (JpoWrongTypeException e) {
			// do nothing
		}

		typeFactory.addTypeWrapper(new MockTypeWrapper());

		assertEquals(MockTypeWrapper.class, typeFactory.getTypeWrapper(Mock.class).getTypeWrapper().getClass());
		assertEquals(new MockTypeWrapper().propertyType(), typeFactory.getTypeWrapper(Mock.class).propertyType());
	}

	@Test
	public void testOverrideTypeWrapper() {
		TypeFactory typeFactory = new TypeFactory();
		assertNotNull(typeFactory);

		assertEquals(UtilDateToSqlTimestampWrapper.class, typeFactory.getTypeWrapper(java.util.Date.class).getTypeWrapper().getClass());

		typeFactory.addTypeWrapper(new DateTypeWrapper());

		assertEquals(DateTypeWrapper.class, typeFactory.getTypeWrapper(java.util.Date.class).getTypeWrapper().getClass());
		assertEquals(new DateTypeWrapper().jdbcType(), typeFactory.getTypeWrapper(java.util.Date.class).getJdbcIO().getDBClass());

	}

	class Mock {// do nothing

	}

	class MockTypeWrapper implements TypeWrapper<Mock, InputStream> {
		@Override
		public Class<InputStream> jdbcType() {
			return InputStream.class;
		}
		@Override
		public Class<Mock> propertyType() {
			return Mock.class;
		}
		@Override
		public Mock wrap(final InputStream value) {
			return null;
		}
		@Override
		public InputStream unWrap(final Mock value) {
			return null;
		}
		@Override
		public Mock clone(final Mock source) {
			return source;
		}
	}

	class DateTypeWrapper implements TypeWrapper<Date, SQLXML> {
		@Override
		public Class<SQLXML> jdbcType() {
			return SQLXML.class;
		}
		@Override
		public Class<Date> propertyType() {
			return Date.class;
		}
		@Override
		public Date wrap(final SQLXML value) {
			return null;
		}
		@Override
		public SQLXML unWrap(final Date value) {
			return null;
		}
		@Override
		public Date clone(final Date source) {
			return source;
		}
	}
}
