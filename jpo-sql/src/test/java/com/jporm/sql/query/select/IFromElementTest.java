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
import com.jporm.sql.dialect.h2.H2DBProfile;
import com.jporm.sql.query.processor.NoOpsStringPropertiesProcessor;
import com.jporm.sql.query.processor.TableNameImpl;
import com.jporm.sql.query.select.from.JoinElement;
import com.jporm.sql.query.select.from.JoinType;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class IFromElementTest extends BaseSqlTestApi {

    @Test
    public void testCrossJoin() {
        final JoinElement joinElement = JoinElement.build(JoinType.SIMPLE_JOIN, new TableNameImpl("Employee", "Employee_1"));
        StringBuilder queryElement = new StringBuilder();
        new H2DBProfile().getSqlRender().getSelectRender().getFromRender().renderFromElement(joinElement, queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals(", Employee Employee_1 ", queryElement.toString());
    }

    @Test
    public void testInnerJoin() {
        final JoinElement joinElement = JoinElement.build(JoinType.INNER_JOIN, new TableNameImpl("People", ""), "Employee.id", "People.firstname");
        StringBuilder queryElement = new StringBuilder();
        new H2DBProfile().getSqlRender().getSelectRender().getFromRender().renderFromElement(joinElement, queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals("INNER JOIN People ON Employee.id = People.firstname ", queryElement.toString());
    }

    @Test
    public void testLeftOuterJoin() {
        final JoinElement joinElement = JoinElement.build(JoinType.LEFT_OUTER_JOIN, new TableNameImpl("People", "People_3"), "Employee.id", "People_3.firstname");
        StringBuilder queryElement = new StringBuilder();
        new H2DBProfile().getSqlRender().getSelectRender().getFromRender().renderFromElement(joinElement, queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals("LEFT OUTER JOIN People People_3 ON Employee.id = People_3.firstname ", queryElement.toString());
    }

    @Test
    public void testNaturalJoin() {
        final JoinElement joinElement = JoinElement.build(JoinType.NATURAL_JOIN, new TableNameImpl("Employee", "Employee_1"));
        StringBuilder queryElement = new StringBuilder();
        new H2DBProfile().getSqlRender().getSelectRender().getFromRender().renderFromElement(joinElement, queryElement, new NoOpsStringPropertiesProcessor());

        assertEquals("NATURAL JOIN Employee Employee_1 ", queryElement.toString());
    }

    @Test
    public void testRightOuterJoin() {
        final JoinElement joinElement = JoinElement.build(JoinType.RIGHT_OUTER_JOIN, new TableNameImpl("People", ""));
        StringBuilder queryElement = new StringBuilder();
        new H2DBProfile().getSqlRender().getSelectRender().getFromRender().renderFromElement(joinElement, queryElement, new NoOpsStringPropertiesProcessor());
        assertEquals("RIGHT OUTER JOIN People ", queryElement.toString()); //$NON-NLS-1$
    }

}
