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
import com.jporm.types.JpoResultSet;
import com.jporm.types.TypeConverterJdbcReady;

public class PropertyPersistorImpl<BEAN, P, DB> implements PropertyPersistor<BEAN, P, DB> {

	private final TypeConverterJdbcReady<P, DB> typeWrapper;
	private final VersionMath<P> math;
	private final String fieldName;
	private final Getter<BEAN, P> getManipulator;
	private final Setter<BEAN, P> setManipulator;

	public PropertyPersistorImpl (final String fieldName, final Getter<BEAN, P> getManipulator, final Setter<BEAN, P> setManipulator, final TypeConverterJdbcReady<P, DB> typeWrapper,
			final VersionMath<P> math) {
		this.fieldName = fieldName;
		this.getManipulator = getManipulator;
		this.setManipulator = setManipulator;
		this.typeWrapper = typeWrapper;
		this.math = math;

	}

	/**
	 * Extract the value from a {@link ResultSet} and set it to a bean's property
	 * @param bean
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	@Override
	public void getFromResultSet(final BEAN bean, final JpoResultSet rs) throws IllegalArgumentException, SQLException {
		this.setPropertyValueToBean( bean, getValueFromResultSet(rs, this.getFieldName()) );
	}

	/**
	 * Extract the value from a {@link ResultSet} and set it to a bean's property
	 * @param bean
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	@Override
	public void getFromResultSet(final BEAN bean, final JpoResultSet rs, final int rsColumnIndex) throws IllegalArgumentException, SQLException {
		this.setPropertyValueToBean( bean, this.typeWrapper.fromJdbcType(this.typeWrapper.getJdbcIO().getValueFromResultSet(rs, rsColumnIndex) ) );
	}

	@Override
	public P getValueFromResultSet(final JpoResultSet rs, final String fieldName)
			throws IllegalArgumentException, SQLException {
		return this.typeWrapper.fromJdbcType(this.typeWrapper.getJdbcIO().getValueFromResultSet(rs, fieldName ));
	}

	/**
	 * Set copy the property value from source to destination
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@Override
	public void clonePropertyValue(final BEAN source, final BEAN destination) throws IllegalArgumentException {
		this.setPropertyValueToBean(destination, this.typeWrapper.clone(this.getPropertyValueFromBean(source)));
	}

	@Override
	public void increaseVersion(final BEAN bean, final boolean firstVersionNumber) throws IllegalArgumentException {
		this.setPropertyValueToBean(bean, this.math.increase(firstVersionNumber, this.getPropertyValueFromBean(bean)));
	}

	@Override
	public P getPropertyValueFromBean(final BEAN bean) throws IllegalArgumentException {
		return this.getGetManipulator().getValue(bean);
	}

	private void setPropertyValueToBean(final BEAN bean, final P value) throws IllegalArgumentException {
		this.getSetManipulator().setValue(bean, value);
	}

	@Override
	public Class<P> propertyType(){
		return this.typeWrapper.propertyType();
	}

	public String getFieldName() {
		return fieldName;
	}

	public Getter<BEAN, P> getGetManipulator() {
		return getManipulator;
	}

	public Setter<BEAN, P> getSetManipulator() {
		return setManipulator;
	}

}