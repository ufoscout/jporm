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
package com.jporm.persistor;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import com.jporm.persistor.accessor.Getter;
import com.jporm.persistor.accessor.Setter;
import com.jporm.persistor.version.VersionMath;
import com.jporm.types.TypeConverterJdbcReady;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;

public class PropertyPersistorImpl<BEAN, P, DB> implements PropertyPersistor<BEAN, P, DB> {

	private final TypeConverterJdbcReady<P, DB> typeWrapper;
	private final VersionMath<P> math;
	private final String fieldName;
	private final Getter<BEAN, P> getManipulator;
	private final Setter<BEAN, P> setManipulator;

	public PropertyPersistorImpl(final String fieldName, final Getter<BEAN, P> getManipulator, final Setter<BEAN, P> setManipulator,
			final TypeConverterJdbcReady<P, DB> typeWrapper, final VersionMath<P> math) {
		this.fieldName = fieldName;
		this.getManipulator = getManipulator;
		this.setManipulator = setManipulator;
		this.typeWrapper = typeWrapper;
		this.math = math;

	}

	/**
	 * Set copy the property value from source to destination
	 *
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@Override
	public BEAN clonePropertyValue(final BEAN source, final BEAN destination) throws IllegalArgumentException {
		return this.setPropertyValueToBean(destination, this.typeWrapper.clone(this.getPropertyValueFromBean(source)));
	}

	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Extract the value from a {@link ResultSet} and set it to a bean's
	 * property
	 *
	 * @param bean
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	@Override
	public BEAN getFromResultSet(final BEAN bean, final ResultEntry rs) throws IllegalArgumentException, SQLException {
		return this.setPropertyValueToBean(bean, getValueFromResultSet(rs, this.getFieldName()));
	}

	/**
	 * Extract the value from a {@link ResultSet} and set it to a bean's
	 * property
	 *
	 * @param bean
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	@Override
	public BEAN getFromResultSet(final BEAN bean, final ResultEntry rs, final int rsColumnIndex) throws IllegalArgumentException, SQLException {
		return this.setPropertyValueToBean(bean, this.typeWrapper.fromJdbcType(this.typeWrapper.getJdbcIO().getValueFromResultSet(rs, rsColumnIndex)));
	}

	public Getter<BEAN, P> getGetManipulator() {
		return getManipulator;
	}

	@Override
	public P getPropertyValueFromBean(final BEAN bean) throws IllegalArgumentException {
		return this.getGetManipulator().getValue(bean);
	}

	public Setter<BEAN, P> getSetManipulator() {
		return setManipulator;
	}

	@Override
	public P getValueFromResultSet(final ResultEntry rs, final String fieldName) throws IllegalArgumentException, SQLException {
		return this.typeWrapper.fromJdbcType(this.typeWrapper.getJdbcIO().getValueFromResultSet(rs, fieldName));
	}

	@Override
	public BEAN increaseVersion(final BEAN bean, final boolean firstVersionNumber) throws IllegalArgumentException {
		return this.setPropertyValueToBean(bean, this.math.increase(firstVersionNumber, this.getPropertyValueFromBean(bean)));
	}

	@Override
	public Class<P> propertyType() {
		return this.typeWrapper.propertyType();
	}

	@Override
	public BEAN setPropertyValueToBean(final BEAN bean, final P value) throws IllegalArgumentException {
		return this.getSetManipulator().setValue(bean, value);
	}

}
