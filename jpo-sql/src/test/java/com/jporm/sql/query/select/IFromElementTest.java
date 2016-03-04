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
package com.jporm.sql.query.select;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.query.processor.NoOpsStringPropertiesProcessor;
import com.jporm.sql.query.processor.TableNameImpl;
import com.jporm.sql.query.select.from.AFromElement;
import com.jporm.sql.query.select.from.InnerJoinElement;
import com.jporm.sql.query.select.from.LeftOuterJoinElement;
import com.jporm.sql.query.select.from.NaturalJoinElement;
import com.jporm.sql.query.select.from.RightOuterJoinElement;
import com.jporm.sql.query.select.from.SimpleJoinElement;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class IFromElementTest extends BaseSqlTestApi {

    @Test
    public void testCrossJoin() {
        final AFromElement joinElement = new SimpleJoinElement(new TableNameImpl("Employee", "Employee_1"));
        StringBuilder queryElement = new StringBuilder();
        joinElement.sqlElementQuery(queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals(", Employee Employee_1 ", queryElement.toString());
    }

    @Test
    public void testInnerJoin() {
        final AFromElement joinElement = new InnerJoinElement(new TableNameImpl("People", ""), "Employee.id", "People.firstname");
        StringBuilder queryElement = new StringBuilder();
        joinElement.sqlElementQuery(queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals("INNER JOIN People ON Employee.id = People.firstname ", queryElement.toString());
    }

    @Test
    public void testLeftOuterJoin() {
        final AFromElement joinElement = new LeftOuterJoinElement(new TableNameImpl("People", "People_3"), "Employee.id", "People_3.firstname");
        StringBuilder queryElement = new StringBuilder();
        joinElement.sqlElementQuery(queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals("LEFT OUTER JOIN People People_3 ON Employee.id = People_3.firstname ", queryElement.toString());
    }

    @Test
    public void testNaturalJoin() {
        final AFromElement joinElement = new NaturalJoinElement(new TableNameImpl("Employee", "Employee_1"));
        StringBuilder queryElement = new StringBuilder();
        joinElement.sqlElementQuery(queryElement, new NoOpsStringPropertiesProcessor());

        assertEquals("NATURAL JOIN Employee Employee_1 ", queryElement.toString());
    }

    @Test
    public void testRightOuterJoin() {
        final AFromElement joinElement = new RightOuterJoinElement(new TableNameImpl("People", ""));
        StringBuilder queryElement = new StringBuilder();
        joinElement.sqlElementQuery(queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals("RIGHT OUTER JOIN People ", queryElement.toString()); //$NON-NLS-1$
    }

}
