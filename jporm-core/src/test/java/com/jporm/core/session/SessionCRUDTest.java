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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.domain.AutoId;
import com.jporm.core.domain.Employee;
import com.jporm.query.SaveUpdateDeleteQueryRoot;
import com.jporm.query.delete.DeleteQuery;
import com.jporm.query.delete.CustomDeleteQuery;
import com.jporm.query.save.SaveQuery;
import com.jporm.query.save.SaveOrUpdateQuery;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.UpdateQuery;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionVoidCallback;
import com.jporm.transaction.TransactionalSession;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SessionCRUDTest extends BaseTestApi {

	@Test
	public void testSaveOrUpdateWithConditionGenerator() {
		getJPO().session().txNow(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final TransactionalSession session) {

				AutoId autoId = new AutoId();
				final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
				autoId.setValue(value);

				autoId = session.saveOrUpdateQuery(autoId).now();
				final Integer newId = autoId.getId();

				assertTrue( session.find(AutoId.class, newId).exist() );

				assertEquals(value, session.find(AutoId.class, newId).getOptional().get().getValue());

				final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
				autoId.setValue(newValue);

				autoId = session.saveOrUpdateQuery(autoId).now();

				assertEquals(newId, autoId.getId());
				assertEquals(newValue, session.find(AutoId.class, newId).getOptional().get().getValue());

				session.deleteQuery(autoId).now();
				assertFalse( session.find(AutoId.class, newId).exist() );

				return null;
			}
		});

	}

	@Test
	public void queriesAlreadyExecutedShouldNotBeExecutedTwiceAutomatically() {

		Employee employee = getJPO().session().txNow(new TransactionCallback<Employee>() {
			@Override
			public Employee doInTransaction(final TransactionalSession txSession) {
				Employee newEmployee = new Employee();

				//If this query is executed automatically again the transaction fails
				SaveQuery<Employee> saveQuery = txSession.saveQuery(newEmployee);
				newEmployee = saveQuery.now();

				return newEmployee;
			}
		});
		assertNotNull(employee);

	}

	@Test
	public void queriesNotExecutedShouldBeExecutedAutomaticallyBeforeTransactionEnds() {

		final List<SaveUpdateDeleteQueryRoot> queries = new ArrayList<SaveUpdateDeleteQueryRoot>();

		getJPO().session().txVoidNow(new TransactionVoidCallback() {

			@Override
			public void doInTransaction(final TransactionalSession session) {

				SaveQuery<AutoId> save = session.saveQuery(new AutoId());
				queries.add(save);

				SaveOrUpdateQuery<AutoId> saveOrUpdate = session.saveOrUpdateQuery(new AutoId());
				queries.add(saveOrUpdate);

				UpdateQuery<AutoId> update = session.updateQuery(session.saveQuery(new AutoId()).now());
				queries.add(update);

				CustomUpdateQuery updateQuery = session.updateQuery(AutoId.class).set().eq("value", "value").where().eq("value", "value").query();
				queries.add(updateQuery);

				DeleteQuery<AutoId> delete = session.deleteQuery(new AutoId());
				queries.add(delete);

				CustomDeleteQuery<AutoId> deleteQuery = session.deleteQuery(AutoId.class).where().eq("value", "value").query();
				queries.add(deleteQuery);

				checkExecution(queries, false);
			}
		});

		checkExecution(queries, true);

	}

	private void checkExecution(List<SaveUpdateDeleteQueryRoot> queries, boolean executed) {
		queries.forEach(query -> {
			assertEquals(executed, query.isExecuted());
		});
	}

}
