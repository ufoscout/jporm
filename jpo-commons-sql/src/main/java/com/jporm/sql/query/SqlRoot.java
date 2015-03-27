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
package com.jporm.sql.query;

import java.util.List;

import com.jporm.sql.dialect.DBProfile;

/**
 *
 * @author Francesco Cina'
 *
 * Mar 31, 2012
 */
public interface SqlRoot extends Sql {

	/**
	 * Return the sql query generated by this IQuery Object
	 * @return
	 */
	String renderSql(DBProfile dbprofile);

	/**
	 * Append to the string buffer the sql query generated by this IQuery Object
	 * @param queryBuilder
	 */
	void renderSql(DBProfile dbprofile, StringBuilder queryBuilder);

	/**
	 * Append to the list all the values of the expression's elements
	 * @return
	 */
	void appendValues(List<Object> values);

}
