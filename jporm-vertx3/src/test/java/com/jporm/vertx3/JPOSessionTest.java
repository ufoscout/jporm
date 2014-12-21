/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.vertx3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.BaseVertxTestApi;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;
import com.jporm.session.TransactionCallbackVoid;

public class JPOSessionTest extends BaseVertxTestApi {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public <T> void testJPOSession() throws InterruptedException {
		com.jporm.JPO jpo = getJPO();

		final Long id = jpo.session().doInTransaction(new TransactionCallback<Long>() {
			@Override
			public Long doInTransaction(final Session session) {
				//				Long _id = create(session, "");
				//				session.find(People.class, _id).get();
				//				return _id;


				long _id = create(session, "");
				logger.info("Saved people with id [{}] and name [{}]", _id, "");
				People people = session.find(People.class, _id).get();
				assertNotNull(people);
				return _id;

			}
		});

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				jpo.session().doInTransactionVoid(new TransactionCallbackVoid() {
					@Override
					public void doInTransaction(final Session session) {
						People found = session.find(People.class, id).get();
						logger.info("Found: " + found);
						assertNotNull(found);
					}
				});
			}});

		thread.start();
		thread.join();

	}

	@Test
	public <T> void testJPOWithLambdaSession() throws InterruptedException {
		com.jporm.JPO jpo = getJPO();

		final Long id = jpo.session().doInTransaction(session -> {
			long _id = create(session, "");
			logger.info("Saved people with id [{}] and name [{}]", _id, "");
			People people = session.find(People.class, _id).get();
			assertNotNull(people);
			return _id;
		});

		final BlockingQueue<People> queue = new ArrayBlockingQueue<People>(10);
		new Thread(() -> jpo.session().doInTransactionVoid(session -> {
			People found = session.find(People.class, id).get();
			logger.info("Found: " + found);
			try {
				queue.put(found);
			} catch (Exception e) {
				e.printStackTrace();
			}
		})).start();

		assertNotNull( queue.poll(2, TimeUnit.SECONDS) );

	}


	long create(final Session conn, final String firstName) {

		//		final long id = new Date().getTime();
		People people = new People();
		//		people.setId( id );
		people.setFirstname( firstName );
		people.setLastname("Wizard"); //$NON-NLS-1$

		// CREATE
		people = conn.save(people);

		logger.info("People [" + firstName + "] saved with id: " + people.getId()); //$NON-NLS-1$ //$NON-NLS-2$
		//		assertFalse( id == people.getId() );
		return people.getId();

	}
}
