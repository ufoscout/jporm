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
package com.jporm.rm.kotlin.query.save;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.rm.kotlin.BaseTestApi;

public class CustomSaveQueryImplTest extends BaseTestApi {

    @Test
    public void testSaveQuerySintax() {

        getJPO().tx(session -> {

            final CustomSaveQuery save = session.save(Employee.class, "id", "employeeNumber", "name");

            save.values("idValue", "employeeNumberValue", null);

            System.out.println(save.sqlQuery());
            final String expectedSql = "INSERT INTO EMPLOYEE (ID, EMPLOYEE_NUMBER, NAME) VALUES (?, ?, ?) ";
            assertEquals(expectedSql, save.sqlQuery());

            final List<Object> values = new ArrayList<>();
            save.sqlValues(values);

            assertEquals(3, values.size());

            assertEquals("idValue", values.get(0)); //$NON-NLS-1$
            assertEquals("employeeNumberValue", values.get(1));
            assertNull(values.get(2));
        });

    }

}
