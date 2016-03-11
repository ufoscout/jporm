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

import com.jporm.rm.session.SqlSession;
import com.jporm.sql.query.where.expression.Exp;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;

public class SQLSessionCRUDTest extends BaseTestAllDB {

    public SQLSessionCRUDTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testSQLSessionCRUD() {

        getJPO().transaction().execute(session -> {

            final int id = new Random().nextInt(Integer.MAX_VALUE);
            final int age = new Random().nextInt(Integer.MAX_VALUE);

            SqlSession sql =  session.sql();

            // INSERT
            int saveResult = sql.insertInto("Employee", "id", "age").values(id, age).execute();
            assertEquals(1, saveResult);

            // SELECT
            int selectedAge = sql.select("age").from("Employee").where().eq("id", id).fetchInt();
            assertEquals(age, selectedAge);

            // UPDATE
            int newAge = new Random().nextInt(Integer.MAX_VALUE);
            int updateResult = sql.update("Employee").set("age", newAge).where(Exp.eq("id", id)).execute();
            assertEquals(1, updateResult);

            // SELECT
            int selectedNewAge = sql.select("age").from("Employee").where().eq("id", id).fetchInt();
            assertEquals(newAge, selectedNewAge);

            // DELETE
            int deletedResult = sql.deleteFrom("Employee").where().not().not().eq("id", id).execute();
            assertEquals(1, deletedResult);

            // SELECT
            int selectedCount = sql.selectAll().from("Employee").where().eq("id", id).fetchRowCount();
            assertEquals(0, selectedCount);

        });

    }

}
