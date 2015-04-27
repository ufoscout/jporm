/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.vertx.session.impl;

import io.vertx.core.Vertx;

import java.util.UUID;

import org.junit.Test;

import com.jporm.rx.JpoRX;
import com.jporm.rx.session.Session;
import com.jporm.rx.vertx.BaseTestApi;
import com.jporm.test.domain.section08.CommonUser;

public class SessionImplCRUDTest extends BaseTestApi {

	@Test
	public void testOne() throws Throwable {
		JpoRX jpo = newJpo(Vertx.vertx());
		final String firstname = UUID.randomUUID().toString();
		final String lastname = UUID.randomUUID().toString();

		CommonUser newUser = new CommonUser();
		newUser.setFirstname(firstname);
		newUser.setLastname(lastname);

		Session session = jpo.session();

		//SAVE
		session.save(newUser)
		.thenAccept(savedUser -> {

			threadAssertNotNull(savedUser);
			threadAssertNotNull(savedUser.getId());
			threadAssertNotNull(savedUser.getVersion());

			//FIND
			session.findById(CommonUser.class, savedUser.getId()).fetch()
			.thenAccept(foundUser -> {

				getLogger().info("Found bean {}", foundUser);
				threadAssertNotNull(foundUser);
				getLogger().info("Found bean with id {}", foundUser.getId());
				getLogger().info("Found bean with firstname {}", foundUser.getFirstname());
				getLogger().info("Found bean with lastname {}", foundUser.getLastname());
				getLogger().info("Found bean with version {}", foundUser.getVersion());

				threadAssertEquals(savedUser.getId(), foundUser.getId() );
				threadAssertEquals(firstname, foundUser.getFirstname() );

//				jpo.session().findQuery("u.firstname, u.id", User.class, "u").where().eq("u.id", userId).getList(customQueryResult -> {
//					threadAssertTrue(customQueryResult.succeeded());
//					threadAssertEquals(1, customQueryResult.result().size() );
//					getLogger().info("Found with custom query {}", customQueryResult.result().get(0));
//					threadAssertEquals(firstname, customQueryResult.result().get(0).getString("u.firstname") );
//				});

				//UPDATE
				foundUser.setFirstname(UUID.randomUUID().toString());
				session.update(foundUser).thenAccept(updatedUser -> {

					getLogger().info("Update bean {}", updatedUser);
					threadAssertNotNull(updatedUser);
					getLogger().info("Update bean with id {}", updatedUser.getId());
					getLogger().info("Update bean with firstname {}", updatedUser.getFirstname());
					getLogger().info("Update bean with lastname {}", updatedUser.getLastname());
					getLogger().info("Update bean with version {}", updatedUser.getVersion());

					threadAssertEquals(foundUser.getId(), updatedUser.getId() );
					threadAssertEquals(foundUser.getFirstname(), updatedUser.getFirstname() );

					//The bean version should be increased
					threadAssertEquals(foundUser.getVersion() + 1, updatedUser.getVersion());


					//FIND THE UPDATED USER TO VERIFY THAT DATA HAS BEEN PERSISTED
					session.findById(CommonUser.class, updatedUser.getId()).fetch()
					.thenAccept(foundUpdatedUser -> {

						getLogger().info("Found Updated bean {}", foundUpdatedUser);
						threadAssertNotNull(foundUpdatedUser);
						getLogger().info("Found Updated bean with id {}", foundUpdatedUser.getId());
						getLogger().info("Found Updated bean with firstname {}", foundUpdatedUser.getFirstname());
						getLogger().info("Found Updated bean with lastname {}", foundUpdatedUser.getLastname());
						getLogger().info("Found Updated bean with version {}", foundUpdatedUser.getVersion());

						threadAssertEquals(updatedUser.getId(), foundUpdatedUser.getId() );
						threadAssertEquals(updatedUser.getFirstname(), foundUpdatedUser.getFirstname() );
						threadAssertEquals(updatedUser.getVersion(), foundUpdatedUser.getVersion());

						//DELETE
						session.delete(savedUser)
						.thenAccept(deleteResult -> {

							getLogger().info("User deleted");
							threadAssertNotNull(deleteResult);
							threadAssertEquals(1, deleteResult.deleted());


							//FIND DELETED USER
							session.findById(CommonUser.class, savedUser.getId()).fetch()
							.thenAccept(deletedUser -> {
								getLogger().info("Found bean {}", deletedUser);
								threadAssertNull(deletedUser);

								resume();
							});

						});

					});

				});

			});
		});
		await(2000, 1);
	}

}
