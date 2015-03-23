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
package com.jporm.rx.core.session;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.rx.core.query.find.CustomFindQuery;
import com.jporm.rx.core.query.find.FindQuery;
import com.jporm.rx.core.query.find.FindQueryBase;

public interface Session {

	/**
	 * An executor to perform any kind of plain SQL statements.
	 * @return
	 */
	SqlExecutor sqlExecutor();

	/**
	 * Find a bean using the bean type and id(s).
	 * @param <BEAN>
	 * @param bean
	 * @return
	 * @throws JpoException
	 */
	<BEAN> FindQueryBase<BEAN> find(BEAN bean) throws JpoException;

	/**
	 * Find a bean using its ID.
	 *
	 * @param <BEAN>
	 * @param clazz The Class of the bean to load
	 * @param idValue the value of the identifying column of the bean
	 * @return
	 */
	<BEAN> FindQueryBase<BEAN> find(Class<BEAN> clazz, Object idValue);

	/**
	 * Create a new query to find bean
	 * @param <BEAN>
	 * @param clazz The class of the bean that will be retrieved by the query execution. The simple class name will be used as alias for the class
	 * @return
	 * @throws JpoException
	 */
	<BEAN> FindQuery<BEAN> findQuery(Class<BEAN> clazz) throws JpoException;

	/**
	 * Create a new query to find bean
	 * @param <BEAN>
	 * @param clazz The class of the bean that will be retrieved by the query execution.
	 * @param alias The alias of the class in the query.
	 * @return
	 * @throws JpoException
	 */
	<BEAN> FindQuery<BEAN> findQuery(Class<BEAN> clazz, String alias) throws JpoException;

	/**
	 * Create a new custom query that permits to specify a custom select clause.
	 * @param select the custom select clause
	 * @param clazz The class of the object that will be retrieved by the query execution.
	 * @param alias The alias of the class in the query.
	 * @return
	 * @throws JpoException
	 */
	CustomFindQuery findQuery(String select, Class<?> clazz, String alias ) throws JpoException;

	/**
	 * Create a new custom query that permits to specify which fields have to be loaded.
	 * The 'selectFields' array contains the name of the fields to fetch.
	 * @param selectFields the name of the fields to fetch
	 * @param clazz The class of the object that will be retrieved by the query execution.
	 * @param alias The alias of the class in the query.
	 * @return
	 * @throws JpoException
	 */
	CustomFindQuery findQuery(String[] selectFields, Class<?> clazz, String alias ) throws JpoException;

}
