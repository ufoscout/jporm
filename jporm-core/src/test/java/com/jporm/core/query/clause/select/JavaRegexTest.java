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
package com.jporm.core.query.clause.select;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.query.clause.OrmCustomSelect;
import com.jporm.core.query.namesolver.NameSolverImpl;

/**
 * 
 * @author Francesco Cina
 *
 * 04/lug/2011
 */
public class JavaRegexTest extends BaseTestApi {

    @Test
    public void testRegex() {

        final Pattern pattern = Pattern.compile(OrmCustomSelect.SQL_SELECT_SPLIT_PATTERN);

        final String text = "Employee.id, count(Employee.age), sum(old.age, young.age) as sum,me.address, SchemaNAme.table.id, sum(schema.old.age, young.age, schema.table.name) as sum2"; //$NON-NLS-1$

        final List<String> expected = new ArrayList<String>();
        expected.add("Employee.id"); //$NON-NLS-1$
        expected.add("count(Employee.age)"); //$NON-NLS-1$
        expected.add("sum(old.age, young.age) as sum"); //$NON-NLS-1$
        expected.add("me.address"); //$NON-NLS-1$
        expected.add("SchemaNAme.table.id"); //$NON-NLS-1$
        expected.add("sum(schema.old.age, young.age, schema.table.name) as sum2"); //$NON-NLS-1$

        final Matcher m = pattern.matcher(text);
        int count = 0;

        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }

        assertEquals(expected.size() , count);
    }

    @Test
    public void testRegex5() {

        final Pattern pattern = Pattern.compile(OrmCustomSelect.SQL_SELECT_SPLIT_PATTERN);

        Matcher m = pattern.matcher("Employee.id"); //$NON-NLS-1$

        int count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.id"); //$NON-NLS-1$
            assertEquals("Employee.id", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1 , count);
    }

    @Test
    public void testRegex6() {

        final Pattern pattern = Pattern.compile(OrmCustomSelect.SQL_SELECT_SPLIT_PATTERN);

        Matcher m = pattern.matcher("Employee.id as hello"); //$NON-NLS-1$

        int count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.id as hello"); //$NON-NLS-1$
            assertEquals("Employee.id as hello", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1 , count);
    }

    @Test
    public void testRegex2() {
        final Pattern pattern = Pattern.compile(NameSolverImpl.FIND_ALL_PROPERTY_PATTERN);

        Matcher m = pattern.matcher("Employee.id"); //$NON-NLS-1$

        int count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.id"); //$NON-NLS-1$
            assertEquals("Employee.id", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1 , count);

        //-----------------------

        m = pattern.matcher(" count(Employee.age)"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.age"); //$NON-NLS-1$
            assertEquals("Employee.age", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1 , count);

        //------------------------

        m = pattern.matcher(" sum(old.age, young.age) as sum"); //$NON-NLS-1$
        List<String> expected = new ArrayList<String>();
        expected.add("old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size() , count);


        //-----------------------

        m = pattern.matcher(" SchemaNAme.table.id"); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("SchemaNAme.table.id"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size() , count);


        //------------------------

        m = pattern.matcher(" sum(schema.old.age, young.age, schema.table.name) as sum2 "); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("schema.old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$
        expected.add("schema.table.name"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size() , count);

    }

    @Test
    public void testRegex3() {
        final Pattern pattern = Pattern.compile(NameSolverImpl.FIND_ALL_PROPERTY_PATTERN);

        Matcher m = pattern.matcher("Employee.id"); //$NON-NLS-1$

        int count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.id"); //$NON-NLS-1$
            assertEquals("Employee.id", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1 , count);

        //-----------------------

        m = pattern.matcher(" count(Employee.age)"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: Employee.age"); //$NON-NLS-1$
            assertEquals("Employee.age", m.group().trim()); //$NON-NLS-1$
            count++;
        }
        assertEquals(1 , count);

        //------------------------

        m = pattern.matcher(" sum(old.age, young.age) as sum"); //$NON-NLS-1$
        List<String> expected = new ArrayList<String>();
        expected.add("old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size() , count);


        //-----------------------

        m = pattern.matcher(" SchemaNAme.table.id"); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("SchemaNAme.table.id"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size() , count);


        //------------------------

        m = pattern.matcher(" sum(schema.old.age, young.age, schema.table.name) as sum2 "); //$NON-NLS-1$
        expected = new ArrayList<String>();
        expected.add("schema.old.age"); //$NON-NLS-1$
        expected.add("young.age"); //$NON-NLS-1$
        expected.add("schema.table.name"); //$NON-NLS-1$

        count = 0;
        while (m.find()) {
            System.out.println("group: " + m.group() + " - start: " + m.start() + " - end: " + m.end()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            System.out.println("expec: " + expected.get(count)); //$NON-NLS-1$
            assertEquals(expected.get(count), m.group().trim());
            count++;
        }
        assertEquals(expected.size() , count);

    }
}
