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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This interface is used as a marker to identify Extended {@link TypeConverter}
 * s. A converter for a specific type. This can be used to create custom
 * converter from a desired type to a specific {@link PreparedStatement} setter
 * type and from a {@link ResultSet} getter type to the desired type.
 *
 * P is the class of the desired property. DB is the class type of the related
 * field in the {@link ResultSet} and {@link PreparedStatement} that will be
 * used to perform read/write operations. Valid Class type for R are:
 *
 * byte[].class Object.class String.class java.io.InputStream.class
 * java.io.Reader.class java.math.BigDecimal.class java.sql.Array.class
 * java.sql.Blob.class java.sql.Clob.class java.sql.Date.class
 * java.sql.NClob.class java.sql.Ref.class java.sql.RowId.class
 * java.sql.SQLXML.class java.sql.Time.class java.sql.Timestamp.class
 * java.net.URL.class
 *
 * @author Francesco Cina'
 * @author ufo
 *
 * @param
 *            <P>
 * @param <DB>
 */
public interface TypeConverter<P, DB> {

    /**
     * Return a new instance (the same instance can be returned if the object is immutable) equivalent to the source
     * 
     * @param source
     * @return
     */
    P clone(P source);

    P fromJdbcType(DB value);

    Class<DB> jdbcType();

    Class<P> propertyType();

    DB toJdbcType(P value);

}
