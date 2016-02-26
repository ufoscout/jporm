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
package com.jporm.sql.query.clause;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dsl.dialect.H2DBProfile;
import com.jporm.sql.query.clause.impl.InsertImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.test.domain.section01.EmployeeWithStringId;

public class InsertTest extends BaseSqlTestApi {

    @Test
    public void testSaveBeanWithUUIDGenerator() {

        final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), EmployeeWithStringId.class, new String[] { "name" });
        save.values(new String[] { "employeeNameValue" });

        String renderedSql = save.renderSql(new H2DBProfile());
        System.out.println(renderedSql);
        final String expectedSql = "INSERT INTO EMPLOYEE_WITH_STRING_ID (ID, NAME) VALUES (?, ?) ";
        assertEquals(expectedSql, renderedSql);

    }

    @Test
    public void testSaveMultiQuerySintaxWithGeneratorsOverride() {

        final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class, new String[] { "firstname" });

        save.values(new String[] { "firstnameValue1" });
        save.values(new String[] { "firstnameValue2" });

        System.out.println(save.renderSql(new H2DBProfile()));
        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (SEQ_PEOPLE.nextval, ?), (SEQ_PEOPLE.nextval, ?) ";
        assertEquals(expectedSql, save.renderSql(new H2DBProfile()));

        final List<Object> values = new ArrayList<Object>();
        save.appendValues(values);

        assertEquals(2, values.size());

        assertEquals("firstnameValue1", values.get(0));
        assertEquals("firstnameValue2", values.get(1));

    }

    @Test
    public void testSaveMultiQuerySintaxWithoutGenerators() {

        final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class, new String[] { "id", "firstname" });

        save.values(new String[] { "idValue1", "firstnameValue1" });
        save.values(new String[] { "idValue2", "firstnameValue2" });
        save.useGenerators(false);

        System.out.println(save.renderSql(new H2DBProfile()));
        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (?, ?), (?, ?) ";
        assertEquals(expectedSql, save.renderSql(new H2DBProfile()));

        final List<Object> values = new ArrayList<Object>();
        save.appendValues(values);

        assertEquals(4, values.size());

        assertEquals("idValue1", values.get(0));
        assertEquals("firstnameValue1", values.get(1));
        assertEquals("idValue2", values.get(2));
        assertEquals("firstnameValue2", values.get(3));
    }

    @Test
    public void testSaveQuerySintax() {

        final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, new String[] { "id", "employeeNumber", "name" });
        save.values(new String[] { "idValue", "employeeNumberValue", null });

        System.out.println(save.renderSql(new H2DBProfile()));
        final String expectedSql = "INSERT INTO EMPLOYEE (ID, EMPLOYEE_NUMBER, NAME) VALUES (?, ?, ?) ";
        assertEquals(expectedSql, save.renderSql(new H2DBProfile()));

        final List<Object> values = new ArrayList<Object>();
        save.appendValues(values);

        assertEquals(3, values.size());

        assertEquals("idValue", values.get(0)); //$NON-NLS-1$
        assertEquals("employeeNumberValue", values.get(1));
        assertNull(values.get(2));

    }

    @Test
    public void testSaveQuerySintaxWithGenerators() {

        final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class, new String[] { "firstname" });

        save.values(new String[] { "firstnameValue" });

        System.out.println(save.renderSql(new H2DBProfile()));
        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (SEQ_PEOPLE.nextval, ?) ";
        assertEquals(expectedSql, save.renderSql(new H2DBProfile()));

        final List<Object> values = new ArrayList<Object>();
        save.appendValues(values);

        assertEquals(1, values.size());

        assertEquals("firstnameValue", values.get(0)); //$NON-NLS-1$

    }

    @Test
    public void testSaveQuerySintaxWithGeneratorsOverride() {

        final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class, new String[] { "firstname" });

        save.values(new String[] { "firstnameValue" });

        System.out.println(save.renderSql(new H2DBProfile()));
        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (SEQ_PEOPLE.nextval, ?) ";
        assertEquals(expectedSql, save.renderSql(new H2DBProfile()));

        final List<Object> values = new ArrayList<Object>();
        save.appendValues(values);

        assertEquals(1, values.size());

        assertEquals("firstnameValue", values.get(0)); //$NON-NLS-1$

    }

    @Test
    public void testSaveQuerySintaxWithoutGenerators() {

        final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class, new String[] { "id", "firstname" });

        save.values(new String[] { "idValue", "firstnameValue" });
        save.useGenerators(false);

        System.out.println(save.renderSql(new H2DBProfile()));
        final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (?, ?) ";
        assertEquals(expectedSql, save.renderSql(new H2DBProfile()));

        final List<Object> values = new ArrayList<Object>();
        save.appendValues(values);

        assertEquals(2, values.size());

        assertEquals("idValue", values.get(0));
        assertEquals("firstnameValue", values.get(1));
    }

}
