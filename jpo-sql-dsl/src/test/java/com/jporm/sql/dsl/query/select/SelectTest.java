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
package com.jporm.sql.dsl.query.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jporm.sql.dsl.BaseSqlTestApi;

/**
 *
 * @author Francesco Cina
 *
 *         07/lug/2011
 */
public class SelectTest extends BaseSqlTestApi {

    @Test
    public void testLimitOffset() {
        Select<String> select = dsl().select("emp.name").from("Employee", "emp");
        select.limit(10);
        select.offset(5);
        String sql = select.sqlQuery();
        getLogger().info(sql);
    }

    @Test
    public void testSelectRender1() {
        // SelectImpl<Employee> select = new SelectImpl<Employee>(new
        // H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(),
        // Employee.class);
        //
        // final String[] selectClause = {"Employee.id as hello,
        // sum(Employee.id, Blobclob_ByteArray.index, nada.nada) as sum,
        // Beppe.Signori.Goal"};
        // // final String[] selectClause = {"Employee.id as hello", "
        // sum(Employee.id, Blobclob_ByteArray.index, nada.nada) as sum", "
        // Beppe.Signori.Goal "}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        // select.selectFields(selectClause);
        // String expected = "SELECT Employee.id as hello,
        // sum(Employee.id,Blobclob_ByteArray.index,nada.nada) as sum,
        // Beppe.Signori.Goal AS \"Beppe.Signori.Goal\" "; //$NON-NLS-1$
        // System.out.println("select.render(): " + select.renderSql());
        // //$NON-NLS-1$
        // System.out.println("expected : " + expected); //$NON-NLS-1$
        //
        // assertEquals(expected, select.renderSql());
    }

