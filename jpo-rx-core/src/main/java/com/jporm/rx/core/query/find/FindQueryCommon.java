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
package com.jporm.rx.core.query.find;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.io.RowMapper;

/**
 *
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
public interface FindQueryCommon<BEAN> {

	CompletableFuture<BEAN> get();

	/**
	 * Fetch the bean
	 * @return
	 */
	CompletableFuture<Optional<BEAN>> getOptional();

	/**
	 * Fetch the bean. An {@link JpoNotUniqueResultException} is thrown if the result is not unique.
	 * @return
	 */
	CompletableFuture<BEAN> getUnique();

	/**
	 * Return whether a bean exists with the specified id(s)
	 * @return
	 */
	CompletableFuture<Boolean> exist();

	/**
	 * Execute the query returning the list of beans.
	 * @return
	 */
	CompletableFuture<List<BEAN>> getList();

	/**
	 * Return the count of entities this query should return.
	 * @return
	 */
	CompletableFuture<Integer> getRowCount();

	/**
	 * Execute the query and for each bean returned the callback method of {@link RowMapper} is called.
	 * No references to created Beans are hold by the orm; in addition, one bean at time is created just before calling
	 * the callback method. This method permits to handle big amount of data with a minimum memory footprint.
	 * @param orm
	 */
	CompletableFuture<Void> get(RowMapper<BEAN> orm);

}
