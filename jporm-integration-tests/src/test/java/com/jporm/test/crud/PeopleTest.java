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
package com.jporm.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.People;
import com.jporm.transaction.Transaction;

/**
 * 
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class PeopleTest extends BaseTestAllDB {

	public PeopleTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCrudPeople() {
		final JPO jpOrm = getJPOrm();

		jpOrm.register(People.class);

		long id = new Random().nextInt(Integer.MAX_VALUE);
		People people = new People();
		people.setId( id );
		people.setFirstname( "people" ); //$NON-NLS-1$
		people.setLastname("Wizard"); //$NON-NLS-1$

		assertFalse( jpOrm.session().find(people).exist() );

		// CREATE
		final Session conn = jpOrm.session();
		Transaction tx = conn.transaction();
		people = conn.save(people);
		tx.commit();

		System.out.println("People saved with id: " + people.getId()); //$NON-NLS-1$
		assertTrue( id == people.getId() );
		id = people.getId();

		assertTrue( jpOrm.session().find(people).exist() );

		// LOAD
		tx = conn.transaction();
		People peopleLoad1 = conn.find(People.class, id).get();
		assertNotNull(peopleLoad1);
		assertEquals( people.getId(), peopleLoad1.getId() );
		assertEquals( people.getFirstname(), peopleLoad1.getFirstname() );
		assertEquals( people.getLastname(), peopleLoad1.getLastname() );

		//UPDATE
		peopleLoad1.setFirstname("Wizard name"); //$NON-NLS-1$
		peopleLoad1 = conn.update(peopleLoad1);
		tx.commit();

		// LOAD
		tx = conn.transaction();
		final People peopleLoad2 = conn.find(People.class, new Object[]{id}).get();
		assertNotNull(peopleLoad2);
		assertEquals( peopleLoad1.getId(), peopleLoad2.getId() );
		assertEquals( peopleLoad1.getFirstname(), peopleLoad2.getFirstname() );
		assertEquals( peopleLoad1.getLastname(), peopleLoad2.getLastname() );

		//DELETE
		conn.delete(peopleLoad2);
		tx.commit();

		tx = conn.transaction();
		final People peopleLoad3 = conn.find(People.class, new Object[]{id}).get();
		assertNull(peopleLoad3);
		tx.commit();

	}

}
