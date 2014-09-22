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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Test;

import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.AggregatedUserJob;
import com.jporm.test.domain.section08.AggregatedUserManyJob;
import com.jporm.test.domain.section08.UserAddress;
import com.jporm.test.domain.section08.UserCountry;
import com.jporm.test.domain.section08.UserJobTask;

/**
 * @author cinafr
 */
@SuppressWarnings("nls")
public class BeanRelationsCRUDTest extends BaseTestAllDB {

	private final String firstname = UUID.randomUUID().toString();

	public BeanRelationsCRUDTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testUserCountrySave() {
		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				UserCountry userCountry = new UserCountry();
				userCountry.setName("testUserCountrySave-" + UUID.randomUUID().toString());

				userCountry = session.saveOrUpdate(userCountry);
				assertNotNull(userCountry);
				assertNotNull(userCountry.getId());
				getLogger().info("UserCountry id [{}], name [{}]", userCountry.getId(), userCountry.getName());
				return null;
			}
		});
	}

	@Test
	public void testCRUD() {

		getJPOrm().session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {

				// SAVE
				AggregatedUserManyJob localUser = new AggregatedUserManyJob();
				localUser.setFirstname(firstname);
				localUser.setLastname("lastname");

				localUser.getJobs().add(new AggregatedUserJob("job1-" + firstname));

				AggregatedUserJob job2 = new AggregatedUserJob("job2-" + firstname);
				localUser.getJobs().add(job2);

				UserJobTask jobTask1 = new UserJobTask();
				jobTask1.setName("job1task1" + UUID.randomUUID());
				job2.getJobTasks().add(jobTask1);

				UserJobTask jobTask2 = new UserJobTask();
				jobTask2.setName("job1task2" + UUID.randomUUID());
				job2.getJobTasks().add(jobTask2);

				localUser.setAddress(new UserAddress());
				localUser.getAddress().setCountry(new UserCountry());
				localUser.getAddress().getCountry().setName("country-" + firstname);

				localUser = session.saveOrUpdate(localUser);

				// LOAD
				AggregatedUserManyJob userFound = session.find(AggregatedUserManyJob.class, localUser.getId()).get();

				assertNotNull(userFound);
				assertNotNull(userFound.getJobs());
				assertEquals(firstname, userFound.getFirstname());
				assertEquals("country-" + firstname, localUser.getAddress().getCountry().getName());
				assertFalse(userFound.getJobs().isEmpty());
				assertEquals(2, userFound.getJobs().size());

				for (AggregatedUserJob job : userFound.getJobs()) {
					if (job.getName().contains("job1-" + firstname)) {
						assertEquals(0, job.getJobTasks().size());
						continue;
					}
					if (job.getName().contains("job2-" + firstname)) {
						assertEquals(2, job.getJobTasks().size());
						continue;
					}
					fail("job with unexpected name found: [" + job.getName() + "]");
				}

				// SAVE OR UPDATE
				localUser.setLastname("updatedLastname-" + firstname);
				AggregatedUserJob jobChanged = localUser.getJobs().get(0);
				Long jobChangedId = jobChanged.getId();
				jobChanged.setName("updatedJobname-" + firstname);
				localUser.getAddress().getCountry().setName("updatedCountry-" + firstname);

				AggregatedUserJob job3 = new AggregatedUserJob("job3-" + firstname);
				localUser.getJobs().add(job3);

				UserJobTask jobTask3 = new UserJobTask();
				jobTask3.setName("job3task3-" + firstname);
				job3.getJobTasks().add(jobTask3);

				localUser = session.saveOrUpdate(localUser);

				// LOAD
				userFound = session.find(AggregatedUserManyJob.class, localUser.getId()).get();

				assertNotNull(userFound);
				assertNotNull(userFound.getJobs());
				assertEquals(firstname, userFound.getFirstname());
				assertFalse(userFound.getJobs().isEmpty());
				assertEquals(3, userFound.getJobs().size());
				assertEquals("updatedCountry-" + firstname, userFound.getAddress().getCountry().getName());

				for (AggregatedUserJob job : userFound.getJobs()) {
					if (job.getName().contains("updatedJobname-" + firstname)) {
						assertEquals(jobChangedId, job.getId());
						assertEquals(0, job.getJobTasks().size());
						continue;
					}
					if (job.getName().contains("job2-" + firstname)) {
						assertEquals(2, job.getJobTasks().size());
						continue;
					}
					if (job.getName().contains("job3-" + firstname)) {
						assertEquals(1, job.getJobTasks().size());
						assertEquals("job3task3-" + firstname, job.getJobTasks().get(0).getName());
						continue;
					}
					fail("job with unexpected name found: [" + job.getName() + "]");
				}

				return null;
			}
		});

	}

}
