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

import java.util.Random;

import org.junit.Test;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;

/**
 *
 * @author Francesco Cina
 *
 *         05/giu/2011
 */
public class CustomSaveWithMultipleValuesTest extends BaseTestAllDB {

    public CustomSaveWithMultipleValuesTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testCustomQueryWithMoreFields() {

        getJPO().transaction().execute(session -> {
            Integer id1 = new Random().nextInt(1000000) + 100000;
            Integer id2 = new Random().nextInt(1000000) + 100000;
            int created = session.save(Employee.class, "id", "age")
                    .values(id1, 10)
                    .values(id2, 10)
                    .execute();
            assertEquals(2, created);

            assertEquals(2, session.find(Employee.class).where("id in (?,?)", id1, id2).fetchRowCount());
        });
    }

}
