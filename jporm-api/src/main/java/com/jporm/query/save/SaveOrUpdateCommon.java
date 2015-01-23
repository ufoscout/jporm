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
package com.jporm.query.save;

import com.jporm.query.SaveUpdateDeleteQueryRoot;

public interface SaveOrUpdateCommon<BEAN, T extends SaveOrUpdateCommon<?,?>> extends SaveUpdateDeleteQueryRoot {

	/**
	 * Perform the action and return the number of affected rows.
	 * @return
	 */
	BEAN now();

	/**
	 * Set the query timeout for the query in seconds.
	 */
	T timeout(int seconds);

	/**
	 * Return the query timeout for the query in seconds.
	 */
	int getTimeout();

}