    @Test
    public void testCustomExpressionQuery() {
        Select<String> query = dsl()
                .select("first", "second")
                .from("Employee");

        getLogger().info("Generated select: \n{}", query.sqlQuery());

        query.where("mod(Employee.id, 10) = 1");



        assertTrue(containsIgnoreCase(query.sqlQuery(), "SELECT"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " first AS \"first\", "));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " second "));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " FROM EMPLOYEE WHERE mod(Employee.ID, 10) = 1 "));
    }


    @Test
    public void testQuery1() {
        Select<String> select = dsl()
                        .select("ID", "NAME", "age", "surname", "employeeNumber")
                        .from("Employee");

        final String expectedSql = "SELECT ID AS \"ID\", NAME AS \"NAME\", age AS \"age\", surname AS \"surname\", employeeNumber AS \"employeeNumber\" FROM Employee "; //$NON-NLS-1$
        assertEquals(expectedSql, select.sqlQuery());
    }

    @Test
    public void testQuery2() {
        Select<String> select = dsl()
                .select("ID", "NAME", "age", "surname", "employeeNumber")
                .from("Employee");
        select.where().eq("Employee.id", 1).ge("age", 18).in("Employee.name", new Object[] { "frank", "john", "carl" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        final String expectedSql = "SELECT ID AS \"ID\", NAME AS \"NAME\", age AS \"age\", surname AS \"surname\", employeeNumber AS \"employeeNumber\" FROM Employee WHERE Employee.id = ? AND age >= ? AND Employee.name in ( ?, ?, ? ) ";
        assertEquals(expectedSql, select.sqlQuery());
    }

    @Test
    public void testQuery2Count() {
        Select<String> select = dsl()
                .select("Employee.ID")
                .from("Employee");

        select.where().eq("id", 1).ge("age", 18).in("name", new Object[] { "frank", "john", "carl" });
        final String expectedSql = "SELECT COUNT(*) FROM ( SELECT Employee.ID AS \"Employee.ID\" FROM Employee WHERE id = ? AND age >= ? AND name in ( ?, ?, ? ) ) a ";
        assertEquals(expectedSql, select.sqlRowCountQuery());
    }

    @Test
    public void testQuery3() {
        Select<String> query = dsl()
                .select("Employee.ID AS \"id\"", "Employee.AGE AS \"age\"")
                .from("Employee");
        query.where().eq("Employee.id", 1).ge("Employee.age", 18); //$NON-NLS-1$ //$NON-NLS-2$
        query.orderBy().asc("id"); //$NON-NLS-1$
        query.orderBy().desc("Employee.age"); //$NON-NLS-1$
        final String expectedSql = "SELECT Employee.ID AS \"id\", Employee.AGE AS \"age\" FROM Employee WHERE Employee.id = ? AND Employee.age >= ? ORDER BY id ASC , Employee.age DESC ";
        assertEquals(expectedSql, query.sqlQuery());
    }


    @Test
    public void testQueryWithNullParameter() {
        Select<String> select = dsl()
                .select("Employee.ID")
                .from("EMPLOYEE", "Employee");
        select.where().eq("Employee.AGE", null); //$NON-NLS-1$

        final String expectedSql = "SELECT Employee.ID AS \"Employee.ID\" FROM EMPLOYEE Employee WHERE Employee.AGE = ? ";
        assertEquals(expectedSql, select.sqlQuery());

        List<Object> values = select.sqlValues();
        assertTrue(values.size() == 1);
    }

    @Test
    public void testSameTableJoinQuery1() {
        Select<String> query = dsl()
                .select("e1.ID")
                .from("EMPLOYEE", "e1");
        query.innerJoin("EMPLOYEE", "e2", "e1.name", "e2.name");

        assertTrue(containsIgnoreCase(query.sqlQuery(), "SELECT "));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " e1.ID"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " e1.NAME"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " FROM EMPLOYEE e1 INNER JOIN EMPLOYEE e2 ON e1.NAME = e2.NAME "));
    }

    @Test
    public void testSameTableJoinQueryThreeTimes() {
        Select<String> query = dsl()
                .select("e1.ID")
                .from("EMPLOYEE", "e1");

        query.innerJoin("EMPLOYEE", "e2", "e1.name", "e2.name")
        .innerJoin("EMPLOYEE", "e3", "e1.surname", "e3.name");

        assertTrue(containsIgnoreCase(query.sqlQuery(), "SELECT"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " e1.ID"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " e1.NAME"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " e1.SURNAME"));
        assertTrue(containsIgnoreCase(query.sqlQuery(),
                " FROM EMPLOYEE e1 INNER JOIN EMPLOYEE e2 ON e1.NAME = e2.NAME INNER JOIN EMPLOYEE e3 ON e1.SURNAME = e3.NAME "));

    }

    @Test
    public void testSubQuery1() {

        Select<String> subQuery1 = dsl()
                .select("Employee.id as hello", "People.lastname")
                .from("Employee");
        subQuery1.join("People");
        subQuery1.where().geProperties("Employee.id", "People.id");
        subQuery1.orderBy().asc("People.lastname");

        Select<String> subQuery2 = dsl()
                .select("People2.id")
                .from("People2");
        subQuery2.where().eq("people2.firstname", "wizard");

        Select<String> query = dsl()
                .select("e.id")
                .from("Employee", "e");
        query.innerJoin("People", "p", "e.id", "p.firstname")
             .naturalJoin("Blobclob_ByteArray");
        query.where().in("e.age", subQuery1);
        query.where().nin("p.firstname", subQuery2);

        getLogger().info("Select query is: \n{}", query.sqlQuery());

        assertTrue(containsIgnoreCase(query.sqlQuery(), "SELECT"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " e.ID"));
        assertTrue(containsIgnoreCase(query.sqlQuery(),
                " FROM EMPLOYEE e INNER JOIN PEOPLE p ON e.ID = p.FIRSTNAME NATURAL JOIN Blobclob_ByteArray"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " WHERE e.AGE IN ( SELECT "));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " Employee.ID as hello"));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " People.LASTNAME"));
        assertTrue(containsIgnoreCase(query.sqlQuery(),
                " FROM EMPLOYEE , PEOPLE WHERE Employee.ID >= People.ID ORDER BY People.LASTNAME ASC ) AND p.FIRSTNAME NOT IN "));
        assertTrue(containsIgnoreCase(query.sqlQuery(), " AND p.FIRSTNAME NOT IN ( SELECT"));

        final List<Object> values = query.sqlValues();
        assertTrue(values.size() == 1);
        assertEquals("wizard", values.get(0)); //$NON-NLS-1$

    }

    private boolean containsIgnoreCase(String text, String substring) {
        return text.toLowerCase().contains(substring.toLowerCase());
    }
}
