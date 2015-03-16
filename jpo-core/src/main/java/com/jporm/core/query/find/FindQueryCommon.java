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
package com.jporm.core.query.find;

import java.util.List;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.query.find.CommonFindQueryRoot;
import com.jporm.core.query.OrmRowMapper;

/**
 *
 * @author Francesco Cina
 *
 * 18/giu/2011
 */
public interface FindQueryCommon<BEAN> extends CommonFindQueryRoot, FindQueryBase<BEAN> {

	/**
	 * Execute the query returning the list of beans.
	 * @return
	 */
	List<BEAN> getList() throws JpoException;

	/**
	 * Return the count of entities this query should return.
	 * @return
	 */
	int getRowCount() throws JpoException;

	/**
	 * Execute the query and for each bean returned the callback method of {@link OrmRowMapper} is called.
	 * No references to created Beans are hold by the orm; in addition, one bean at time is created just before calling
	 * the callback method. This method permits to handle big amount of data with a minimum memory footprint.
	 * @param orm
	 * @throws JpoException
	 */
	void get(OrmRowMapper<BEAN> orm) throws JpoException;


}
