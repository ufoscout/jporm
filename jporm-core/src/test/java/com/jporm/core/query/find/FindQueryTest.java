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
package com.jporm.core.query.find;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.jporm.JPO;
import com.jporm.core.BaseTestApi;
import com.jporm.core.JPOrm;
import com.jporm.core.domain.Blobclob_ByteArray;
import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.core.domain.Zoo_People;
import com.jporm.core.session.NullSessionProvider;
import com.jporm.core.session.SessionProvider;
import com.jporm.exception.OrmException;
import com.jporm.query.find.CustomFindQuery;
import com.jporm.query.find.FindQuery;
import com.jporm.query.find.FindWhere;
import com.jporm.session.Session;

/**
 * 
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class FindQueryTest extends BaseTestApi {

    @Test
    @Ignore
    public void testQueryWithNullParameter() {
        final SessionProvider connectionProvider = new NullSessionProvider();
        final JPO jpOrm = new JPOrm(connectionProvider);
        final Session session =  jpOrm.session();

        FindWhere<Employee> query = session.findQuery(Employee.class, "Employee").where().eq("age", null); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee WHERE Employee.AGE = ? "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());

        List<Object> values = new ArrayList<Object>();
        query.appendValues(values);
        assertTrue( values.size() == 1 );
    }

    @Test
    @Ignore
    public void testQuery1() {
        final SessionProvider connectionProvider = new NullSessionProvider();
        final JPO jpOrm = new JPOrm(connectionProvider);
        jpOrm.register(Employee.class);
        jpOrm.register(Zoo_People.class);

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class, "Employee"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testQuery2() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(Zoo_People.class);

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class, "Employee"); //$NON-NLS-1$
        query.where().eq("Employee.id", 1).ge("Employee.age", 18).in("Employee.name", new Object[]{"frank", "john", "carl"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee WHERE Employee.ID = ? AND Employee.AGE >= ? AND Employee.NAME in ( ?, ?, ? ) "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testQuery2Count() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(Zoo_People.class);

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class);
        query.where().eq("id", 1).ge("age", 18).in("name", new Object[]{"frank", "john", "carl"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        System.out.println(query.renderRowCountSql());
        final String expectedSql = "SELECT COUNT(*) FROM EMPLOYEE Employee WHERE Employee.ID = ? AND Employee.AGE >= ? AND Employee.NAME in ( ?, ?, ? ) "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderRowCountSql());
    }

    @Test
    @Ignore
    public void testQuery3() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(Zoo_People.class);

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class, "Employee"); //$NON-NLS-1$
        query.where().eq("Employee.id", 1).ge("Employee.age", 18); //$NON-NLS-1$ //$NON-NLS-2$
        query.orderBy().asc("id"); //$NON-NLS-1$
        query.orderBy().desc("Employee.age"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee WHERE Employee.ID = ? AND Employee.AGE >= ? ORDER BY Employee.ID ASC , Employee.AGE DESC "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testQuery4() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class, "employeeAlias"); //$NON-NLS-1$
        query.where().eq("employeeAlias.id", 1).ge("age", 18); //$NON-NLS-1$ //$NON-NLS-2$
        query.orderBy().asc("employeeAlias.id"); //$NON-NLS-1$
        query.orderBy().desc("employeeAlias.age"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT employeeAlias.ID AS \"id\", employeeAlias.NAME AS \"name\", employeeAlias.AGE AS \"age\", employeeAlias.SURNAME AS \"surname\", employeeAlias.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE employeeAlias WHERE employeeAlias.ID = ? AND employeeAlias.AGE >= ? ORDER BY employeeAlias.ID ASC , employeeAlias.AGE DESC "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testQuery5() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(Blobclob_ByteArray.class);

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class, "Employee"); //$NON-NLS-1$
        query.join(Blobclob_ByteArray.class);
        query.where().eq("Employee.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("Employee.age", "Blobclob_ByteArray.index"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        query.orderBy().asc("Employee.id"); //$NON-NLS-1$
        query.orderBy().desc("Employee.age"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee , BLOBCLOB Blobclob_ByteArray WHERE Employee.ID = ? AND Blobclob_ByteArray.ID >= ? AND Employee.AGE > Blobclob_ByteArray.ID ORDER BY Employee.ID ASC , Employee.AGE DESC "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testWrongFieldQuery1() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class);
        query.join(Blobclob_ByteArray.class);
        query.where().eq("id", 1).ge("Blobclob_ByteArray.index", 18).gt("ages", 18); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        try {
            query.renderSql();
            fail("field ages is wrong!"); //$NON-NLS-1$
        } catch (OrmException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void testQuery6() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(People.class);
        jpOrm.register(Blobclob_ByteArray.class);

        final Session session =  jpOrm.session();

        final FindQuery<Employee> query = session.findQuery(Employee.class, "e"); //$NON-NLS-1$
        query.innerJoin(People.class, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray.class); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        query.where().gt("e.id", 1); //$NON-NLS-1$
        query.orderBy().asc("e.id"); //$NON-NLS-1$
        query.orderBy().desc("e.age"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT e.ID AS \"id\", e.NAME AS \"name\", e.AGE AS \"age\", e.SURNAME AS \"surname\", e.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e INNER JOIN PEOPLE p ON e.ID = p.FIRSTNAME NATURAL JOIN BLOBCLOB Blobclob_ByteArray WHERE e.ID > ? ORDER BY e.ID ASC , e.AGE DESC "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testCustomQuery1() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(Blobclob_ByteArray.class);

        final Session session =  jpOrm.session();

        final String[] select = {"sum(emp.id, emp.age), count(Blobclob_ByteArray.index), emp.employeeNumber"}; //$NON-NLS-1$
        final CustomFindQuery query = session.findQuery(select, Employee.class, "emp").distinct(true); //$NON-NLS-1$
        query.join(Blobclob_ByteArray.class);
        query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("emp.age", "Blobclob_ByteArray.index"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        query.orderBy().asc("id"); //$NON-NLS-1$
        query.orderBy().desc("emp.age"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE), count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ? AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC , emp.AGE DESC "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testCustomQuery2() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(Blobclob_ByteArray.class);

        final Session session =  jpOrm.session();

        final String[] select = {"sum(emp.id, emp.age)", "count(Blobclob_ByteArray.index)", "emp.employeeNumber"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        final CustomFindQuery query = session.findQuery(select, Employee.class, "emp").distinct(true); //$NON-NLS-1$
        query.join(Blobclob_ByteArray.class);
        query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("emp.age", "Blobclob_ByteArray.index"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        query.orderBy().asc("id"); //$NON-NLS-1$
        query.orderBy().desc("emp.employeeNumber"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE), count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ? AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC , emp.EMPLOYEE_NUMBER DESC "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    @Ignore
    public void testCustomQuery3() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(Blobclob_ByteArray.class);

        final Session session =  jpOrm.session();

        final String[] select = {"sum(emp.id, emp.age)", "emp.age, count(Blobclob_ByteArray.index) , emp.employeeNumber"}; //$NON-NLS-1$ //$NON-NLS-2$
        final CustomFindQuery query = session.findQuery(select, Employee.class, "emp").distinct(true); //$NON-NLS-1$
        query.join(Blobclob_ByteArray.class);
        query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("emp.age", "Blobclob_ByteArray.index"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        query.orderBy().asc("id"); //$NON-NLS-1$
        query.orderBy().desc("emp.employeeNumber"); //$NON-NLS-1$
        System.out.println(query.renderSql());
        final String expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE), emp.AGE AS \"emp.age\", count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ? AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC , emp.EMPLOYEE_NUMBER DESC "; //$NON-NLS-1$
        assertEquals(expectedSql , query.renderSql());
    }

    @Test
    public void testSubQuery1() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        jpOrm.register(Employee.class);
        jpOrm.register(People.class);
        jpOrm.register(Blobclob_ByteArray.class);

        final Session session =  jpOrm.session();

        final CustomFindQuery subQuery1 = session.findQuery(new String[]{"Employee.id as hello", "People.lastname"}, Employee.class, "Employee"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        subQuery1.join(People.class);
        subQuery1.where().geProperties("Employee.id", "People.id"); //$NON-NLS-1$ //$NON-NLS-2$
        subQuery1.orderBy().asc("People.lastname"); //$NON-NLS-1$
        final FindQuery<People> subQuery2 = session.findQuery(People.class, "people"); //$NON-NLS-1$
        subQuery2.where().eq("people.firstname", "wizard"); //$NON-NLS-1$ //$NON-NLS-2$

        final FindQuery<Employee> query = session.findQuery(Employee.class, "e"); //$NON-NLS-1$
        query.innerJoin(People.class, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray.class); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        query.where().in("e.age", subQuery1); //$NON-NLS-1$
        query.where().nin("p.firstname", subQuery2); //$NON-NLS-1$

        System.out.println(query.renderSql());
//        final String expectedSql = "SELECT e_0.ID AS \"id\", e_0.NAME AS \"name\", e_0.AGE AS \"age\", e_0.SURNAME AS \"surname\", e_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e_0 INNER JOIN PEOPLE p_1 ON e_0.ID = p_1.FIRSTNAME NATURAL JOIN BLOBCLOB Blobclob_ByteArray_2 WHERE e_0.AGE IN ( "
//                + "SELECT Employee_0.ID as hello, People_1.LASTNAME AS \"People.lastname\" FROM EMPLOYEE Employee_0 , PEOPLE People_1 WHERE Employee_0.ID >= People_1.ID ORDER BY People_1.LASTNAME ASC ) AND p_1.FIRSTNAME NOT IN ( SELECT people_0.FIRSTCLOB AS \"firstclob\", people_0.ID AS \"id\", people_0.SECONDBLOB AS \"secondblob\", people_0.BIRTHDATE AS \"birthdate\", people_0.LASTNAME AS \"lastname\", people_0.FIRSTNAME AS \"firstname\", people_0.FIRSTBLOB AS \"firstblob\", people_0.DEATHDATE AS \"deathdate\" FROM PEOPLE people_0 WHERE people_0.FIRSTNAME = ? ) "; //$NON-NLS-1$
        //assertEquals(expectedSql , query.renderSql());

        assertTrue(query.renderSql().contains("SELECT"));
        assertTrue(query.renderSql().contains(" e_0.ID AS \"id\""));
        assertTrue(query.renderSql().contains(" e_0.NAME AS \"name\""));
        assertTrue(query.renderSql().contains(" e_0.AGE AS \"age\""));
        assertTrue(query.renderSql().contains(" e_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql().contains(" e_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql().contains(" FROM EMPLOYEE e_0 INNER JOIN PEOPLE p_1 ON e_0.ID = p_1.FIRSTNAME NATURAL JOIN BLOBCLOB Blobclob_ByteArray_2"));
        assertTrue(query.renderSql().contains(" WHERE e_0.AGE IN ( SELECT "));
        assertTrue(query.renderSql().contains(" Employee_0.ID as hello"));
        assertTrue(query.renderSql().contains(" People_1.LASTNAME AS \"People.lastname\""));
        assertTrue(query.renderSql().contains(" FROM EMPLOYEE Employee_0 , PEOPLE People_1 WHERE Employee_0.ID >= People_1.ID ORDER BY People_1.LASTNAME ASC ) AND p_1.FIRSTNAME NOT IN "));
        assertTrue(query.renderSql().contains(" AND p_1.FIRSTNAME NOT IN ( SELECT"));
        
        final List<Object> values = new ArrayList<Object>();
        query.appendValues(values);
        assertTrue( values.size() == 1 );
        assertEquals( "wizard" , values.get(0) ); //$NON-NLS-1$

    }

    @Test
    public void testOnlineSqlWriting() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());
        final Session session =  jpOrm.session();

        // METHOD ONE
        final CustomFindQuery subQuery1 = session.findQuery(new String[]{"Employee.id as hello","People.lastname"}, Employee.class, "Employee"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        subQuery1.join(People.class);
        subQuery1.where().geProperties("Employee.id", "People.id"); //$NON-NLS-1$ //$NON-NLS-2$
        subQuery1.orderBy().asc("People.lastname"); //$NON-NLS-1$

        final FindQuery<People> subQuery2 = session.findQuery(People.class, "people"); //$NON-NLS-1$
        subQuery2.where().eq("people.firstname", "wizard"); //$NON-NLS-1$ //$NON-NLS-2$

        final FindQuery<Employee> query = session.findQuery(Employee.class, "e"); //$NON-NLS-1$
        query.innerJoin(People.class, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray.class); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        query.where().in("e.age", subQuery1); //$NON-NLS-1$
        query.where().nin("p.firstname", subQuery2); //$NON-NLS-1$

        final String methodOneRendering = query.renderSql();


        // SAME QUERY WITH OLD ONLINE WRITING
        final String oldOnlineMethodWriting = session.findQuery(Employee.class, "e") //$NON-NLS-1$
                .innerJoin(People.class, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray.class) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                .where().in("e.age", //$NON-NLS-1$
                        session.findQuery(new String[]{"Employee.id as hello","People.lastname"}, Employee.class, "Employee") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        .join(People.class)
                        .where().geProperties("Employee.id", "People.id") //$NON-NLS-1$ //$NON-NLS-2$
                        .query().orderBy().asc("People.lastname").query() //$NON-NLS-1$
                        )
                        .query().where().nin("p.firstname", //$NON-NLS-1$
                                session.findQuery(People.class, "people").where().eq("people.firstname", "wizard").query() //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                )
                                .query().renderSql();

        System.out.println("Method one query        : " + methodOneRendering); //$NON-NLS-1$
        System.out.println("old online writing query: " + oldOnlineMethodWriting); //$NON-NLS-1$

        assertEquals(methodOneRendering, oldOnlineMethodWriting);

        // SAME QUERY WITH ONLINE WRITING
        final String onlineMethodWriting = session.findQuery(Employee.class, "e") //$NON-NLS-1$
                .innerJoin(People.class, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray.class) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                .where().in("e.age", //$NON-NLS-1$
                        session.findQuery(new String[]{"Employee.id as hello","People.lastname"}, Employee.class, "Employee") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        .join(People.class)
                        .where().geProperties("Employee.id", "People.id") //$NON-NLS-1$ //$NON-NLS-2$
                        .orderBy().asc("People.lastname") //$NON-NLS-1$
                        )
                        .nin("p.firstname", //$NON-NLS-1$
                                session.findQuery(People.class, "people").where().eq("people.firstname", "wizard") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                )
                                .renderSql();

        System.out.println("Method one query    : " + methodOneRendering); //$NON-NLS-1$
        System.out.println("online writing query: " + onlineMethodWriting); //$NON-NLS-1$

        assertEquals(methodOneRendering, onlineMethodWriting);

    }

    @Test
    public void testSameTableJoinQuery1() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());

        final Session session =  jpOrm.session();

        FindQuery<Employee> query = session.findQuery(Employee.class, "e1").innerJoin(Employee.class, "e2", "e1.name", "e2.name"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        System.out.println(query.renderSql());
        //final String expectedSql = "SELECT e1_0.ID AS \"id\", e1_0.NAME AS \"name\", e1_0.AGE AS \"age\", e1_0.SURNAME AS \"surname\", e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME "; //$NON-NLS-1$
        //assertEquals(expectedSql , query.renderSql());
        
        assertTrue(query.renderSql().contains("SELECT "));
        assertTrue(query.renderSql().contains(" e1_0.ID AS \"id\""));
        assertTrue(query.renderSql().contains(" e1_0.NAME AS \"name\""));
        assertTrue(query.renderSql().contains(" e1_0.AGE AS \"age\""));
        assertTrue(query.renderSql().contains(" e1_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql().contains(" e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql().contains(" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME "));
    }

    @Test
    public void testSameTableJoinQueryThreeTimes() {
        final JPO jpOrm = new JPOrm(new NullSessionProvider());

        final Session session =  jpOrm.session();

        FindQuery<Employee> query = session.findQuery(Employee.class, "e1").innerJoin(Employee.class, "e2", "e1.name", "e2.name").innerJoin(Employee.class, "e3", "e1.surname", "e3.name"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
        System.out.println(query.renderSql());
        //final String expectedSql = "SELECT e1_0.ID AS \"id\", e1_0.NAME AS \"name\", e1_0.AGE AS \"age\", e1_0.SURNAME AS \"surname\", e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME INNER JOIN EMPLOYEE e3_2 ON e1_0.SURNAME = e3_2.NAME "; //$NON-NLS-1$
        
        assertTrue(query.renderSql().contains("SELECT"));
        assertTrue(query.renderSql().contains(" e1_0.ID AS \"id\""));
        assertTrue(query.renderSql().contains(" e1_0.NAME AS \"name\""));
        assertTrue(query.renderSql().contains(" e1_0.AGE AS \"age\""));
        assertTrue(query.renderSql().contains(" e1_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql().contains(" e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql().contains(" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME INNER JOIN EMPLOYEE e3_2 ON e1_0.SURNAME = e3_2.NAME "));
        
        
    }

    @Test
    public void testCustomExpressionQuery() {
        final SessionProvider connectionProvider = new NullSessionProvider();
        final JPO jpOrm = new JPOrm(connectionProvider);
        final Session session =  jpOrm.session();

        FindWhere<Employee> query = session.findQuery(Employee.class, "Employee").where("mod(Employee.id, 10) = 1");  //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println(query.renderSql());
//        final String expectedSql = "SELECT Employee_0.ID AS \"id\", Employee_0.NAME AS \"name\", Employee_0.AGE AS \"age\", Employee_0.SURNAME AS \"surname\", Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee_0 WHERE mod(Employee_0.ID, 10) = 1 ";
//        assertEquals(expectedSql , query.renderSql());

        assertTrue(query.renderSql().contains("SELECT"));
        assertTrue(query.renderSql().contains(" Employee_0.ID AS \"id\""));
        assertTrue(query.renderSql().contains(" Employee_0.NAME AS \"name\""));
        assertTrue(query.renderSql().contains(" Employee_0.AGE AS \"age\""));
        assertTrue(query.renderSql().contains(" Employee_0.SURNAME AS \"surname\""));
        assertTrue(query.renderSql().contains(" Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\""));
        assertTrue(query.renderSql().contains(" FROM EMPLOYEE Employee_0 WHERE mod(Employee_0.ID, 10) = 1 "));
    }

}
