/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.rm.kotlin.query.find;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.persistor.generator.Persistor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.types.io.ResultEntry;

import javax.swing.tree.RowMapper;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface FindQueryExecutionProvider<BEAN> extends SelectCommon {

	/**
	 * Return whether at least one entries exists that matches the query. It is
	 * equivalent to fetchRowCount()>0
	 *
	 * @return
	 */
	public default boolean exist() {
		return fetchRowCount() > 0;
	}

	/**
	 * Execute the query returning the list of beans.
	 *
	 * @return
	 */
	public default List<BEAN> fetchAll() {
		return fetchAll((final BEAN newObject, final Integer rowCount) -> {
			return newObject;
		});
	}

	/**
	 * Execute the query and for each bean returned the callback method of
	 * {@link RowMapper} is called. No references to created Beans are hold by
	 * the orm; in addition, one bean at time is created just before calling the
	 * callback method. This method permits to handle big amount of data with a
	 * minimum memory footprint.
	 *
	 * @param orm
	 * @throws JpoException
	 */
	public default void fetchAll(final BiConsumer<BEAN, Integer> beanReader) throws JpoException {
		final Persistor<BEAN> persistor = getExecutionEnvProvider().getOrmClassTool().getPersistor();
		final List<String> ignoredFields = getExecutionEnvProvider().getIgnoredFields();
		getExecutionEnvProvider().getSqlExecutor().query(sqlQuery(), sqlValues(), (entry, rowCount) -> {
			beanReader.accept(persistor.beanFromResultSet(entry, ignoredFields), rowCount);
		});
	}

	/**
	 * Execute the query and for each bean returned the callback method of
	 * {@link RowMapper} is called. No references to created Beans are hold by
	 * the orm; in addition, one bean at time is created just before calling the
	 * callback method. This method permits to handle big amount of data with a
	 * minimum memory footprint.
	 *
	 * @param orm
	 * @throws JpoException
	 */
	public default <R> List<R> fetchAll(final BiFunction<BEAN, Integer, R> beanReader) throws JpoException {
		final Persistor<BEAN> persistor = getExecutionEnvProvider().getOrmClassTool().getPersistor();
		final List<String> ignoredFields = getExecutionEnvProvider().getIgnoredFields();
		return getExecutionEnvProvider().getSqlExecutor().query(sqlQuery(), sqlValues(), (resultEntry, rowCount) -> {
			return beanReader.apply(persistor.beanFromResultSet(resultEntry, ignoredFields), rowCount);
		});
	}

	/**
	 * Fetch the bean
	 *
	 * @return
	 */
	public default BEAN fetchOne() throws JpoException {
		return getExecutionEnvProvider().getSqlExecutor().query(sqlQuery(), sqlValues(), resultSet -> {
			if (resultSet.hasNext()) {
				final ResultEntry entry = resultSet.next();
				final Persistor<BEAN> persistor = getExecutionEnvProvider().getOrmClassTool().getPersistor();
				return persistor.beanFromResultSet(entry, getExecutionEnvProvider().getIgnoredFields());
			}
			return null;
		});
	}

	/**
	 * Fetch the bean
	 *
	 * @return
	 */
	public default Optional<BEAN> fetchOneOptional() throws JpoException {
		return Optional.ofNullable(fetchOne());
	}

	/**
	 * Fetch the bean. An {@link JpoNotUniqueResultException} is thrown if the
	 * result is not unique.
	 *
	 * @return
	 */
	public default BEAN fetchOneUnique() throws JpoNotUniqueResultException {
		final GenericWrapper<BEAN> wrapper = new GenericWrapper<>(null);
		fetchAll((final BEAN newObject, final Integer rowCount) -> {
			if (rowCount > 0) {
				throw new JpoNotUniqueResultManyResultsException(
						"The query execution returned a number of rows different than one: more than one result found");
			}
			wrapper.setValue(newObject);
		});
		if (wrapper.getValue() == null) {
			throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found");
		}
		return wrapper.getValue();
	}

	/**
	 * Return the count of entities this query should return.
	 *
	 * @return
	 */
	public default int fetchRowCount() {
		return getExecutionEnvProvider().getSqlExecutor().queryForIntUnique(sqlRowCountQuery(), sqlValues());
	}

	ExecutionEnvProvider<BEAN> getExecutionEnvProvider();

}
