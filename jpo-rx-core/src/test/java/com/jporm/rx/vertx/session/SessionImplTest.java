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
package com.jporm.rx.vertx.session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import spike.User;

import com.jporm.rx.JpoRxImpl;
import com.jporm.rx.core.BaseTestApi;
import com.jporm.sql.query.clause.Insert;

public class SessionImplTest extends BaseTestApi {

	@Test
	public void testOne() throws Throwable {
		JpoRxImpl jpo = newJpo();

		final String firstname = UUID.randomUUID().toString();
		final String lastname = UUID.randomUUID().toString();
		final Insert insertUser = getSqlFactory().insert(User.class);
		insertUser.values().eq("firstname", firstname);
		insertUser.values().eq("lastname", lastname);

		jpo.getSessionProvider().getConnection()
		.thenApply(connection -> {
				List<Object> params = new ArrayList<>();
				insertUser.appendValues(params);

				getLogger().info("Execute query: {}", insertUser.renderSql());

				connection.updateWithParams(insertUser.renderSql(), params, handler2 -> {
						getLogger().info("Insert succeeded: {}", handler2.succeeded());
						UpdateResult updateResult = handler2.result();
						getLogger().info("Updated {} rows", updateResult.getUpdated());
						getLogger().info("Keys {}", updateResult.getKeys());

						final Long userId = updateResult.getKeys().getLong(0);
						getLogger().info("User id {}", userId);

						jpo.session().find(User.class, userId).get()
						.thenAccept(user -> {

//						});
//
//						jpo.session().find(User.class, userId).get(userResult -> {
//							threadAssertTrue(userResult.succeeded());
//							User user = userResult.result();
							threadAssertEquals(userId, user.getId() );
							getLogger().info("Found bean {}", user);
							getLogger().info("Found bean with id {}", user.getId());
							getLogger().info("Found bean with firstname {}", user.getFirstname());
							getLogger().info("Found bean with lastname {}", user.getLastname());
							getLogger().info("Found bean with version {}", user.getVersion());

							jpo.session().findQuery("u.firstname, u.id", User.class, "u").where().eq("u.id", userId).getList(customQueryResult -> {
								threadAssertTrue(customQueryResult.succeeded());
								threadAssertEquals(1, customQueryResult.result().size() );
								getLogger().info("Found with custom query {}", customQueryResult.result().get(0));
								threadAssertEquals(firstname, customQueryResult.result().get(0).getString("u.firstname") );
								resume();
							});

						});

				});

			});

		});

		await(1000, 1);
	}


}
