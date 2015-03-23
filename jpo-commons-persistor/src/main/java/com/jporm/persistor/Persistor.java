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

import java.util.List;

import com.jporm.annotation.LockMode;
import com.jporm.types.io.ResultSet;

/**
 * @author Francesco Cina 22/mag/2011
 */
public interface Persistor<BEAN> {

	BeanFromResultSet<BEAN> beanFromResultSet(ResultSet rs, List<String> fieldsToIgnore);

	BEAN clone(BEAN entity);

	/**
	 * @param propertyNames
	 * @param propertyValues
	 * @return
	 */
	BEAN newInstance();

	boolean isVersionableWithLock();

	void increaseVersion(BEAN entity, boolean firstVersionNumber);

	/**
	 * Return whether in the save query there are automatically generated key (for example using a call to a Sequence in
	 * the insert query)
	 *
	 * @return
	 */
	boolean useGenerators(BEAN entity);

	void updateGeneratedValues(ResultSet rs, BEAN entity);

	boolean hasGenerator();

	/**
	 * @param javaColumnNames
	 * @param entity
	 * @return
	 */
	Object[] getPropertyValues(String[] javaColumnNames, BEAN entity);

	/**
	 * @return
	 */
	LockMode getVersionableLockMode();

}
