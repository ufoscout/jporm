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

import static org.junit.Assert.*;

import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.CascadeUser;
import com.jporm.test.domain.section08.CascadeUserAddress;
import com.jporm.test.domain.section08.CascadeUserJob;
import com.jporm.test.domain.section08.UserCountry;
import com.jporm.test.domain.section08.UserJobTask;

/**
 * 
 * @author cinafr
 *
 */
@SuppressWarnings("nls")
public class BeanRelationsCascadeTypeTest extends BaseTestAllDB {

	private String firstname = UUID.randomUUID().toString();
	private UserCountry country;

	public BeanRelationsCascadeTypeTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Before
	public void setUp() {

		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				country = new UserCountry();
				country.setName("Country-" + new Random().nextInt());
				country = session.save(country).now();

				return null;
			}
		});
	}


	@Test
	public void testCascade1() {

		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				// SAVE
				CascadeUser localUser = new CascadeUser();
				localUser.setFirstname(firstname);
				localUser.setLastname("lastname");

				localUser.getJobs().add(new CascadeUserJob("job1-" + firstname));

				CascadeUserJob job2 = new CascadeUserJob("job2-" + firstname);
				localUser.getJobs().add(job2);

				UserJobTask jobTask1 = new UserJobTask();
				jobTask1.setName("job1task1" + UUID.randomUUID());
				job2.setJobTask(jobTask1);

				CascadeUserAddress address = new CascadeUserAddress();
				address.setCountry(country);
				localUser.setAddress(address);

				localUser = session.save(localUser).now();
				assertNotNull(localUser.getAddress());
				assertNotNull(localUser.getAddress().getCountry());

				// LOAD
				CascadeUser userFound = session.find(CascadeUser.class, localUser.getId()).get();

				assertNotNull(userFound);
				assertNotNull(userFound.getJobs());
				assertNotNull(userFound.getAddress());
				assertNotNull(userFound.getAddress().getCountry());
				assertEquals(firstname, userFound.getFirstname());
				assertEquals(country.getName(), localUser.getAddress().getCountry().getName());
				assertFalse(userFound.getJobs().isEmpty());
				assertEquals(2, userFound.getJobs().size());

				for( CascadeUserJob job : userFound.getJobs() ) {
					if ( job.getName().contains("job1-" + firstname) ) {
						assertNull(job.getJobTask());
						continue;
					}
					if ( job.getName().contains("job2-" + firstname) ) {
						assertNotNull(job.getJobTask());
						continue;
					}
					fail("job with unexpected name found: [" + job.getName() + "]");
				}


				String jobNewName = "updatedJobname-" + firstname;
				//SAVE OR UPDATE
				userFound.setLastname("updatedLastname-" + firstname);
				userFound.getJobs().get(0).setName(jobNewName);

				CascadeUserJob job3 = new CascadeUserJob("job3-" + firstname);
				userFound.getJobs().add(job3);

				UserJobTask jobTask3 = new UserJobTask();
				jobTask3.setName("job3task3-" + firstname);
				job3.setJobTask(jobTask3);

				userFound.getAddress().getCountry().setName("newNameThatWillNotBeSaved");

				userFound = session.saveOrUpdate(userFound).now();
				assertNotNull(userFound.getAddress());
				assertNotNull(userFound.getAddress().getCountry());
				assertEquals(3, userFound.getJobs().size());

				//LOAD
				userFound = session.find(CascadeUser.class, localUser.getId()).get();

				assertNotNull(userFound);
				assertNotNull(userFound.getJobs());
				assertEquals(firstname, userFound.getFirstname());
				assertFalse(userFound.getJobs().isEmpty());
				assertEquals(3, userFound.getJobs().size());
				assertEquals(country.getName(), localUser.getAddress().getCountry().getName());

				for( CascadeUserJob job : userFound.getJobs() ) {
					if ( job.getName().contains(jobNewName) ) {
						fail("This should not be updated due to the cascade relation specified");
					}
					if ( job.getName().contains("job2-" + firstname) ) {
						//						assertEquals(2 , job.getJobTasks().size());
					}
					if ( job.getName().contains("job3-" + firstname) ) {
						assertNotNull(job.getJobTask());
						assertEquals("job3task3-" + firstname , job.getJobTask().getName());
					}
				}

				//DELETE
				assertTrue( session.delete(localUser).now() > 0 );
				assertNull( session.find(CascadeUser.class, localUser.getId()).get() );
				assertNotNull( session.find(UserCountry.class, country.getId()).get() );

				return null;
			}
		});

	}

}
