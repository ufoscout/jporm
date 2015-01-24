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
package com.jporm.core.transaction;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.domain.Employee;

public class TransactionalSessionImplTest extends BaseTestApi {

	@Test
	public void itShouldKeepTrackOfAllTheSaveUpdateDeleteQueries() {

		TransactionalSessionImpl tx = new TransactionalSessionImpl(getJPO().session());

		int count = 0;

		assertEquals(count, tx.getSaveUpdateDeleteQueries().size());

		tx.deleteQuery("");
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.deleteQuery(new ArrayList<Employee>());
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.deleteQuery(Employee.class);
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.saveQuery("");
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.saveQuery(new ArrayList<Employee>());
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.saveOrUpdateQuery("");
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.saveOrUpdateQuery(new ArrayList<Employee>());
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.updateQuery("");
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.updateQuery(new ArrayList<Employee>());
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());

		tx.updateQuery(Employee.class);
		assertEquals(++count, tx.getSaveUpdateDeleteQueries().size());
	}

}
