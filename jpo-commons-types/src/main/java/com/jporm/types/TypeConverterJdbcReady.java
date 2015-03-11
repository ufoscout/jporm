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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Mar 2, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.types;



/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 2, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class TypeConverterJdbcReady<P, DB> implements TypeConverter<P, DB> {

	private final TypeConverter<P, DB> typeConverter;
	private final JdbcIO<DB> jdbcIO;

	TypeConverterJdbcReady(final TypeConverter<P, DB> typeConverter, final JdbcIO<DB> jdbcIO) {
		this.typeConverter = typeConverter;
		this.jdbcIO = jdbcIO;
	}

	/**
	 * @return
	 * @see com.jporm.types.TypeConverter#jdbcType()
	 */
	@Override
	public Class<DB> jdbcType() {
		return getTypeConverter().jdbcType();
	}

	/**
	 * @return
	 * @see com.jporm.types.TypeConverter#propertyType()
	 */
	@Override
	public Class<P> propertyType() {
		return getTypeConverter().propertyType();
	}

	/**
	 * @param value
	 * @return
	 * @see com.jporm.types.TypeConverter#fromJdbcType(java.lang.Object)
	 */
	@Override
	public P fromJdbcType(final DB value) {
		return getTypeConverter().fromJdbcType(value);
	}

	/**
	 * @param value
	 * @return
	 * @see com.jporm.types.TypeConverter#toJdbcType(java.lang.Object)
	 */
	@Override
	public DB toJdbcType(final P value) {
		return getTypeConverter().toJdbcType(value);
	}

	/**
	 * @param source
	 * @return
	 * @see com.jporm.types.TypeConverter#clone(java.lang.Object)
	 */
	@Override
	public P clone(final P source) {
		return getTypeConverter().clone(source);
	}

	public JdbcIO<DB> getJdbcIO() {
		return jdbcIO;
	}

	public TypeConverter<P, DB> getTypeConverter() {
		return typeConverter;
	}

}
