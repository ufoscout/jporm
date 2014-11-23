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
package com.jporm.core.persistor.wrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.sql.SQLXML;
import java.util.Date;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.JPOrm;
import com.jporm.core.persistor.type.TypeFactory;
import com.jporm.core.persistor.type.ext.UtilDateToSqlTimestampWrapper;
import com.jporm.core.session.NullSessionProvider;
import com.jporm.exception.OrmException;
import com.jporm.wrapper.TypeWrapper;

/**
 * 
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class JPOAddWrapperTest extends BaseTestApi {

    @Test
    public void tesRegisterTypeWrapper() {
        final JPOrm jpOrm = new JPOrm(new NullSessionProvider());
        TypeFactory typeFactory = jpOrm.getTypeFactory();
        assertNotNull(typeFactory);

        try {
            typeFactory.getTypeWrapper(Mock.class);
            fail("An OrmException should be thrown"); //$NON-NLS-1$
        } catch (OrmException e) {
            // do nothing
        }

        jpOrm.register(new MockTypeWrapper());

        assertEquals(MockTypeWrapper.class, typeFactory.getTypeWrapper(Mock.class).getTypeWrapper().getClass());
        assertEquals(new MockTypeWrapper().propertyType(), typeFactory.getTypeWrapper(Mock.class).propertyType());
    }

    @Test
    public void testOverrideTypeWrapper() {
        final JPOrm jpOrm = new JPOrm(new NullSessionProvider());
        TypeFactory typeFactory = jpOrm.getTypeFactory();
        assertNotNull(typeFactory);

        assertEquals(UtilDateToSqlTimestampWrapper.class, typeFactory.getTypeWrapper(java.util.Date.class).getTypeWrapper().getClass());

        jpOrm.register(new DateTypeWrapper());

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
