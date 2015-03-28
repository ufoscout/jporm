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
 *          ON : Mar 3, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.persistor;

import java.sql.SQLException;

import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.Statement;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 3, 2013
 *
 * @param <BEAN> the type of the bean to manipulate
 * @param <P> the type of the bean's property to manipulate
 * @param <DB> the type of the field in the {@link Statement} and {@link ResultEntry}
 */
public interface PropertyPersistor<BEAN, P, DB> {

	/**
	 * @return
	 */
	Class<P> propertyType();

	/**
	 * @param bean
	 * @return
	 * @throws IllegalArgumentException
	 */
	P getPropertyValueFromBean(BEAN bean) throws IllegalArgumentException;

	/**
	 * @param bean
	 * @param firstVersionNumber
	 * @throws IllegalArgumentException
	 */
	void increaseVersion(BEAN bean, boolean firstVersionNumber)
			throws IllegalArgumentException;

	/**
	 * @param source
	 * @param destination
	 * @throws IllegalArgumentException
	 */
	void clonePropertyValue(BEAN source, BEAN destination)
			throws IllegalArgumentException;

	/**
	 * @param bean
	 * @param rs
	 * @param rsColumnIndex
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	void getFromResultSet(BEAN bean, ResultEntry rs, int rsColumnIndex)
			throws IllegalArgumentException, SQLException;

	/**
	 * @param bean
	 * @param rs
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	void getFromResultSet(BEAN bean, ResultEntry rs) throws IllegalArgumentException, SQLException;

	/**
	 * @param bean
	 * @param rs
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	P getValueFromResultSet(ResultEntry rs, String fieldName) throws IllegalArgumentException, SQLException;

}
