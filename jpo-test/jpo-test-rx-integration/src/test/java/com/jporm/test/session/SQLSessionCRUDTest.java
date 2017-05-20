/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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

import java.util.Random;

import org.junit.Test;

import com.jporm.rx.rxjava2.session.Session;
import com.jporm.rx.rxjava2.session.SqlSession;
import com.jporm.sql.query.where.Exp;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;

import io.reactivex.Single;

public class SQLSessionCRUDTest extends BaseTestAllDB {

	public SQLSessionCRUDTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testSQLSessionCRUD() {

		transaction((Session session) -> {

			final int id = new Random().nextInt(Integer.MAX_VALUE);
			final int age = new Random().nextInt(Integer.MAX_VALUE);

			final SqlSession sql =  session.sql();

			// INSERT
			int saveResult;
			saveResult = sql.insertInto("EMPLOYEE", "id", "age").values(id, age).execute().blockingGet().updated();
			assertEquals(1, saveResult);

			// SELECT
			final int selectedAge = sql.select("age").from("EMPLOYEE").where().eq("id", id).fetchIntUnique().blockingGet();
			assertEquals(age, selectedAge);

			// UPDATE
			final int newAge = new Random().nextInt(Integer.MAX_VALUE);
			final int updateResult = sql.update("EMPLOYEE").set("age", newAge).where(Exp.eq("id", id)).execute().blockingGet().updated();
			assertEquals(1, updateResult);

			// SELECT
			final int selectedNewAge = sql.select("age").from("EMPLOYEE").where().eq("id", id).fetchIntUnique().blockingGet();
			assertEquals(newAge, selectedNewAge);

			// DELETE
			final int deletedResult = sql.deleteFrom("EMPLOYEE").where().not().not().eq("id", id).execute().blockingGet().deleted();
			assertEquals(1, deletedResult);

			// SELECT
			final int selectedCount = sql.selectAll().from("EMPLOYEE").where().eq("id", id).fetchRowCount().blockingGet();
			assertEquals(0, selectedCount);

			return Single.just("");

		});

	}

}
