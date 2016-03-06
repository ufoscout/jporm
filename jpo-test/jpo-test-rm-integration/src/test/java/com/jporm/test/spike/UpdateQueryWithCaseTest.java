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
package com.jporm.test.spike;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;

/**
 *
 * @author cinafr
 *
 */
public class UpdateQueryWithCaseTest extends BaseTestAllDB {

    public UpdateQueryWithCaseTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testIntegerNewRecordVersion() {
        getJPO().transaction().execute((session) -> {

            String updateWithCase =
                    "UPDATE EMPLOYEE " +
                       "SET AGE = CASE ID " +
                                          "WHEN 1 THEN 2 " +
                                          "WHEN 2 THEN 3 " +
                                          "ELSE ID "+
                                          "END " +
                      "WHERE ID IN(1, 2)";

            int result = session.sqlExecutor().update(updateWithCase);
            assertTrue(result>=0);

        });
    }

}
