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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.rm.kotlin.session

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

import java.util.Optional
import java.util.UUID

import org.junit.Before
import org.junit.Test

import com.jporm.rm.kotlin.BaseTestApi
import com.jporm.test.domain.section01.Employee

/**
 * <class_description>
 *
 *
 * **notes**:
 *
 *
 * ON : Feb 14, 2013

 * @author Francesco Cina'
 * *
 * @version $Revision
</class_description> */
class SessionCRUDWithOptionalTest : BaseTestApi() {

    @Before
    fun setUp() {
        jpo.tx().execute { session: Session -> session.delete(Employee::class.java).execute() }
    }

    @Test
    fun testCRUDwithNullOptional() {
        jpo.tx().readOnly(true).execute { session: Session ->

            var employee = Employee()

            // Save with null Optional
            run {
                employee.id = 10000
                assertNull(employee.employeeNumber)
                employee = session.save(employee)
            }

            // Fetch with null Optional
            run {
                employee = session.find(Employee::class.java).where().eq("id", employee.id).fetchOne()
                assertNotNull(employee)
                assertNotNull(employee.employeeNumber)
                assertFalse(employee.employeeNumber.isPresent)
            }

            // Update with null Optional
            run {
                employee.employeeNumber = null
                employee = session.update(employee)
                employee = session.find(Employee::class.java).where().eq("id", employee.id).fetchOne()
                assertNotNull(employee.employeeNumber)
                assertFalse(employee.employeeNumber.isPresent)
            }

        }

    }

    @Test
    fun testCRUDwithEmptyOptional() {
        jpo.tx().readOnly(true).execute { session: Session ->

            var employee = Employee()

            // Save with null Optional
            run {
                employee.id = 10000
                employee.employeeNumber = Optional.empty<String>()
                employee = session.save(employee)
            }

            // Fetch with null Optional
            run {
                employee = session.find(Employee::class.java).where().eq("id", employee.id).fetchOne()
                assertNotNull(employee)
                assertNotNull(employee.employeeNumber)
                assertFalse(employee.employeeNumber.isPresent)
            }

            // Update with null Optional
            run {
                employee.employeeNumber = Optional.empty<String>()
                employee = session.update(employee)
                employee = session.find(Employee::class.java).where().eq("id", employee.id).fetchOne()
                assertNotNull(employee.employeeNumber)
                assertFalse(employee.employeeNumber.isPresent)
            }

        }

    }

    @Test
    fun testCRUDwithNotEmptyOptional() {
        jpo.tx().readOnly(true).execute { session: Session ->

            var employee = Employee()
            val employeeNumber = UUID.randomUUID().toString()

            // Save with valid Optional
            run {
                employee.id = 10000
                employee.employeeNumber = Optional.of(employeeNumber)
                employee = session.save(employee)
            }

            // Fetch with valid Optional
            run {
                employee = session.find(Employee::class.java).where().eq("id", employee.id).fetchOne()
                assertNotNull(employee)
                assertNotNull(employee.employeeNumber)
                assertTrue(employee.employeeNumber.isPresent)
                assertEquals(employeeNumber, employee.employeeNumber.get())
            }

            // Update with empty Optional
            run {
                employee.employeeNumber = Optional.empty<String>()
                employee = session.update(employee)
                employee = session.find(Employee::class.java).where().eq("id", employee.id).fetchOne()
                assertNotNull(employee.employeeNumber)
                assertFalse(employee.employeeNumber.isPresent)
            }

            val newEmployeeNumber = UUID.randomUUID().toString()

            // Update with valid Optional
            run {
                employee.employeeNumber = Optional.of(newEmployeeNumber)
                employee = session.update(employee)
                employee = session.find(Employee::class.java).where().eq("id", employee.id).fetchOne()
                assertNotNull(employee.employeeNumber)
                assertTrue(employee.employeeNumber.isPresent)
                assertEquals(newEmployeeNumber, employee.employeeNumber.get())

            }

        }
    }

}
