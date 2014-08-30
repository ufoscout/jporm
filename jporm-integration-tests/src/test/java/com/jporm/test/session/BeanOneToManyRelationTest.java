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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.JPO;
import com.jporm.exception.sql.OrmSqlDataIntegrityViolationException;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.AggregatedUserJob;
import com.jporm.test.domain.section08.AggregatedUserManyJob;
import com.jporm.test.domain.section08.User;
import com.jporm.test.domain.section08.UserJob;
import com.jporm.test.domain.section08.UserJobTask;

/**
 * 
 * @author cinafr
 *
 */
@SuppressWarnings("nls")
public class BeanOneToManyRelationTest extends BaseTestAllDB {

	public BeanOneToManyRelationTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	User user;
	List<UserJob> jobs = new ArrayList<UserJob>();
	private JPO jpo;
	private String firstname = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		jpo = getJPOrm();
		jpo.session().doInTransaction(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {
				user = new User();
				user.setFirstname(firstname);
				user.setLastname("lastname");
				user = session.save(user).now();

				getLogger().info("Created user with id [{}]", user.getId());

				{
					UserJob job1 = new UserJob();
					job1.setName("job1" + UUID.randomUUID());
					job1.setUserId(user.getId());
					job1 = session.save(job1).now();
					getLogger().info("Created user_job with id [{}]", job1.getId());
					jobs.add( job1 );

					UserJobTask jobTask1 = new UserJobTask();
					jobTask1.setUserJobId(job1.getId());
					jobTask1.setName("job1task1" + UUID.randomUUID());
					jobTask1 = session.save(jobTask1).now();
					getLogger().info("Created user_job_task with id [{}]", jobTask1.getId());

					UserJobTask jobTask2 = new UserJobTask();
					jobTask2.setUserJobId(job1.getId());
					jobTask2.setName("job1task2" + UUID.randomUUID());
					jobTask2 = session.save(jobTask2).now();
					getLogger().info("Created user_job_task with id [{}]", jobTask2.getId());

				}

				{
					UserJob job2 = new UserJob();
					job2.setName("job2" + UUID.randomUUID());
					job2.setUserId(user.getId());
					job2 = session.save(job2).now();
					getLogger().info("Created user_job with id [{}]", job2.getId());
					jobs.add( job2 );
				}
				return null;
			}

		});
	}

	@Test
	public void testLoadLazyFalse() {

		AggregatedUserManyJob aggregatedUser = jpo.session().find(AggregatedUserManyJob.class, user.getId()).get();

		assertNotNull(aggregatedUser);
		assertEquals(firstname, aggregatedUser.getFirstname());
		assertNotNull(aggregatedUser.getJobs());
		assertFalse(aggregatedUser.getJobs().isEmpty());
		assertEquals( 2 , aggregatedUser.getJobs().size() );

		for( AggregatedUserJob job : aggregatedUser.getJobs() ) {
			if ( job.getName().contains("job1") ) {
				assertEquals(2 , job.getJobTasks().size());
				continue;
			}
			if ( job.getName().contains("job2") ) {
				assertEquals(0 , job.getJobTasks().size());
				continue;
			}
			fail("job with unexpected name found: [" + job.getName() + "]");
		}
	}

	@Test
	public void testLoadLazyTrue() {

		AggregatedUserManyJob aggregatedUser = jpo.session().find(AggregatedUserManyJob.class, user.getId()).lazy(true).get();

		assertNotNull(aggregatedUser);
		assertNotNull(aggregatedUser.getJobs());
		assertEquals(firstname, aggregatedUser.getFirstname());
		assertTrue(aggregatedUser.getJobs().isEmpty());

	}

	@Test
	public void testDeleteCascadeFalse() {

		AggregatedUserManyJob aggregatedUser = jpo.session().find(AggregatedUserManyJob.class, user.getId()).get();
		assertNotNull(aggregatedUser);

		try {
			jpo.session().delete(aggregatedUser).cascade(false).now();
			fail("Should not be possible to delete without violating some FKs");
		} catch (OrmSqlDataIntegrityViolationException e) {
			//ok
		}

	}

	@Test
	public void testDeleteCascadeTrue() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {

				AggregatedUserManyJob aggregatedUser = session.find(AggregatedUserManyJob.class, user.getId()).get();
				assertNotNull(aggregatedUser);

				assertEquals(5, session.delete(aggregatedUser).now());

				assertNull(session.find(AggregatedUserManyJob.class, user.getId()).get());

				return null;
			}
		});

	}

	@Test
	public void testSaveCascadeFalse() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AggregatedUserManyJob localUser = new AggregatedUserManyJob();
				localUser.setFirstname(firstname);
				localUser.setLastname("lastname");

				localUser.getJobs().add(new AggregatedUserJob("job1-" + firstname));

				localUser = session.save(localUser).cascade(false).now();
				getLogger().info("Created user with id [{}]", localUser.getId());

				AggregatedUserManyJob userFound = session.find(AggregatedUserManyJob.class, localUser.getId()).get();

				assertNotNull(userFound);
				assertNotNull(userFound.getJobs());
				assertEquals(firstname, userFound.getFirstname());
				assertTrue(userFound.getJobs().isEmpty());

				return null;
			}
		});

	}

	@Test
	public void testSaveCascadeTrue() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
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

				localUser = session.save(localUser).now();
				getLogger().info("Created user with id [{}]", localUser.getId());

				AggregatedUserManyJob userFound = jpo.session().find(AggregatedUserManyJob.class, localUser.getId()).lazy(false).get();

				assertNotNull(userFound);
				assertNotNull(userFound.getJobs());
				assertEquals(firstname, userFound.getFirstname());
				assertFalse(userFound.getJobs().isEmpty());

				for( AggregatedUserJob job : userFound.getJobs() ) {
					if ( job.getName().contains("job1-" + firstname) ) {
						assertEquals(0 , job.getJobTasks().size());
						continue;
					}
					if ( job.getName().contains("job2-" + firstname) ) {
						assertEquals(2 , job.getJobTasks().size());
						continue;
					}
					fail("job with unexpected name found: [" + job.getName() + "]");
				}

				return null;
			}
		});

	}

	@Test
	public void testUpdateCascadeFalse() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AggregatedUserManyJob aggregatedUser = session.find(AggregatedUserManyJob.class, user.getId()).get();
				assertEquals(firstname, aggregatedUser.getFirstname());
				assertNotNull(aggregatedUser.getJobs());
				assertFalse(aggregatedUser.getJobs().isEmpty());
				assertEquals( 2 , aggregatedUser.getJobs().size() );


				String lastname = "updated-" + UUID.randomUUID();
				aggregatedUser.setLastname(lastname);
				aggregatedUser.getJobs().get(0).setName(lastname);

				aggregatedUser = session.update(aggregatedUser).cascade(false).now();
				aggregatedUser = session.find(AggregatedUserManyJob.class, user.getId()).get();

				assertEquals(firstname, aggregatedUser.getFirstname());
				assertEquals(lastname, aggregatedUser.getLastname());
				assertNotNull(aggregatedUser.getJobs());
				assertFalse(aggregatedUser.getJobs().isEmpty());
				assertEquals( 2 , aggregatedUser.getJobs().size() );

				for (AggregatedUserJob job : aggregatedUser.getJobs()) {
					if (job.getName().equals(lastname)) {
						fail("The job job should not be updated");
					}
				}
				return null;
			}
		});
	}

	@Test
	public void testUpdateCascadeTrue() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AggregatedUserManyJob aggregatedUser = session.find(AggregatedUserManyJob.class, user.getId()).get();
				assertEquals(firstname, aggregatedUser.getFirstname());
				assertNotNull(aggregatedUser.getJobs());
				assertFalse(aggregatedUser.getJobs().isEmpty());
				assertEquals( 2 , aggregatedUser.getJobs().size() );


				String lastname = "updated-" + UUID.randomUUID();
				aggregatedUser.setLastname(lastname);
				AggregatedUserJob jobChanged = aggregatedUser.getJobs().get(0);
				Long jobChangedId = jobChanged.getId();
				jobChanged.setName(lastname);

				aggregatedUser = session.update(aggregatedUser).now();
				aggregatedUser = session.find(AggregatedUserManyJob.class, user.getId()).get();

				assertEquals(firstname, aggregatedUser.getFirstname());
				assertEquals(lastname, aggregatedUser.getLastname());
				assertNotNull(aggregatedUser.getJobs());
				assertFalse(aggregatedUser.getJobs().isEmpty());
				assertEquals( 2 , aggregatedUser.getJobs().size() );

				boolean found = false;
				for (AggregatedUserJob job : aggregatedUser.getJobs()) {
					if (job.getId().equals(jobChangedId)) {
						assertEquals( lastname , job.getName() );
						found = true;
					}
				}
				assertTrue(found);
				return null;
			}
		});
	}
}
