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
package com.jporm.rx.core.session.impl;

import java.util.UUID;

import org.junit.Test;

import com.jporm.rx.JpoRX;
import com.jporm.rx.core.BaseTestApi;
import com.jporm.rx.core.session.Session;

public class SessionImplCRUDTest extends BaseTestApi {

	@Test
	public void testOne() throws Throwable {
		JpoRX jpo = newJpo();
		final String firstname = UUID.randomUUID().toString();
		final String lastname = UUID.randomUUID().toString();

		User newUser = new User();
		newUser.setFirstname(firstname);
		newUser.setLastname(lastname);

		Session session = jpo.session();

		session.save(newUser)
		.thenAccept(savedUser -> {

			threadAssertNotNull(savedUser);
			threadAssertNotNull(savedUser.getId());
			threadAssertNotNull(savedUser.getVersion());

			jpo.session().find(User.class, savedUser.getId()).get()
			.thenAccept(user -> {

				getLogger().info("Found bean {}", user);
				threadAssertNotNull(user);
				getLogger().info("Found bean with id {}", user.getId());
				getLogger().info("Found bean with firstname {}", user.getFirstname());
				getLogger().info("Found bean with lastname {}", user.getLastname());
				getLogger().info("Found bean with version {}", user.getVersion());

				threadAssertEquals(savedUser.getId(), user.getId() );
				threadAssertEquals(firstname, user.getFirstname() );

//				jpo.session().findQuery("u.firstname, u.id", User.class, "u").where().eq("u.id", userId).getList(customQueryResult -> {
//					threadAssertTrue(customQueryResult.succeeded());
//					threadAssertEquals(1, customQueryResult.result().size() );
//					getLogger().info("Found with custom query {}", customQueryResult.result().get(0));
//					threadAssertEquals(firstname, customQueryResult.result().get(0).getString("u.firstname") );
//				});

				resume();
			});
		});
		await(2000, 1);
	}

}
