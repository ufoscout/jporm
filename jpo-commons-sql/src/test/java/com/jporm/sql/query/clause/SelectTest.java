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
package com.jporm.sql.query.clause;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.jporm.core.domain.Blobclob_ByteArray;
import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.H2DBProfile;
import com.jporm.sql.query.clause.impl.SelectImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;

/**
 *
 * @author Francesco Cina
 *
 *         23/giu/2011
 */
public class SelectTest extends BaseSqlTestApi {

    private <BEAN> String[] getBeanFields(final Class<BEAN> clazz) {
        return getClassDescriptor(clazz).getAllColumnJavaNames();
    }

    @Test
    public void testCustomExpressionQuery() {
        Select query = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "Employee");
        query.selectFields(getBeanFields(Employee.class));

        query.where("mod(Employee.id, 10) = 1"); //$NON-NLS-1$
        System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT Employee_0.ID AS \"id\",
        // Employee_0.NAME AS \"name\", Employee_0.AGE AS \"age\",
        // Employee_0.SURNAME AS \"surname\", Employee_0.EMPLOYEE_NUMBER AS
        // \"employeeNumber\" FROM EMPLOYEE Employee_0 WHERE mod(Employee_0.ID,
        // 10) = 1 ";
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));

        assertTrue(query.renderSql(new H2DBProfile()).contains("SELECT"));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" Employee_0.ID AS \"id\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" Employee_0.NAME AS \"name\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" Employee_0.AGE AS \"age\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" Employee_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" FROM EMPLOYEE Employee_0 WHERE mod(Employee_0.ID, 10) = 1 "));
    }

    @Test
    @Ignore
    public void testCustomQuery1() {
        // final JPO jpOrm = new JPOrm(new NullSessionProvider());
        // jpOrm.register(Employee.class);
        // jpOrm.register(Blobclob_ByteArray.class);
        //
        // final Session session = jpOrm.session();
        //
        // final String[] select = {"sum(emp.id, emp.age),
        // count(Blobclob_ByteArray.index), emp.employeeNumber"}; //$NON-NLS-1$
        // final CustomFindQuery query = session.findQuery(select,
        // Employee.class, "emp").distinct(true); //$NON-NLS-1$
        // query.join(Blobclob_ByteArray.class);
        // query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index",
        // 18).gtProperties("emp.age", "Blobclob_ByteArray.index");
        // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        // query.orderBy().asc("id"); //$NON-NLS-1$
        // query.orderBy().desc("emp.age"); //$NON-NLS-1$
        // System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE),
        // count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS
        // \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB
        // Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ?
        // AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC , emp.AGE
        // DESC "; //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));
    }

    @Test
    @Ignore
    public void testCustomQuery2() {
        // final JPO jpOrm = new JPOrm(new NullSessionProvider());
        // jpOrm.register(Employee.class);
        // jpOrm.register(Blobclob_ByteArray.class);
        //
        // final Session session = jpOrm.session();
        //
        // final String[] select = {"sum(emp.id, emp.age)",
        // "count(Blobclob_ByteArray.index)", "emp.employeeNumber"};
        // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        // final CustomFindQuery query = session.findQuery(select,
        // Employee.class, "emp").distinct(true); //$NON-NLS-1$
        // query.join(Blobclob_ByteArray.class);
        // query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index",
        // 18).gtProperties("emp.age", "Blobclob_ByteArray.index");
        // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        // query.orderBy().asc("id"); //$NON-NLS-1$
        // query.orderBy().desc("emp.employeeNumber"); //$NON-NLS-1$
        // System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE),
        // count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS
        // \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB
        // Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ?
        // AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC ,
        // emp.EMPLOYEE_NUMBER DESC "; //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));
    }

    @Test
    @Ignore
    public void testCustomQuery3() {
        // final JPO jpOrm = new JPOrm(new NullSessionProvider());
        // jpOrm.register(Employee.class);
        // jpOrm.register(Blobclob_ByteArray.class);
        //
        // final Session session = jpOrm.session();
        //
        // final String[] select = {"sum(emp.id, emp.age)", "emp.age,
        // count(Blobclob_ByteArray.index) , emp.employeeNumber"}; //$NON-NLS-1$
        // //$NON-NLS-2$
        // final CustomFindQuery query = session.findQuery(select,
        // Employee.class, "emp").distinct(true); //$NON-NLS-1$
        // query.join(Blobclob_ByteArray.class);
        // query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index",
        // 18).gtProperties("emp.age", "Blobclob_ByteArray.index");
        // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        // query.orderBy().asc("id"); //$NON-NLS-1$
        // query.orderBy().desc("emp.employeeNumber"); //$NON-NLS-1$
        // System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE),
        // emp.AGE AS \"emp.age\", count(Blobclob_ByteArray.ID),
        // emp.EMPLOYEE_NUMBER AS \"emp.employeeNumber\" FROM EMPLOYEE emp ,
        // BLOBCLOB Blobclob_ByteArray WHERE emp.ID = ? AND
        // Blobclob_ByteArray.ID >= ? AND emp.AGE > Blobclob_ByteArray.ID ORDER
        // BY emp.ID ASC , emp.EMPLOYEE_NUMBER DESC "; //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));
    }

    @Test
    public void testQuery1() {
        Select select = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
        select.selectFields("id", "name", "age", "surname", "employeeNumber");
        System.out.println(select.renderSql(new H2DBProfile()));
        final String expectedSql = "SELECT Employee_0.ID AS \"id\", Employee_0.NAME AS \"name\", Employee_0.AGE AS \"age\", Employee_0.SURNAME AS \"surname\", Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee_0 "; //$NON-NLS-1$
        assertEquals(expectedSql, select.renderSql(new H2DBProfile()));
    }

    @Test
    public void testQuery2() {
        Select select = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
        select.selectFields(getBeanFields(Employee.class));
        select.where().eq("Employee.id", 1).ge("age", 18).in("Employee.name", new Object[] { "frank", "john", "carl" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        System.out.println(select.renderSql(new H2DBProfile()));
        final String expectedSql = "SELECT Employee_0.SURNAME AS \"surname\", Employee_0.NAME AS \"name\", Employee_0.ID AS \"id\", Employee_0.AGE AS \"age\", Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee_0 WHERE Employee_0.ID = ? AND Employee_0.AGE >= ? AND Employee_0.NAME in ( ?, ?, ? ) ";
        assertEquals(expectedSql, select.renderSql(new H2DBProfile()));
    }

    @Test
    public void testQuery2Count() {
        Select select = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
        select.selectFields(getBeanFields(Employee.class));
        select.where().eq("id", 1).ge("age", 18).in("name", new Object[] { "frank", "john", "carl" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        System.out.println(select.renderRowCountSql(new H2DBProfile()));
        final String expectedSql = "SELECT COUNT(*) FROM ( SELECT Employee_0.SURNAME AS \"surname\", Employee_0.NAME AS \"name\", Employee_0.ID AS \"id\", Employee_0.AGE AS \"age\", Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee_0 WHERE Employee_0.ID = ? AND Employee_0.AGE >= ? AND Employee_0.NAME in ( ?, ?, ? ) ) a ";
        assertEquals(expectedSql, select.renderRowCountSql(new H2DBProfile()));
    }

    @Test
    public void testQuery3() {
        Select query = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
        query.selectFields(getBeanFields(Employee.class));
        query.where().eq("Employee.id", 1).ge("Employee.age", 18); //$NON-NLS-1$ //$NON-NLS-2$
        query.orderBy().asc("id"); //$NON-NLS-1$
        query.orderBy().desc("Employee.age"); //$NON-NLS-1$
        System.out.println(query.renderSql(new H2DBProfile()));
        final String expectedSql = "SELECT Employee_0.SURNAME AS \"surname\", Employee_0.NAME AS \"name\", Employee_0.ID AS \"id\", Employee_0.AGE AS \"age\", Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee_0 WHERE Employee_0.ID = ? AND Employee_0.AGE >= ? ORDER BY Employee_0.ID ASC , Employee_0.AGE DESC ";
        assertEquals(expectedSql, query.renderSql(new H2DBProfile()));
    }

    @Test
    public void testQuery4() {
        // Select query = new SelectImpl<>(getClassDescriptorMap(), new
        // PropertiesFactory(), Employee.class, "employeeAlias");
        // query.selectFields(getBeanFields(Employee.class));
        //
        // query.where().eq("employeeAlias.id", 1).ge("age", 18);
        // query.orderBy().asc("employeeAlias.id");
        // query.orderBy().desc("employeeAlias.age");
        // System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT employeeAlias.ID AS \"id\",
        // employeeAlias.NAME AS \"name\", employeeAlias.AGE AS \"age\",
        // employeeAlias.SURNAME AS \"surname\", employeeAlias.EMPLOYEE_NUMBER
        // AS \"employeeNumber\" FROM EMPLOYEE employeeAlias WHERE
        // employeeAlias.ID = ? AND employeeAlias.AGE >= ? ORDER BY
        // employeeAlias.ID ASC , employeeAlias.AGE DESC "; //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));
    }

    @Test
    @Ignore
    public void testQuery5() {
        // final JPO jpOrm = new JPOrm(new NullSessionProvider());
        // jpOrm.register(Employee.class);
        // jpOrm.register(Blobclob_ByteArray.class);
        //
        // final Session session = jpOrm.session();
        //
        // final FindQuery<Employee> query = session.findQuery(Employee.class,
        // "Employee"); //$NON-NLS-1$
        // query.join(Blobclob_ByteArray.class);
        // query.where().eq("Employee.id", 1).ge("Blobclob_ByteArray.index",
        // 18).gtProperties("Employee.age", "Blobclob_ByteArray.index");
        // //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        // query.orderBy().asc("Employee.id"); //$NON-NLS-1$
        // query.orderBy().desc("Employee.age"); //$NON-NLS-1$
        // System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT Employee.ID AS \"id\",
        // Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME
        // AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM
        // EMPLOYEE Employee , BLOBCLOB Blobclob_ByteArray WHERE Employee.ID = ?
        // AND Blobclob_ByteArray.ID >= ? AND Employee.AGE >
        // Blobclob_ByteArray.ID ORDER BY Employee.ID ASC , Employee.AGE DESC ";
        // //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));
    }

    @Test
    @Ignore
    public void testQuery6() {
        // final JPO jpOrm = new JPOrm(new NullSessionProvider());
        // jpOrm.register(Employee.class);
        // jpOrm.register(People.class);
        // jpOrm.register(Blobclob_ByteArray.class);
        //
        // final Session session = jpOrm.session();
        //
        // final FindQuery<Employee> query = session.findQuery(Employee.class,
        // "e"); //$NON-NLS-1$
        // query.innerJoin(People.class, "p", "e.id",
        // "p.firstname").naturalJoin(Blobclob_ByteArray.class); //$NON-NLS-1$
        // //$NON-NLS-2$ //$NON-NLS-3$
        // query.where().gt("e.id", 1); //$NON-NLS-1$
        // query.orderBy().asc("e.id"); //$NON-NLS-1$
        // query.orderBy().desc("e.age"); //$NON-NLS-1$
        // System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT e.ID AS \"id\", e.NAME AS
        // \"name\", e.AGE AS \"age\", e.SURNAME AS \"surname\",
        // e.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e INNER JOIN
        // PEOPLE p ON e.ID = p.FIRSTNAME NATURAL JOIN BLOBCLOB
        // Blobclob_ByteArray WHERE e.ID > ? ORDER BY e.ID ASC , e.AGE DESC ";
        // //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));
    }

    @Test
    public void testQueryWithNullParameter() {
        Select select = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);

        select.selectFields(getBeanFields(Employee.class));
        select.where().eq("age", null); //$NON-NLS-1$
        System.out.println(select.renderSql(new H2DBProfile()));
        final String expectedSql = "SELECT Employee_0.SURNAME AS \"surname\", Employee_0.NAME AS \"name\", Employee_0.ID AS \"id\", Employee_0.AGE AS \"age\", Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee_0 WHERE Employee_0.AGE = ? ";
        assertEquals(expectedSql, select.renderSql(new H2DBProfile()));

        List<Object> values = new ArrayList<Object>();
        select.appendValues(values);
        assertTrue(values.size() == 1);
    }

    @Test
    public void testSameTableJoinQuery1() {
        Select query = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "e1");
        query.selectFields(getBeanFields(Employee.class));
        query.from().innerJoin(Employee.class, "e2", "e1.name", "e2.name");

        System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT e1_0.ID AS \"id\", e1_0.NAME AS
        // \"name\", e1_0.AGE AS \"age\", e1_0.SURNAME AS \"surname\",
        // e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e1_0 INNER
        // JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME "; //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));

        assertTrue(query.renderSql(new H2DBProfile()).contains("SELECT "));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.ID AS \"id\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.NAME AS \"name\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.AGE AS \"age\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME "));
    }

    @Test
    public void testSameTableJoinQueryThreeTimes() {
        Select query = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "e1");
        query.selectFields(getBeanFields(Employee.class));

        query.from().innerJoin(Employee.class, "e2", "e1.name", "e2.name").innerJoin(Employee.class, "e3", "e1.surname", "e3.name"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT e1_0.ID AS \"id\", e1_0.NAME AS
        // \"name\", e1_0.AGE AS \"age\", e1_0.SURNAME AS \"surname\",
        // e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e1_0 INNER
        // JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME INNER JOIN EMPLOYEE e3_2
        // ON e1_0.SURNAME = e3_2.NAME "; //$NON-NLS-1$

        assertTrue(query.renderSql(new H2DBProfile()).contains("SELECT"));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.ID AS \"id\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.NAME AS \"name\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.AGE AS \"age\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql(new H2DBProfile())
                .contains(" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME INNER JOIN EMPLOYEE e3_2 ON e1_0.SURNAME = e3_2.NAME "));

    }

    @Test
    public void testSubQuery1() {

        Select subQuery1 = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
        subQuery1.selectFields(new String[] { "Employee.id as hello", "People.lastname" });
        subQuery1.from().join(People.class);
        subQuery1.where().geProperties("Employee.id", "People.id"); //$NON-NLS-1$ //$NON-NLS-2$
        subQuery1.orderBy().asc("People.lastname"); //$NON-NLS-1$

        Select subQuery2 = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class, "people");
        subQuery2.selectFields(getBeanFields(People.class));
        subQuery2.where().eq("people.firstname", "wizard"); //$NON-NLS-1$ //$NON-NLS-2$

        Select query = new SelectImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class, "e");
        query.selectFields(getBeanFields(Employee.class));
        query.from().innerJoin(People.class, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray.class);
        query.where().in("e.age", subQuery1); //$NON-NLS-1$
        query.where().nin("p.firstname", subQuery2); //$NON-NLS-1$

        System.out.println(query.renderSql(new H2DBProfile()));
        // final String expectedSql = "SELECT e_0.ID AS \"id\", e_0.NAME AS
        // \"name\", e_0.AGE AS \"age\", e_0.SURNAME AS \"surname\",
        // e_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e_0 INNER
        // JOIN PEOPLE p_1 ON e_0.ID = p_1.FIRSTNAME NATURAL JOIN BLOBCLOB
        // Blobclob_ByteArray_2 WHERE e_0.AGE IN ( "
        // + "SELECT Employee_0.ID as hello, People_1.LASTNAME AS
        // \"People.lastname\" FROM EMPLOYEE Employee_0 , PEOPLE People_1 WHERE
        // Employee_0.ID >= People_1.ID ORDER BY People_1.LASTNAME ASC ) AND
        // p_1.FIRSTNAME NOT IN ( SELECT people_0.FIRSTCLOB AS \"firstclob\",
        // people_0.ID AS \"id\", people_0.SECONDBLOB AS \"secondblob\",
        // people_0.BIRTHDATE AS \"birthdate\", people_0.LASTNAME AS
        // \"lastname\", people_0.FIRSTNAME AS \"firstname\", people_0.FIRSTBLOB
        // AS \"firstblob\", people_0.DEATHDATE AS \"deathdate\" FROM PEOPLE
        // people_0 WHERE people_0.FIRSTNAME = ? ) "; //$NON-NLS-1$
        // assertEquals(expectedSql , query.renderSql(new H2DBProfile()));

        assertTrue(query.renderSql(new H2DBProfile()).contains("SELECT"));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e_0.ID AS \"id\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e_0.NAME AS \"name\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e_0.AGE AS \"age\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" e_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql(new H2DBProfile())
                .contains(" FROM EMPLOYEE e_0 INNER JOIN PEOPLE p_1 ON e_0.ID = p_1.FIRSTNAME NATURAL JOIN BLOBCLOB Blobclob_ByteArray_2"));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" WHERE e_0.AGE IN ( SELECT "));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" Employee_0.ID as hello"));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" People_1.LASTNAME AS \"People.lastname\""));
        assertTrue(query.renderSql(new H2DBProfile()).contains(
                " FROM EMPLOYEE Employee_0 , PEOPLE People_1 WHERE Employee_0.ID >= People_1.ID ORDER BY People_1.LASTNAME ASC ) AND p_1.FIRSTNAME NOT IN "));
        assertTrue(query.renderSql(new H2DBProfile()).contains(" AND p_1.FIRSTNAME NOT IN ( SELECT"));

        final List<Object> values = new ArrayList<Object>();
        query.appendValues(values);
        assertTrue(values.size() == 1);
        assertEquals("wizard", values.get(0)); //$NON-NLS-1$

    }

    @Test
    @Ignore
    public void testWrongFieldQuery1() {
        // final JPO jpOrm = new JPOrm(new NullSessionProvider());
        //
        // final Session session = jpOrm.session();
        //
        // final FindQuery<Employee> query = session.findQuery(Employee.class);
        // query.join(Blobclob_ByteArray.class);
        // query.where().eq("id", 1).ge("Blobclob_ByteArray.index",
        // 18).gt("ages", 18); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        // try {
        // query.renderSql(new H2DBProfile());
        // fail("field ages is wrong!"); //$NON-NLS-1$
        // } catch (JpoException e) {
        // e.printStackTrace();
        // }
    }
}
