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
package com.jporm.commons.core.query.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.commons.core.BaseCommonsCoreTestApi;
import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.core.domain.Zoo_People;

public class NameSolverImplTest extends BaseCommonsCoreTestApi {

    private PropertiesFactory propertiesFactory = new PropertiesFactory();

    @Test
    public void testNameSolver1() {

        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);

        nameSolver.getTableName(Employee.class, "Employee_1"); //$NON-NLS-1$
        nameSolver.getTableName(People.class, "People"); //$NON-NLS-1$
        nameSolver.getTableName(Zoo_People.class, "Zoo_People_1"); //$NON-NLS-1$

        assertEquals("Employee_1_0.ID", nameSolver.solvePropertyName("id")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("People_1.ID", nameSolver.solvePropertyName("People.id")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("Zoo_People_1_2.ID", nameSolver.solvePropertyName("Zoo_People_1.id")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void testNameSolver2() {

        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);

        nameSolver.getTableName(Employee.class, "EmployeeAlias"); //$NON-NLS-1$
        nameSolver.getTableName(People.class, "People_1"); //$NON-NLS-1$

        assertEquals("EmployeeAlias_0.ID", nameSolver.solvePropertyName("id")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("EmployeeAlias_0.ID", nameSolver.solvePropertyName("EmployeeAlias.id")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("People_1_1.ID", nameSolver.solvePropertyName("People_1.id")); //$NON-NLS-1$ //$NON-NLS-2$

        boolean ormExceptionThrown = false;
        try {
            nameSolver.solvePropertyName("Zoo_People.id"); //$NON-NLS-1$
        } catch (final JpoWrongPropertyNameException e) {
            ormExceptionThrown = true;
            System.out.println("OrmException thrown with message: " + e.getMessage()); //$NON-NLS-1$
        }
        assertTrue(ormExceptionThrown);
    }

    @Test
    public void testNameSolver3() {

        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);

        nameSolver.getTableName(Employee.class, "EmployeeAlias"); //$NON-NLS-1$

        assertEquals("EmployeeAlias_0.ID", nameSolver.solvePropertyName("EmployeeAlias.id")); //$NON-NLS-1$ //$NON-NLS-2$

        boolean ormExceptionThrown = false;
        try {
            nameSolver.getTableName(People.class, "People_1"); //$NON-NLS-1$
        } catch (final JpoWrongPropertyNameException e) {
            ormExceptionThrown = true;
            System.out.println("OrmException thrown with message: " + e.getMessage()); //$NON-NLS-1$
        }
        assertFalse(ormExceptionThrown);

        ormExceptionThrown = false;
        try {
            nameSolver.solvePropertyName("Zoo_People.id"); //$NON-NLS-1$
        } catch (final JpoWrongPropertyNameException e) {
            ormExceptionThrown = true;
            System.out.println("OrmException thrown with message: " + e.getMessage()); //$NON-NLS-1$
        }
        assertTrue(ormExceptionThrown);
    }

    @SuppressWarnings("nls")
    @Test
    public void testNameSolverBenchmark() {
        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);

        nameSolver.getTableName(People.class, "people");
        nameSolver.getTableName(Employee.class, "emp");

        final Date now = new Date();
        int howMany = 1000000;
        for (int i = 0; i < howMany; i++) {
            nameSolver.solvePropertyName("people.id");
            nameSolver.solvePropertyName("people.firstname");
            nameSolver.solvePropertyName("people.lastname");
            nameSolver.solvePropertyName("people.birthdate");
            nameSolver.solvePropertyName("people.deathdate");
            nameSolver.solvePropertyName("people.firstblob");
            nameSolver.solvePropertyName("emp.id");
            nameSolver.solvePropertyName("emp.age");
            nameSolver.solvePropertyName("emp.surname");
            nameSolver.solvePropertyName("emp.employeeNumber");
        }
        getLogger().info("Time to solve " + howMany + " properties: " + (new Date().getTime() - now.getTime()));
    }

    @Test
    public void testResolveCustomExpression1() {

        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);
        nameSolver.getTableName(People.class, "people");
        nameSolver.getTableName(Employee.class, "emp");

        String expression = "((people.firstname != emp.surname) OR (people.lastname=='ufo') )AND NOT ( mod(emp.id, 1) = 10 )";
        String expectedOutput = "((people_0.FIRSTNAME !=emp_1.SURNAME) OR (people_0.LASTNAME=='ufo') )AND NOT ( mod(emp_1.ID, 1) = 10 )";
        StringBuilder outputBuilder = new StringBuilder();
        nameSolver.solveAllPropertyNames(expression, outputBuilder);
        String output = outputBuilder.toString();

        getLogger().info("Input----> " + expression);
        getLogger().info("Output---> " + output);
        getLogger().info("Expected-> " + expectedOutput);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testResolveCustomExpressionWithoutAlias1() {

        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);
        nameSolver.getTableName(People.class, "people");
        nameSolver.getTableName(Employee.class, "emp");

        String expression = "((firstname != emp.surname) OR (lastname=='ufo') )AND NOT ( mod(id, 1) = 10 )";
        String expectedOutput = "((people_0.FIRSTNAME !=emp_1.SURNAME) OR (people_0.LASTNAME=='ufo') )AND NOT ( mod(people_0.ID, 1) = 10 )";
        StringBuilder outputBuilder = new StringBuilder();
        nameSolver.solveAllPropertyNames(expression, outputBuilder);
        String output = outputBuilder.toString();

        getLogger().info("Input----> " + expression);
        getLogger().info("Output---> " + output);
        getLogger().info("Expected-> " + expectedOutput);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testResolveCustomExpressionWithoutAlias2() {

        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);
        nameSolver.getTableName(People.class, "people");
        nameSolver.getTableName(Employee.class, "emp");

        String expression = "firstname = emp.id and ((firstname != emp.surname) OR (lastname=='ufo') )AND NOT ( mod(id, 1) = 10 )";
        String expectedOutput = "people_0.FIRSTNAME =emp_1.ID and ((people_0.FIRSTNAME !=emp_1.SURNAME) OR (people_0.LASTNAME=='ufo') )AND NOT ( mod(people_0.ID, 1) = 10 )";
        StringBuilder outputBuilder = new StringBuilder();
        nameSolver.solveAllPropertyNames(expression, outputBuilder);
        String output = outputBuilder.toString();

        getLogger().info("Input----> " + expression);
        getLogger().info("Output---> " + output);
        getLogger().info("Expected-> " + expectedOutput);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testResolveCustomExpressionWithoutAlias3() {

        final ClassTablePropertiesProcessor nameSolver = new ClassTablePropertiesProcessor(getClassDescriptorMap(), propertiesFactory, false);
        nameSolver.getTableName(People.class, "people");
        nameSolver.getTableName(Employee.class, "emp");

        String expression = "firstname as first, emp.id, count(id) as countId, sum(id, emp.id)";
        String expectedOutput = "people_0.FIRSTNAME as first,emp_1.ID, count(people_0.ID) as countId, sum(people_0.ID,emp_1.ID)";
        StringBuilder outputBuilder = new StringBuilder();
        nameSolver.solveAllPropertyNames(expression, outputBuilder);
        String output = outputBuilder.toString();

        getLogger().info("Input----> " + expression);
        getLogger().info("Output---> " + output);
        getLogger().info("Expected-> " + expectedOutput);

        assertEquals(expectedOutput, output);
    }

}
