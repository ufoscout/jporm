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

import org.junit.Test;

import com.jporm.sql.dsl.BaseSqlTestApi;
import com.jporm.sql.dsl.query.from.AFromElement;
import com.jporm.sql.dsl.query.from.InnerJoinElement;
import com.jporm.sql.dsl.query.from.LeftOuterJoinElement;
import com.jporm.sql.dsl.query.from.NaturalJoinElement;
import com.jporm.sql.dsl.query.from.RightOuterJoinElement;
import com.jporm.sql.dsl.query.from.SimpleJoinElement;
import com.jporm.sql.dsl.query.processor.NoOpsStringPropertiesProcessor;
import com.jporm.sql.dsl.query.processor.TableNameImpl;

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
        assertEquals(", Employee Employee_1 ", joinElement.sqlElementQuery(getH2DDProfile(), new NoOpsStringPropertiesProcessor()));
    }

    @Test
    public void testInnerJoin() {
        final AFromElement joinElement = new InnerJoinElement(new TableNameImpl("People", ""), "Employee.id", "People.firstname");
        assertEquals("INNER JOIN People ON Employee.id = People.firstname ", joinElement.sqlElementQuery(getH2DDProfile(), new NoOpsStringPropertiesProcessor()));
    }

    @Test
    public void testLeftOuterJoin() {
        final AFromElement joinElement = new LeftOuterJoinElement(new TableNameImpl("People", "People_3"), "Employee.id", "People_3.firstname");
        assertEquals("LEFT OUTER JOIN People People_3 ON Employee.id = People_3.firstname ", joinElement.sqlElementQuery(getH2DDProfile(), new NoOpsStringPropertiesProcessor()));
    }

    @Test
    public void testNaturalJoin() {
        final AFromElement joinElement = new NaturalJoinElement(new TableNameImpl("Employee", "Employee_1"));

        assertEquals("NATURAL JOIN Employee Employee_1 ", joinElement.sqlElementQuery(getH2DDProfile(), new NoOpsStringPropertiesProcessor()));
    }

    @Test
    public void testRightOuterJoin() {
        final AFromElement joinElement = new RightOuterJoinElement(new TableNameImpl("People", ""));

        assertEquals("RIGHT OUTER JOIN People ", joinElement.sqlElementQuery(getH2DDProfile(), new NoOpsStringPropertiesProcessor())); //$NON-NLS-1$
    }

}
