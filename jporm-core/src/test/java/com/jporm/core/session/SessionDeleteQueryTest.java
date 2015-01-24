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
 *          ON : Feb 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.domain.AutoId;
import com.jporm.core.domain.People;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.session.Session;
import com.jporm.transaction.TransactionVoidCallback;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SessionDeleteQueryTest extends BaseTestApi {

	@Test
	public void testDeleteCollectionOfDifferentBeans() {
		getJPO().session().txVoidNow(new TransactionVoidCallback() {

			@Override
			public void doInTransaction(final Session session) {

				List<Object> beans = new ArrayList<>();
				beans.add(session.save(new AutoId()));
				beans.add(session.save(new People()));
				beans.add(session.save(new AutoId()));
				beans.add(session.save(new People()));
				beans.add(session.save(new AutoId()));
				beans.add(session.save(new People()));

				beans.forEach(bean -> assertTrue(session.find(bean).exist()));

				DeleteQuery deleteQuery = session.deleteQuery(beans);

				getLogger().info("DeleteQuery sql is \n------------------\n{}\n--------------\n", deleteQuery.renderSql());

				assertEquals(beans.size() , deleteQuery.now());

				beans.forEach(bean -> assertFalse(session.find(bean).exist()));

			}
		});

	}

}
