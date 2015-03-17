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
package com.jporm.core.query.find;

import java.util.Optional;

import com.jporm.commons.core.exception.JpoNotUniqueResultException;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 22, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public interface FindQueryBase<BEAN> {

	/**
	 * Fetch the bean
	 * @return
	 */
	BEAN get();

	/**
	 * Fetch the bean
	 * @return
	 */
	Optional<BEAN> getOptional();

	/**
	 * Fetch the bean. An {@link JpoNotUniqueResultException} is thrown if the result is not unique.
	 * @return
	 */
	BEAN getUnique();

	/**
	 * Return whether a bean exists with the specified id(s)
	 * @return
	 */
	boolean exist();

}
