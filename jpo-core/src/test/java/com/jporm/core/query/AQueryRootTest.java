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
 *          ON : Mar 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.query;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.domain.Employee;
import com.jporm.core.query.find.impl.FindQueryImpl;
import com.jporm.core.session.Session;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class AQueryRootTest extends BaseTestApi {

	@Test
	public void benchmark() {

		Session session = getJPO().session();
		String uniqueKey = UUID.randomUUID().toString();
		int queries = 100000;

		Date now = new Date();
		for (int i=0; i<queries; i++) {
			session.findQuery(Employee.class).where().eq("id", "id").root().renderSql();  //$NON-NLS-1$//$NON-NLS-2$
		}

		getLogger().debug(queries + " rendering time without render cache: " + (new Date().getTime() - now.getTime())); //$NON-NLS-1$

		now = new Date();
		for (int i=0; i<queries; i++) {
			FindQueryImpl<Employee> query = (FindQueryImpl<Employee>) session.findQuery(Employee.class);
			query.cachedRender(uniqueKey);
			query.where().eq("id", "id").root().renderSql();  //$NON-NLS-1$//$NON-NLS-2$
		}
		getLogger().debug(queries + " rendering time with render cache: " + (new Date().getTime() - now.getTime())); //$NON-NLS-1$
	}

}
