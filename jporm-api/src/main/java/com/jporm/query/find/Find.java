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
 *          ON : Feb 22, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.query.find;

import com.jporm.exception.OrmNotUniqueResultException;
import com.jporm.query.QueryRoot;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 22, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public interface Find<BEAN> extends QueryRoot {

	/**
	 * Fetch the bean
	 * @return
	 */
	BEAN get();

	/**
	 * Fetch the bean. An {@link OrmNotUniqueResultException} is thrown if the result is not unique.
	 * @return
	 */
	BEAN getUnique();

	/**
	 * Return whether a bean exists with the specified id(s)
	 * @return
	 */
	boolean exist();

	/**
	 * Activate the cache for this query.
	 * @param cacheName the of the cache to use
	 * @return
	 */
	Find<BEAN> cache(String cacheName);

	/**
	 * The value of the Bean fields listed will not be fetched from the DB. This is useful to load only a partial Bean
	 * to reduce the amount of work of the DB. Normally this is used to avoid loading LOB values when not needed.
	 * @param fields
	 * @return
	 */
	Find<BEAN> ignore(String... fields);

	/**
	 * The value of the Bean fields listed will not be fetched from the DB. This is useful to load only a partial Bean
	 * to reduce the amount of work of the DB. Normally this is used to avoid loading LOB values when not needed.
	 * If 'ignoreFieldsCondition' is false the fields will not be ignored fetched.
	 * @param fields
	 * @param ignoreFieldsCondition
	 * @return
	 */
	Find<BEAN> ignore(boolean ignoreFieldsCondition, String... fields);

}
