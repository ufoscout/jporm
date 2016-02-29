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
package com.jporm.sql.query.insert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.PostgresDBProfile;
import com.jporm.sql.query.insert.Insert;
import com.jporm.sql.query.insert.values.Generator;

public class InsertTest extends BaseSqlTestApi {

    @Test
    public void testSaveBeanWithUUIDGenerator() {

        final Insert save = dsl().insertInto("EMPLOYEE_WITH_STRING_ID", "ID", "name")
                .values("", "employeeNameValue");

        String renderedSql = save.sqlQuery();
        final String expectedSql = "INSERT INTO EMPLOYEE_WITH_STRING_ID (ID, NAME) VALUES (?, ?) ";
        assertEquals(expectedSql, renderedSql.toUpperCase());

    }

    @Test
    public void testSaveMultiQuerySintaxWithGeneratorsOverride() {

        final Insert save = dsl().insertInto("People", "id", "firstname" );

        save.values(new Object[] { Generator.sequence("SEQ_PEOPLE"), "firstnameValue1" });
        save.values( Generator.sequence("SEQ_PEOPLE"), "firstnameValue2" );

        final String expectedSql = "INSERT INTO People (id, firstname) VALUES (SEQ_PEOPLE.nextval, ?), (SEQ_PEOPLE.nextval, ?) ";
        assertEquals(expectedSql, save.sqlQuery());

        final List<Object> values = new ArrayList<Object>();
        save.sqlValues(values);

        assertEquals(2, values.size());

        assertEquals("firstnameValue1", values.get(0));
        assertEquals("firstnameValue2", values.get(1));

    }

    @Test
    public void testSaveMultiQuerySintaxWithoutGenerators() {

        final Insert save = dsl().insertInto("People", new String[] { "id", "firstname" });

        save.values(new Object[] { "idValue1", "firstnameValue1" });
        save.values( "idValue2", "firstnameValue2" );

        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (?, ?), (?, ?) ";
        assertEquals(expectedSql, save.sqlQuery().toUpperCase());

        final List<Object> values = new ArrayList<Object>();
        save.sqlValues(values);

        assertEquals(4, values.size());

        assertEquals("idValue1", values.get(0));
        assertEquals("firstnameValue1", values.get(1));
        assertEquals("idValue2", values.get(2));
        assertEquals("firstnameValue2", values.get(3));
    }

    @Test
    public void testSaveQuerySintax() {

        final Insert save = dsl().insertInto("Employee", "id", "employeeNumber", "name" );
        save.values("idValue", "employeeNumberValue", null );

        final String expectedSql = "INSERT INTO EMPLOYEE (ID, EMPLOYEENUMBER, NAME) VALUES (?, ?, ?) ";
        assertEquals(expectedSql, save.sqlQuery().toUpperCase());

        final List<Object> values = new ArrayList<Object>();
        save.sqlValues(values);

        assertEquals(3, values.size());

        assertEquals("idValue", values.get(0)); //$NON-NLS-1$
        assertEquals("employeeNumberValue", values.get(1));
        assertNull(values.get(2));

    }

    @Test
    public void testSaveQuerySintaxWithGenerators() {

        final Insert save = dsl(new PostgresDBProfile()).insertInto("People", new String[] { "id", "firstname" });

        save.values(new Object[] { Generator.sequence("SEQ_PEOPLE"), "firstnameValue" });

        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (NEXTVAL(SEQ_PEOPLE), ?) ";
        assertEquals(expectedSql, save.sqlQuery().toUpperCase());

        final List<Object> values = new ArrayList<Object>();
        save.sqlValues(values);

        assertEquals(1, values.size());

        assertEquals("firstnameValue", values.get(0)); //$NON-NLS-1$

    }

    @Test
    public void testSaveQuerySintaxWithoutGenerators() {

        final Insert save = dsl().insertInto("People", new String[] { "id", "firstname" });

        save.values("idValue", "firstnameValue" );

        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (?, ?) ";
        assertEquals(expectedSql, save.sqlQuery().toUpperCase());

        final List<Object> values = new ArrayList<Object>();
        save.sqlValues(values);

        assertEquals(2, values.size());

        assertEquals("idValue", values.get(0));
        assertEquals("firstnameValue", values.get(1));
    }

}
