/*******************************************************************************
 * Copyright 2013 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.kotlin.query.find

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail

import java.util.ArrayList

import org.junit.Ignore
import org.junit.Test

import com.jporm.commons.core.exception.JpoException
import com.jporm.core.domain.Blobclob_ByteArray
import com.jporm.core.domain.Employee
import com.jporm.core.domain.People
import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.rm.kotlin.JpoRm
import com.jporm.rm.kotlin.JpoRmBuilder
import com.jporm.rm.kotlin.connection.NullTransactionProvider
import com.jporm.sql.dialect.DBType
import com.jporm.sql.query.select.SelectCommon

/**

 * @author Francesco Cina
 * *
 * *         23/giu/2011
 */
class FindQueryTest : BaseTestApi() {

    @Test
    fun testCustomExpressionQuery() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())
        jpOrm.tx { session ->
            val query = session.find(Employee::class.java, "Employee").where("mod(Employee.id, 10) = 1") //$NON-NLS-1$ //$NON-NLS-2$
            logger.info(query.sqlQuery())
            // final String expectedSql = "SELECT Employee_0.ID AS \"id\",
            // Employee_0.NAME AS \"name\", Employee_0.AGE AS \"age\",
            // Employee_0.SURNAME AS \"surname\", Employee_0.EMPLOYEE_NUMBER AS
            // \"employeeNumber\" FROM EMPLOYEE Employee_0 WHERE
            // mod(Employee_0.ID,
            // 10) = 1 ";
            // assertEquals(expectedSql , query.renderSql());

            assertTrue(query.sqlQuery().contains("SELECT"))
            assertTrue(query.sqlQuery().contains(" Employee_0.ID AS \"id\""))
            assertTrue(query.sqlQuery().contains(" Employee_0.NAME AS \"name\""))
            assertTrue(query.sqlQuery().contains(" Employee_0.AGE AS \"age\""))
            assertTrue(query.sqlQuery().contains(" Employee_0.SURNAME AS \"surname\""))
            assertTrue(query.sqlQuery().contains(" Employee_0.EMPLOYEE_NUMBER AS \"employeeNumber\""))
            assertTrue(query.sqlQuery().contains(" FROM EMPLOYEE Employee_0 WHERE mod(Employee_0.ID, 10) = 1 "))
        }
    }

    @Test
    @Ignore
    fun testCustomQuery1() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val select = arrayOf("sum(emp.id, emp.age), count(Blobclob_ByteArray.index), emp.employeeNumber") //$NON-NLS-1$
            val query = session.find<Any>(*select).from(Employee::class.java, "emp").distinct() //$NON-NLS-1$
            query.join(Blobclob_ByteArray::class.java)
            query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("emp.age", "Blobclob_ByteArray.index") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            query.orderBy().asc("id") //$NON-NLS-1$
            query.orderBy().desc("emp.age") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE), count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ? AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC , emp.AGE DESC " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testCustomQuery2() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val select = arrayOf("sum(emp.id, emp.age)", "count(Blobclob_ByteArray.index)", "emp.employeeNumber") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            val query = session.find<Any>(*select).from(Employee::class.java, "emp").distinct() //$NON-NLS-1$
            query.join(Blobclob_ByteArray::class.java)
            query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("emp.age", "Blobclob_ByteArray.index") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            query.orderBy().asc("id") //$NON-NLS-1$
            query.orderBy().desc("emp.employeeNumber") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE), count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ? AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC , emp.EMPLOYEE_NUMBER DESC " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testCustomQuery3() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val select = arrayOf("sum(emp.id, emp.age)", "emp.age, count(Blobclob_ByteArray.index) , emp.employeeNumber") //$NON-NLS-1$ //$NON-NLS-2$
            val query = session.find<Any>(*select).from(Employee::class.java, "emp").distinct() //$NON-NLS-1$
            query.join(Blobclob_ByteArray::class.java)
            query.where().eq("emp.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("emp.age", "Blobclob_ByteArray.index") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            query.orderBy().asc("id") //$NON-NLS-1$
            query.orderBy().desc("emp.employeeNumber") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT DISTINCT sum(emp.ID, emp.AGE), emp.AGE AS \"emp.age\", count(Blobclob_ByteArray.ID), emp.EMPLOYEE_NUMBER AS \"emp.employeeNumber\" FROM EMPLOYEE emp , BLOBCLOB Blobclob_ByteArray WHERE emp.ID = ? AND Blobclob_ByteArray.ID >= ? AND emp.AGE > Blobclob_ByteArray.ID ORDER BY emp.ID ASC , emp.EMPLOYEE_NUMBER DESC " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    // @Test(expected = JpoWrongPropertyNameException.class)
    // public void testIgnoreNotExistingField() {
    // final ConnectionProvider connectionProvider = new
    // NullConnectionProvider();
    // final JpoRm jpOrm = JpoRmBuilder.get().build(connectionProvider);
    // jpOrm.txVoid( session -> {
    //
    // session.find(AutoId.class).ignore("NOT_EXISTING_FIELD");
    //
    // }

    @Test
    fun testOnlineSqlWriting() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider(DBType.H2))
        jpOrm.tx { session ->

            // METHOD ONE
            val subQuery1 = session.find<Any>("Employee.id as hello", "People.lastname").from(Employee::class.java, "Employee") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            subQuery1.join(People::class.java)
            subQuery1.where().geProperties("Employee.id", "People.id") //$NON-NLS-1$ //$NON-NLS-2$
            subQuery1.orderBy().asc("People.lastname") //$NON-NLS-1$

            val subQuery2 = session.find(People::class.java, "people") //$NON-NLS-1$
            subQuery2.where().eq("people.firstname", "wizard") //$NON-NLS-1$ //$NON-NLS-2$

            val query = session.find(Employee::class.java, "e") //$NON-NLS-1$
            query.innerJoin(People::class.java, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray::class.java) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            query.where().`in`("e.age", subQuery1) //$NON-NLS-1$
            query.where().nin("p.firstname", subQuery2) //$NON-NLS-1$

            val methodOneRendering = query.sqlQuery()

            // SAME QUERY WITH OLD ONLINE WRITING
            val oldOnlineMethodWriting = session.find(Employee::class.java, "e") //$NON-NLS-1$
                    .innerJoin(People::class.java, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray::class.java) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    .where()
                    .`in`("e.age", //$NON-NLS-1$
                            session.find<Any>("Employee.id as hello", "People.lastname").from(Employee::class.java, "Employee") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                    .join(People::class.java).where().geProperties("Employee.id", "People.id") //$NON-NLS-1$ //$NON-NLS-2$
                                    .orderBy().asc("People.lastname") //$NON-NLS-1$
                    ).nin("p.firstname", //$NON-NLS-1$
                    session.find(People::class.java, "people").where().eq("people.firstname", "wizard") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ).sqlQuery()

            logger.info("Method one query        : " + methodOneRendering) //$NON-NLS-1$
            logger.info("old online writing query: " + oldOnlineMethodWriting) //$NON-NLS-1$

            assertEquals(methodOneRendering, oldOnlineMethodWriting)

            // SAME QUERY WITH ONLINE WRITING
            val onlineMethodWriting = session.find(Employee::class.java, "e") //$NON-NLS-1$
                    .innerJoin(People::class.java, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray::class.java) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    .where()
                    .`in`("e.age", //$NON-NLS-1$
                            session.find<Any>("Employee.id as hello", "People.lastname").from(Employee::class.java, "Employee") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                    .join(People::class.java).where().geProperties("Employee.id", "People.id") //$NON-NLS-1$ //$NON-NLS-2$
                                    .orderBy().asc("People.lastname") //$NON-NLS-1$
                    ).nin("p.firstname", //$NON-NLS-1$
                    session.find(People::class.java, "people").where().eq("people.firstname", "wizard") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ).sqlQuery()

            logger.info("Method one query    : " + methodOneRendering) //$NON-NLS-1$
            logger.info("online writing query: " + onlineMethodWriting) //$NON-NLS-1$

            assertEquals(methodOneRendering, onlineMethodWriting)
        }

    }

    @Test
    @Ignore
    fun testQuery1() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "Employee") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testQuery2() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "Employee") //$NON-NLS-1$
            query.where().eq("Employee.id", 1).ge("Employee.age", 18).`in`("Employee.name", *arrayOf<Any>("frank", "john", "carl")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee WHERE Employee.ID = ? AND Employee.AGE >= ? AND Employee.NAME in ( ?, ?, ? ) " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testQuery3() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "Employee") //$NON-NLS-1$
            query.where().eq("Employee.id", 1).ge("Employee.age", 18) //$NON-NLS-1$ //$NON-NLS-2$
            query.orderBy().asc("id") //$NON-NLS-1$
            query.orderBy().desc("Employee.age") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee WHERE Employee.ID = ? AND Employee.AGE >= ? ORDER BY Employee.ID ASC , Employee.AGE DESC " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testQuery4() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "employeeAlias") //$NON-NLS-1$
            query.where().eq("employeeAlias.id", 1).ge("age", 18) //$NON-NLS-1$ //$NON-NLS-2$
            query.orderBy().asc("employeeAlias.id") //$NON-NLS-1$
            query.orderBy().desc("employeeAlias.age") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT employeeAlias.ID AS \"id\", employeeAlias.NAME AS \"name\", employeeAlias.AGE AS \"age\", employeeAlias.SURNAME AS \"surname\", employeeAlias.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE employeeAlias WHERE employeeAlias.ID = ? AND employeeAlias.AGE >= ? ORDER BY employeeAlias.ID ASC , employeeAlias.AGE DESC " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testQuery5() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "Employee") //$NON-NLS-1$
            query.join(Blobclob_ByteArray::class.java)
            query.where().eq("Employee.id", 1).ge("Blobclob_ByteArray.index", 18).gtProperties("Employee.age", "Blobclob_ByteArray.index") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            query.orderBy().asc("Employee.id") //$NON-NLS-1$
            query.orderBy().desc("Employee.age") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee , BLOBCLOB Blobclob_ByteArray WHERE Employee.ID = ? AND Blobclob_ByteArray.ID >= ? AND Employee.AGE > Blobclob_ByteArray.ID ORDER BY Employee.ID ASC , Employee.AGE DESC " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testQuery6() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "e") //$NON-NLS-1$
            query.innerJoin(People::class.java, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray::class.java) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            query.where().gt("e.id", 1) //$NON-NLS-1$
            query.orderBy().asc("e.id") //$NON-NLS-1$
            query.orderBy().desc("e.age") //$NON-NLS-1$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT e.ID AS \"id\", e.NAME AS \"name\", e.AGE AS \"age\", e.SURNAME AS \"surname\", e.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e INNER JOIN PEOPLE p ON e.ID = p.FIRSTNAME NATURAL JOIN BLOBCLOB Blobclob_ByteArray WHERE e.ID > ? ORDER BY e.ID ASC , e.AGE DESC " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())
        }
    }

    @Test
    @Ignore
    fun testQueryWithNullParameter() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())
        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "Employee").where().eq("age", null) //$NON-NLS-1$ //$NON-NLS-2$
            logger.info(query.sqlQuery())
            val expectedSql = "SELECT Employee.ID AS \"id\", Employee.NAME AS \"name\", Employee.AGE AS \"age\", Employee.SURNAME AS \"surname\", Employee.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE Employee WHERE Employee.AGE = ? " //$NON-NLS-1$
            assertEquals(expectedSql, query.sqlQuery())

            val values = ArrayList<Any>()
            query.sqlValues(values)
            assertTrue(values.size == 1)
        }
    }

    @Test
    fun testSameTableJoinQuery1() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider(DBType.H2))

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "e1").innerJoin(Employee::class.java, "e2", "e1.name", "e2.name") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            logger.info(query.sqlQuery())
            // final String expectedSql = "SELECT e1_0.ID AS \"id\", e1_0.NAME
            // AS
            // \"name\", e1_0.AGE AS \"age\", e1_0.SURNAME AS \"surname\",
            // e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e1_0
            // INNER
            // JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME "; //$NON-NLS-1$
            // assertEquals(expectedSql , query.renderSql());

            assertTrue(query.sqlQuery().contains("SELECT "))
            assertTrue(query.sqlQuery().contains(" e1_0.ID AS \"id\""))
            assertTrue(query.sqlQuery().contains(" e1_0.NAME AS \"name\""))
            assertTrue(query.sqlQuery().contains(" e1_0.AGE AS \"age\""))
            assertTrue(query.sqlQuery().contains(" e1_0.SURNAME AS \"surname\""))
            assertTrue(query.sqlQuery().contains(" e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\""))
            assertTrue(query.sqlQuery().contains(" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME "))
        }
    }

    @Test
    fun testSameTableJoinQueryThreeTimes() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider(DBType.H2))

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java, "e1").innerJoin(Employee::class.java, "e2", "e1.name", "e2.name").innerJoin(Employee::class.java, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    "e3", //$NON-NLS-1$
                    "e1.surname", "e3.name") //$NON-NLS-1$ //$NON-NLS-2$
            logger.info(query.sqlQuery())
            // final String expectedSql = "SELECT e1_0.ID AS \"id\", e1_0.NAME
            // AS
            // \"name\", e1_0.AGE AS \"age\", e1_0.SURNAME AS \"surname\",
            // e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e1_0
            // INNER
            // JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME INNER JOIN EMPLOYEE
            // e3_2
            // ON e1_0.SURNAME = e3_2.NAME "; //$NON-NLS-1$

            assertTrue(query.sqlQuery().contains("SELECT"))
            assertTrue(query.sqlQuery().contains(" e1_0.ID AS \"id\""))
            assertTrue(query.sqlQuery().contains(" e1_0.NAME AS \"name\""))
            assertTrue(query.sqlQuery().contains(" e1_0.AGE AS \"age\""))
            assertTrue(query.sqlQuery().contains(" e1_0.SURNAME AS \"surname\""))
            assertTrue(query.sqlQuery().contains(" e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\""))
            assertTrue(query.sqlQuery()
                    .contains(" FROM EMPLOYEE e1_0 INNER JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME INNER JOIN EMPLOYEE e3_2 ON e1_0.SURNAME = e3_2.NAME "))
        }

    }

    @Test
    fun testSubQuery1() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider(DBType.H2))

        jpOrm.tx { session ->

            val subQuery1 = session.find<Any>("Employee.id as hello", "People.lastname").from(Employee::class.java, "Employee") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            subQuery1.join(People::class.java)
            subQuery1.where().geProperties("Employee.id", "People.id") //$NON-NLS-1$ //$NON-NLS-2$
            subQuery1.orderBy().asc("People.lastname") //$NON-NLS-1$
            val subQuery2 = session.find(People::class.java, "people") //$NON-NLS-1$
            subQuery2.where().eq("people.firstname", "wizard") //$NON-NLS-1$ //$NON-NLS-2$

            val query = session.find(Employee::class.java, "e") //$NON-NLS-1$
            query.innerJoin(People::class.java, "p", "e.id", "p.firstname").naturalJoin(Blobclob_ByteArray::class.java) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            query.where().`in`("e.age", subQuery1) //$NON-NLS-1$
            query.where().nin("p.firstname", subQuery2) //$NON-NLS-1$

            logger.info(query.sqlQuery())
            // final String expectedSql = "SELECT e_0.ID AS \"id\", e_0.NAME AS
            // \"name\", e_0.AGE AS \"age\", e_0.SURNAME AS \"surname\",
            // e_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e_0 INNER
            // JOIN PEOPLE p_1 ON e_0.ID = p_1.FIRSTNAME NATURAL JOIN BLOBCLOB
            // Blobclob_ByteArray_2 WHERE e_0.AGE IN ( "
            // + "SELECT Employee_0.ID as hello, People_1.LASTNAME AS
            // \"People.lastname\" FROM EMPLOYEE Employee_0 , PEOPLE People_1
            // WHERE
            // Employee_0.ID >= People_1.ID ORDER BY People_1.LASTNAME ASC ) AND
            // p_1.FIRSTNAME NOT IN ( SELECT people_0.FIRSTCLOB AS
            // \"firstclob\",
            // people_0.ID AS \"id\", people_0.SECONDBLOB AS \"secondblob\",
            // people_0.BIRTHDATE AS \"birthdate\", people_0.LASTNAME AS
            // \"lastname\", people_0.FIRSTNAME AS \"firstname\",
            // people_0.FIRSTBLOB
            // AS \"firstblob\", people_0.DEATHDATE AS \"deathdate\" FROM PEOPLE
            // people_0 WHERE people_0.FIRSTNAME = ? ) "; //$NON-NLS-1$
            // assertEquals(expectedSql , query.renderSql());

            assertTrue(query.sqlQuery().contains("SELECT"))
            assertTrue(query.sqlQuery().contains(" e_0.ID AS \"id\""))
            assertTrue(query.sqlQuery().contains(" e_0.NAME AS \"name\""))
            assertTrue(query.sqlQuery().contains(" e_0.AGE AS \"age\""))
            assertTrue(query.sqlQuery().contains(" e_0.SURNAME AS \"surname\""))
            assertTrue(query.sqlQuery().contains(" e_0.EMPLOYEE_NUMBER AS \"employeeNumber\""))
            assertTrue(
                    query.sqlQuery().contains(" FROM EMPLOYEE e_0 INNER JOIN PEOPLE p_1 ON e_0.ID = p_1.FIRSTNAME NATURAL JOIN BLOBCLOB Blobclob_ByteArray_2"))
            assertTrue(query.sqlQuery().contains(" WHERE e_0.AGE IN ( SELECT "))
            assertTrue(query.sqlQuery().contains(" Employee_0.ID as hello"))
            assertTrue(query.sqlQuery().contains(" People_1.LASTNAME AS \"People.lastname\""))
            assertTrue(query.sqlQuery().contains(
                    " FROM EMPLOYEE Employee_0 , PEOPLE People_1 WHERE Employee_0.ID >= People_1.ID ORDER BY People_1.LASTNAME ASC ) AND p_1.FIRSTNAME NOT IN "))
            assertTrue(query.sqlQuery().contains(" AND p_1.FIRSTNAME NOT IN ( SELECT"))

            val values = ArrayList<Any>()
            query.sqlValues(values)
            assertTrue(values.size == 1)
            assertEquals("wizard", values[0]) //$NON-NLS-1$
        }

    }

    @Test
    @Ignore
    fun testWrongFieldQuery1() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider())

        jpOrm.tx { session ->

            val query = session.find(Employee::class.java)
            query.join(Blobclob_ByteArray::class.java)
            query.where().eq("id", 1).ge("Blobclob_ByteArray.index", 18).gt("ages", 18) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            try {
                query.sqlQuery()
                fail("field ages is wrong!") //$NON-NLS-1$
            } catch (e: JpoException) {
                e.printStackTrace()
            }
        }
    }

    @Test
    fun testJoinWithInnerSelectQuery() {
        val jpOrm = JpoRmBuilder.get().build(NullTransactionProvider(DBType.H2))

        jpOrm.tx { session ->

            session.find(People::class.java, "p").where("p.lastname = ?", "X")

            val query = session.find(Employee::class.java, "e1")
                    .innerJoin(session.find(People::class.java, "p").where("p.lastname = ?", "X"), "e2", "e1.name", "e2.firstname").where("mod(e1.id, 10) = ?", "Y")

            logger.info("Query is: \n{}", query.sqlQuery())
            // final String expectedSql = "SELECT e1_0.ID AS \"id\", e1_0.NAME
            // AS
            // \"name\", e1_0.AGE AS \"age\", e1_0.SURNAME AS \"surname\",
            // e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\" FROM EMPLOYEE e1_0
            // INNER
            // JOIN EMPLOYEE e2_1 ON e1_0.NAME = e2_1.NAME "; //$NON-NLS-1$
            // assertEquals(expectedSql , query.renderSql());

            assertTrue(query.sqlQuery().contains("SELECT "))
            assertTrue(query.sqlQuery().contains(" e1_0.ID AS \"id\""))
            assertTrue(query.sqlQuery().contains(" e1_0.NAME AS \"name\""))
            assertTrue(query.sqlQuery().contains(" e1_0.AGE AS \"age\""))
            assertTrue(query.sqlQuery().contains(" e1_0.SURNAME AS \"surname\""))
            assertTrue(query.sqlQuery().contains(" e1_0.EMPLOYEE_NUMBER AS \"employeeNumber\""))
            assertTrue(query.sqlQuery().contains(" FROM EMPLOYEE e1_0 INNER JOIN (SELECT p_0.FIRSTNAME AS \"firstname\", p_0.BIRTHDATE AS \"birthdate\""))
            assertTrue(query.sqlQuery().contains(" e2 ON e1_0.NAME = e2.firstname "))

            val values = query.sqlValues()

            assertEquals(2, values.size.toLong())
            assertEquals("X", values[0])
            assertEquals("Y", values[1])
        }
    }

}
