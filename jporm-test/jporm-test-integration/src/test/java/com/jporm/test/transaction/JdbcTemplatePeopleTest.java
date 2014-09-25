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
package com.jporm.test.transaction;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.People;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class JdbcTemplatePeopleTest extends BaseTestAllDB {

	public JdbcTemplatePeopleTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testJdbcTemplateTransaction1() {
		final JPO jpOrm = getJPOrm();
		jpOrm.register(People.class);

		final long id = jpOrm.session().doInTransaction(session -> {
			return create( jpOrm );
		});

		final People loaded1 = load(jpOrm, id);
		assertNotNull(loaded1);

		try {
			jpOrm.session().doInTransaction(session -> {
				delete(jpOrm, loaded1);
				throw new RuntimeException();
			});
		} catch (RuntimeException e) {
			//ok!
		}

		final People loaded2 = load(jpOrm, id);
		assertNotNull(loaded2);

		try {
			jpOrm.session().doInTransaction(session -> {
				delete(jpOrm, loaded2);
				throw new RuntimeException();
			});
		} catch (RuntimeException e) {
			//ok!
		}

		final People loaded3 = load(jpOrm, id);
		assertNotNull(loaded3);

		jpOrm.session().doInTransactionVoid(session -> {
			delete(jpOrm, loaded3);
		});

		final People loaded4 = load(jpOrm, id);
		assertNull(loaded4);
	}


	private long create(final JPO jpOrm) {

		final long id = new Date().getTime();
		People people = new People();
		people.setId( id );
		people.setFirstname( "people" ); //$NON-NLS-1$
		people.setLastname("Wizard"); //$NON-NLS-1$

		// CREATE
		final Session conn = jpOrm.session();
		people = conn.save(people);

		System.out.println("People saved with id: " + people.getId()); //$NON-NLS-1$
		assertTrue( id == people.getId() );
		return people.getId();

	}

	private People load(final JPO jpOrm, final long id) {
		// LOAD
		final Session conn = jpOrm.session();
		final People peopleLoad1 = conn.find(People.class, new Object[]{id}).get();
		return peopleLoad1;
	}

	private void delete(final JPO jpOrm, final People people) {
		//DELETE
		final Session conn = jpOrm.session();
		conn.delete(people);
	}

}
