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
package com.jporm.core.session;

import java.util.Collection;
import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.core.query.delete.CustomDeleteQuery;
import com.jporm.core.query.find.CustomFindQuery;
import com.jporm.core.query.find.FindQuery;
import com.jporm.core.query.find.FindQueryBase;
import com.jporm.core.query.save.CustomSaveQuery;
import com.jporm.core.query.update.CustomUpdateQuery;
import com.jporm.core.transaction.TransactionApi;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 *
 */
public interface Session extends TransactionApi {

	/**
	 * Delete one bean from the database
	 *
	 * @param bean
	 * @param cascade
	 * @return
	 */
	<BEAN> int delete(BEAN bean) throws JpoException;

	/**
	 * Delete the beans from the database
	 * @param <BEAN>
	 * @param beans the beans to delete
	 * @throws JpoException
	 * @return
	 */
	<BEAN> int delete(Collection<BEAN> beans) throws JpoException;

	/**
	 * Delete entries from a specific table
	 * @param clazz the TABLE related Class
	 * @throws JpoException
	 */
	<BEAN> CustomDeleteQuery<BEAN> deleteQuery(Class<BEAN> clazz) throws JpoException;

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

	/**
	 * Persist the new bean in the database
	 * @param <BEAN>
	 * @param bean
	 * @throws JpoException
	 * @return
	 */
	<BEAN> BEAN save(BEAN bean);

	/**
	 * Persist the new beans in the database
	 * @param beans the beans to persist
	 * @param cascade whether to persist the children recursively
	 * @return
	 * @throws JpoException
	 */
	<BEAN> List<BEAN> save(Collection<BEAN> beans) throws JpoException;

	/**
	 * Permits to define a custom insert query
	 * @param clazz the TABLE related Class
	 * @throws JpoException
	 */
	<BEAN> CustomSaveQuery saveQuery(Class<BEAN> clazz) throws JpoException;

	/**
	 * For each bean in the list, update the bean if it exists,
	 * otherwise saves it
	 * @param bean the bean to persist
	 * @return
	 * @throws JpoException
	 */
	<BEAN> BEAN saveOrUpdate(BEAN bean) throws JpoException;

	/**
	 * For each bean in the list, update the bean if it exists,
	 * otherwise saves it
	 * @param beans the beans to persist
	 * @param cascade whether to saveOrUpdate the children recursively
	 * @return
	 * @throws JpoException
	 */
	<BEAN> List<BEAN> saveOrUpdate(Collection<BEAN> beans) throws JpoException;

	/**
	 * A script executor useful to execute multiple sql statement from files.
	 * @return
	 * @throws JpoException
	 */
	ScriptExecutor scriptExecutor() throws JpoException;

	/**
	 * An executor to perform any kind of plain SQL statements.
	 * @return
	 */
	SqlExecutor sqlExecutor();

	/**
	 * @param aggregatedUser
	 * @return
	 */
	<BEAN> BEAN update(BEAN bean) throws JpoException;

	/**
	 * Update the values of the existing beans in the database
	 * @param <BEAN>
	 * @param beans the beans to update
	 * @throws JpoException
	 * @return
	 */
	<BEAN> List<BEAN> update(Collection<BEAN> beans) throws JpoException;


	/**
	 * Update the entries of a specific TABLE
	 * @param clazz the TABLE related Class
	 * @throws JpoException
	 */
	<BEAN> CustomUpdateQuery updateQuery(Class<BEAN> clazz) throws JpoException;

}
