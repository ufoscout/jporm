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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.BaseVertxTestApi;
import com.jporm.session.Session;
import com.jporm.session.TransactionCallback;

public class JPOVerticleTest extends BaseVertxTestApi {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testJPOVerticleDeployment() {
		int maxConnections = 4;
		JPOVerticle.deploy(vertx, getDataSource(), maxConnections, jpo -> {
			assertNotNull(jpo);
			testComplete();
		});

		await(10, TimeUnit.SECONDS);
	}

	@Test
	public void testJPOAsyncTransaction() {
		int maxConnections = 1;

		final String name = UUID.randomUUID().toString();
		final List<People> peoples = new ArrayList<>();
		final AtomicReference<JPO> jpoRef = new AtomicReference<JPO>();

		JPOVerticle.deploy(vertx, getDataSource(), maxConnections,
				jpo -> {
					jpoRef.set(jpo);
					jpo.tx((TransactionCallback<People>) session -> {
						long id = create(session, name);
						logger.info("Saved people with id [{}] and name [{}]", id, name);
						People people = session.find(People.class, id).get();
						assertNotNull(people);
						peoples.add(people);
						return people;
					}, replyHandler -> {
						logger.error( "", replyHandler.cause() );
						assertTrue(replyHandler.succeeded());
						People people1 = replyHandler.result().body();
						logger.info("Received people reply after transactionwith id [{}] and name [{}]", people1.getId(), people1.getFirstname());

						People people2 = peoples.get(0);
						assertEquals( people2.getId(), people1.getId() );
						assertEquals( people2.getFirstname(), people1.getFirstname() );

						jpoRef.get().txVoid(session -> {
							assertNotNull(session.find(People.class, people1.getId()).get());
							testComplete();
						});
					});
				});

		await(10, TimeUnit.SECONDS);
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
