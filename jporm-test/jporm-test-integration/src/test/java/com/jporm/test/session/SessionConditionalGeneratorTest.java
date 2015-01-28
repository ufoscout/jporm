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
package com.jporm.test.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

import com.jporm.core.JPO;
import com.jporm.core.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.People_ConditionalGenerator;

/**
 *
 * @author Francesco Cina'
 *
 * Apr 1, 2012
 */
public class SessionConditionalGeneratorTest extends BaseTestAllDB {

	public SessionConditionalGeneratorTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testFailSavingASavedBean() {
		final JPO orm = getJPOrm();
		final Session session = orm.session();
		try {
			session.txVoidNow((_session) -> {
				People_ConditionalGenerator people = new People_ConditionalGenerator();
				final long originalId = people.getId();
				people.setFirstname("people name 1"); //$NON-NLS-1$
				people = session.save(people);

				assertFalse( people.getId() == originalId );
				assertTrue( people.getId() >= 0 );

				assertEquals( "people name 1", session.find(People_ConditionalGenerator.class, 1).getUnique().getFirstname()); //$NON-NLS-1$
				try {
					people.setFirstname("people name 2"); //$NON-NLS-1$
					people = session.save(people);
					System.out.println("wrong saved id: " + people.getId()); //$NON-NLS-1$
					fail("A primary violation exception should be thrown before"); //$NON-NLS-1$
				} catch (final Exception e) {
					//it's ok
				}
				assertEquals( "people name 1", session.find(People_ConditionalGenerator.class, 1).getUnique().getFirstname()); //$NON-NLS-1$
			});


		} catch (final Exception e) {
			//Nothing to do
		}
	}

	@Test
	public void testSavingBeanWithArbitraryId() {
		final JPO orm = getJPOrm();
		final Session session = orm.session();
		final long id = new Random().nextInt( Integer.MAX_VALUE );

		session.txVoidNow((_session) -> {
			try {

				People_ConditionalGenerator people = new People_ConditionalGenerator();
				people.setFirstname("people name 1"); //$NON-NLS-1$
				people.setId(id);
				people = session.save(people);

				System.out.println("saved id: " + people.getId()); //$NON-NLS-1$
				assertEquals( id, people.getId() );

				assertEquals( "people name 1", session.find(People_ConditionalGenerator.class, id).getUnique().getFirstname()); //$NON-NLS-1$

				boolean error = false;
				try {
					People_ConditionalGenerator people2 = new People_ConditionalGenerator();
					people2.setFirstname("people name 2"); //$NON-NLS-1$
					people2.setId(id);
					people2 = session.save(people2);
					people2 = session.save(people2);
					System.out.println("wrong saved id: " + people2.getId()); //$NON-NLS-1$
					fail("A primary violation exception should be thrown before getting here"); //$NON-NLS-1$
				} catch (final Exception e) {
					error = true;
				}
				assertTrue(error);

				assertEquals( "people name 1", session.find(People_ConditionalGenerator.class, id).getUnique().getFirstname()); //$NON-NLS-1$

			} catch (final Exception e) {
				//Nothing to do
			}
		});

	}
}
