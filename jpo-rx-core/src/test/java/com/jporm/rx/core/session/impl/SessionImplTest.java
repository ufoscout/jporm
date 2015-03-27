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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.jporm.rx.JpoRxImpl;
import com.jporm.rx.core.BaseTestApi;
import com.jporm.sql.query.clause.Insert;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSet;

public class SessionImplTest extends BaseTestApi {

	@Test
	public void testOne() throws Throwable {
		JpoRxImpl jpo = newJpo();
		final String firstname = UUID.randomUUID().toString();
		final String lastname = UUID.randomUUID().toString();
		final Insert insertUser = getSqlFactory().insert(User.class);
		insertUser.values().eq("firstname", firstname);
		insertUser.values().eq("lastname", lastname);

		final Map<String, Long> keys = new HashMap<>();

		jpo.getSessionProvider().getDBType().thenCompose(dbType -> {
			return jpo.getSessionProvider().getConnection(true).thenCompose(connection -> {
				List<Object> params = new ArrayList<>();
				insertUser.appendValues(params);

				getLogger().info("Execute query: {}", insertUser.renderSql(dbType.getDBProfile()));
				return connection.update(insertUser.renderSql(dbType.getDBProfile()), new GeneratedKeyReader() {
					@Override
					public String[] generatedColumnNames() {
						return null;
					}

					@Override
					public void read(ResultSet generatedKeyResultSet) {
						threadAssertTrue(generatedKeyResultSet.next());
						keys.put("ID", generatedKeyResultSet.getLong(1));
					}
				}, statement -> {
					int index = 0;
					for (Object object : params) {
						statement.setObject(++index, object);
					}
				});
			}).thenAccept(updateResult -> {
				getLogger().info("Updated {} rows", updateResult.updated());
				getLogger().info("Keys {}", keys);
				final Long userId = keys.get("ID");

				jpo.session().find(User.class, userId).get().thenAccept(user -> {

					getLogger().info("Found bean {}", user);
					threadAssertNotNull(user);
					getLogger().info("Found bean with id {}", user.getId());
					getLogger().info("Found bean with firstname {}", user.getFirstname());
					getLogger().info("Found bean with lastname {}", user.getLastname());
					getLogger().info("Found bean with version {}", user.getVersion());

					threadAssertEquals(userId, user.getId());
					threadAssertEquals(firstname, user.getFirstname());

					// jpo.session().findQuery("u.firstname, u.id", User.class,
					// "u").where().eq("u.id", userId).getList(customQueryResult
					// -> {
					// threadAssertTrue(customQueryResult.succeeded());
					// threadAssertEquals(1, customQueryResult.result().size()
					// );
					// getLogger().info("Found with custom query {}",
					// customQueryResult.result().get(0));
					// threadAssertEquals(firstname,
					// customQueryResult.result().get(0).getString("u.firstname")
					// );
					// });

						resume();
					});
			});
		});
		await(2000, 1);
	}

}
