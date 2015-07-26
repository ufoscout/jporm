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
package com.jporm.rx.sync.query.find;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
@Suspendable
public interface FindQueryCommonSync<BEAN> {

	BEAN fetch();

	/**
	 * Fetch the bean
	 * @return
	 */
	Optional<BEAN> fetchOptional();

	/**
	 * Fetch the bean. An {@link JpoNotUniqueResultException} is thrown if the result is not unique.
	 * @return
	 */
	BEAN fetchUnique();

	/**
	 * Return whether a bean exists with the specified id(s)
	 * @return
	 */
	Boolean exist();

	/**
	 * Execute the query returning the list of beans.
	 * @return
	 */
	List<BEAN> fetchList();

	/**
	 * Return the count of entities this query should return.
	 * @return
	 */
	Integer fetchRowCount();

}
