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
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.People;

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

		transaction(session -> {
			try {
				final long id = new Random().nextInt(Integer.MAX_VALUE);
				assertFalse( session.find(People.class, id).getRowCount().get()>0 );

				// CREATE
				People people_ = new People();
				people_.setId( id );
				people_.setFirstname( "people" ); //$NON-NLS-1$
				people_.setLastname("Wizard"); //$NON-NLS-1$
				people_ = session.save(people_).get();

				// LOAD
				People peopleLoad1_ = session.find(People.class, id).get().get();
				assertNotNull(peopleLoad1_);
				assertEquals( people_.getId(), peopleLoad1_.getId() );
				assertEquals( people_.getFirstname(), peopleLoad1_.getFirstname() );
				assertEquals( people_.getLastname(), peopleLoad1_.getLastname() );

				//UPDATE
				peopleLoad1_.setFirstname("Wizard name"); //$NON-NLS-1$
				peopleLoad1_ = session.update(peopleLoad1_).get();

				// LOAD
				final People peopleLoad2 = session.find(People.class, id).getUnique().get();
				assertNotNull(peopleLoad2);
				assertEquals( peopleLoad1_.getId(), peopleLoad2.getId() );
				assertEquals( peopleLoad1_.getFirstname(), peopleLoad2.getFirstname() );
				assertEquals( peopleLoad1_.getLastname(), peopleLoad2.getLastname() );

				//DELETE
				assertTrue( session.delete(peopleLoad2).get().deleted() == 1 );

				assertFalse(session.find(People.class, id).getOptional().get().isPresent());
				return CompletableFuture.completedFuture(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});

	}

}
