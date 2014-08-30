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

import com.jporm.JPO;
import com.jporm.exception.sql.OrmSqlDataIntegrityViolationException;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section08.AggregatedUserJob;
import com.jporm.test.domain.section08.AggregatedUserSingleJob;
import com.jporm.test.domain.section08.User;
import com.jporm.test.domain.section08.UserAddress;
import com.jporm.test.domain.section08.UserCountry;
import com.jporm.test.domain.section08.UserJob;
import com.jporm.test.domain.section08.UserJobTask;

/**
 * 
 * @author cinafr
 *
 */
@SuppressWarnings("nls")
public class BeanOneToOneRelationTest extends BaseTestAllDB {

	public BeanOneToOneRelationTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	User user;
	UserJob job;
	private UserAddress address;
	private JPO jpo = getJPOrm();
	private String firstname = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		jpo.session().doInTransaction(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {
				user = new User();
				user.setFirstname(firstname);
				user.setLastname("lastname");
				user = session.save(user).now();

				getLogger().info("Created user with id [{}]", user.getId());

				{
					job = new UserJob();
					job.setName("job1" + UUID.randomUUID());
					job.setUserId(user.getId());
					job = session.save(job).now();
					getLogger().info("Created user_job with id [{}]", job.getId());

					UserJobTask jobTask1 = new UserJobTask();
					jobTask1.setUserJobId(job.getId());
					jobTask1.setName("job1task1" + UUID.randomUUID());
					jobTask1 = session.save(jobTask1).now();
					getLogger().info("Created user_job_task with id [{}]", jobTask1.getId());

					UserJobTask jobTask2 = new UserJobTask();
					jobTask2.setUserJobId(job.getId());
					jobTask2.setName("job1task2" + UUID.randomUUID());
					jobTask2 = session.save(jobTask2).now();
					getLogger().info("Created user_job_task with id [{}]", jobTask2.getId());
				}

				{
					UserCountry country = new UserCountry();
					country.setName("Atlantis-" + new Random().nextInt());
					country = session.save(country).now();

					address = new UserAddress();
					address.setUserId(user.getId());
					address.setCountry(country);
					address = session.save(address).cascade(false).now();
				}

				return null;
			}

		});
	}

	@Test
	public void testLoadLazyFalse() {

		AggregatedUserSingleJob aggregatedUser = jpo.session().find(AggregatedUserSingleJob.class, user.getId()).get();

		assertNotNull(aggregatedUser);
		assertEquals(firstname, aggregatedUser.getFirstname());
		assertNotNull(aggregatedUser.getJob());
		assertEquals( job.getName() , aggregatedUser.getJob().getName() );
		assertEquals(2 , aggregatedUser.getJob().getJobTasks().size());
		assertNotNull(aggregatedUser.getAddress());
		assertNotNull(aggregatedUser.getAddress().getCountry());
		assertEquals(address.getCountry().getId(), aggregatedUser.getAddress().getCountry().getId());
		assertEquals(address.getCountry().getName(), aggregatedUser.getAddress().getCountry().getName());

	}

	@Test
	public void testLoadLazyTrue() {

		AggregatedUserSingleJob aggregatedUser = jpo.session().find(AggregatedUserSingleJob.class, user.getId()).lazy(true).get();

		assertNotNull(aggregatedUser);
		assertNull(aggregatedUser.getJob());
		assertEquals(firstname, aggregatedUser.getFirstname());
		assertNull(aggregatedUser.getAddress());

	}

	@Test
	public void testDeleteCascadeFalse() {

		AggregatedUserSingleJob aggregatedUser = jpo.session().find(AggregatedUserSingleJob.class, user.getId()).get();
		assertNotNull(aggregatedUser);

		try {
			jpo.session().delete(aggregatedUser).cascade(false).now();
			fail("Should not be possible to delete without violating some FKs");
		} catch (OrmSqlDataIntegrityViolationException e) {
			e.printStackTrace();
			//ok
		}

	}

	@Test
	public void testDeleteCascadeTrue() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {

			@Override
			public Void doInTransaction(final Session session) {

				AggregatedUserSingleJob aggregatedUser = session.find(AggregatedUserSingleJob.class, user.getId()).get();
				assertNotNull(aggregatedUser);

				int deleted = session.delete(aggregatedUser).now();

				assertNull(session.find(UserCountry.class, address.getCountry().getId()).get());
				assertNull(session.find(AggregatedUserSingleJob.class, user.getId()).get());

				assertEquals(6, deleted);

				return null;
			}
		});

	}

	@Test
	public void testSaveCascadeFalse() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AggregatedUserSingleJob localUser = new AggregatedUserSingleJob();
				localUser.setFirstname(firstname);
				localUser.setLastname("lastname");

				localUser.setJob( new AggregatedUserJob("job1-" + firstname) );

				localUser = session.save(localUser).cascade(false).now();
				getLogger().info("Created user with id [{}]", localUser.getId());

				AggregatedUserSingleJob userFound = session.find(AggregatedUserSingleJob.class, localUser.getId()).lazy(false).get();

				assertNotNull(userFound);
				assertNull(userFound.getJob());
				assertEquals(firstname, userFound.getFirstname());
				assertNull(userFound.getAddress());

				return null;
			}
		});

	}

	@Test
	public void testSaveCascadeTrue() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AggregatedUserSingleJob localUser = new AggregatedUserSingleJob();
				localUser.setFirstname(firstname);
				localUser.setLastname("lastname");

				localUser.setJob( new AggregatedUserJob("job1-" + firstname) );

				UserJobTask jobTask1 = new UserJobTask();
				jobTask1.setName("job1task1" + UUID.randomUUID());
				localUser.getJob().getJobTasks().add(jobTask1);

				UserJobTask jobTask2 = new UserJobTask();
				jobTask2.setName("job1task2" + UUID.randomUUID());
				localUser.getJob().getJobTasks().add(jobTask2);

				UserCountry country = new UserCountry();
				country.setName("Atlantis-" + new Random().nextInt());

				UserAddress localAddress = new UserAddress();
				localAddress.setCountry(country);
				localUser.setAddress(localAddress);

				assertNull( localAddress.getUserId() );

				localUser = session.save(localUser).now();

				assertNull( country.getId() );
				assertNotSame( localAddress , localUser.getAddress() );
				assertNotSame( country , localUser.getAddress().getCountry() );

				getLogger().info("Created user with id [{}]", localUser.getId());

				AggregatedUserSingleJob userFound = jpo.session().find(AggregatedUserSingleJob.class, localUser.getId()).lazy(false).get();

				assertNotNull(userFound);
				assertNotNull(userFound.getJob());
				assertEquals(firstname, userFound.getFirstname());
				assertTrue(userFound.getJob().getName().contains("job1-" + firstname) );
				assertEquals(2 , userFound.getJob().getJobTasks().size());
				assertNotNull(userFound.getAddress());
				assertEquals(country.getName(), userFound.getAddress().getCountry().getName() );

				return null;
			}
		});

	}

	@Test
	public void testUpdateCascadeFalse() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AggregatedUserSingleJob aggregatedUser = session.find(AggregatedUserSingleJob.class, user.getId()).lazy(false).get();
				assertEquals(firstname, aggregatedUser.getFirstname());
				assertNotNull(aggregatedUser.getJob());

				String lastname = "updated-" + UUID.randomUUID();
				aggregatedUser.setLastname(lastname);
				aggregatedUser.getJob().setName(lastname);

				aggregatedUser = session.update(aggregatedUser).cascade(false).now();
				aggregatedUser = session.find(AggregatedUserSingleJob.class, user.getId()).lazy(false).get();

				assertEquals(firstname, aggregatedUser.getFirstname());
				assertEquals(lastname, aggregatedUser.getLastname());
				assertNotNull(aggregatedUser.getJob());

				assertFalse( lastname.equals( aggregatedUser.getJob().getName() ) );

				return null;
			}
		});
	}

	@Test
	public void testUpdateCascadeTrue() {

		jpo.session().doInTransaction(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(final Session session) {
				AggregatedUserSingleJob aggregatedUser = session.find(AggregatedUserSingleJob.class, user.getId()).lazy(false).get();
				assertEquals(firstname, aggregatedUser.getFirstname());
				assertNotNull(aggregatedUser.getJob());

				String updatedName = "updated-" + UUID.randomUUID();
				aggregatedUser.setLastname(updatedName);
				Long jobChangedId = aggregatedUser.getJob().getId();
				aggregatedUser.getJob().setName(updatedName);

				aggregatedUser.getAddress().getCountry().setName(updatedName);

				aggregatedUser = session.update(aggregatedUser).now();
				aggregatedUser = session.find(AggregatedUserSingleJob.class, user.getId()).lazy(false).get();

				assertEquals(firstname, aggregatedUser.getFirstname());
				assertEquals(updatedName, aggregatedUser.getLastname());
				assertNotNull(aggregatedUser.getJob());
				assertEquals(jobChangedId, aggregatedUser.getJob().getId());
				assertEquals(updatedName, aggregatedUser.getJob().getName());
				assertEquals(updatedName, aggregatedUser.getAddress().getCountry().getName());
				return null;
			}
		});
	}
}
