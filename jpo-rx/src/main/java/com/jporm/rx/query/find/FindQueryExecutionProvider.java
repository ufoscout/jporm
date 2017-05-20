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
package com.jporm.rx.query.find;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.exception.JpoNotUniqueResultManyResultsException;
import com.jporm.commons.core.exception.JpoNotUniqueResultNoResultException;
import com.jporm.persistor.generator.Persistor;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.types.io.ResultEntry;

public interface FindQueryExecutionProvider<BEAN> extends SelectCommon {

	/**
	 * Return whether a bean exists with the specified id(s)
	 *
	 * @return
	 */
	default CompletableFuture<Boolean> exist() {
		return fetchRowCount().thenApply(count -> count > 0);
	}

	default CompletableFuture<BEAN> fetch() {
		final ExecutionEnvProvider<BEAN> env = getExecutionEnvProvider();
		return env.getSqlExecutor().query(sqlQuery(), sqlValues(), resultSet -> {
			if (resultSet.hasNext()) {
				final ResultEntry entry = resultSet.next();
				return env.getOrmClassTool().getPersistor().beanFromResultSet(entry, env.getIgnoredFields()).getBean();
			}
			return null;
		});
	}

	/**
	 * Execute the query returning the list of beans.
	 *
	 * @return
	 */
	default CompletableFuture<List<BEAN>> fetchList() {
		final ExecutionEnvProvider<BEAN> env = getExecutionEnvProvider();
		final Persistor<BEAN> persistor = env.getOrmClassTool().getPersistor();
		final List<String> ignoredFields = env.getIgnoredFields();
		return env.getSqlExecutor().query(sqlQuery(), sqlValues(), (rowEntry, count) -> {
			return persistor.beanFromResultSet(rowEntry, ignoredFields).getBean();
		});
	}

	/**
	 * Fetch the bean
	 *
	 * @return
	 */
	default CompletableFuture<Optional<BEAN>> fetchOptional() {
		return fetch().thenApply(Optional::ofNullable);
	}

	/**
	 * Return the count of entities this query should return.
	 *
	 * @return
	 */
	default CompletableFuture<Integer> fetchRowCount() {
		return getExecutionEnvProvider().getSqlExecutor().queryForInt(sqlRowCountQuery(), sqlValues());
	}

	/**
	 * Fetch the bean. An {@link JpoNotUniqueResultException} is thrown if the
	 * result is not unique.
	 *
	 * @return
	 */
	default CompletableFuture<BEAN> fetchUnique() {
		final ExecutionEnvProvider<BEAN> env = getExecutionEnvProvider();
		final Persistor<BEAN> persistor = env.getOrmClassTool().getPersistor();
		final List<String> ignoredFields = env.getIgnoredFields();
		return env.getSqlExecutor().query(sqlQuery(), sqlValues(), (rowEntry, count) -> {
			if (count > 0) {
				throw new JpoNotUniqueResultManyResultsException(
						"The query execution returned a number of rows different than one: more than one result found"); //$NON-NLS-1$
			}
			return persistor.beanFromResultSet(rowEntry, ignoredFields).getBean();
		}).thenApply(beans -> {
			if (beans.isEmpty()) {
				throw new JpoNotUniqueResultNoResultException("The query execution returned a number of rows different than one: no results found"); //$NON-NLS-1$
			}
			return beans.get(0);
		});
	}

	ExecutionEnvProvider<BEAN> getExecutionEnvProvider();

}
