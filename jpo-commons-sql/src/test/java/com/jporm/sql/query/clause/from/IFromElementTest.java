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
package com.jporm.sql.query.clause.from;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.core.domain.Zoo_People;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.query.clause.impl.from.FromElement;
import com.jporm.sql.query.clause.impl.from.InnerJoinElement;
import com.jporm.sql.query.clause.impl.from.JoinElement;
import com.jporm.sql.query.clause.impl.from.LeftOuterJoinElement;
import com.jporm.sql.query.clause.impl.from.NaturalJoinElement;
import com.jporm.sql.query.clause.impl.from.RightOuterJoinElement;
import com.jporm.sql.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 *         27/giu/2011
 */
public class IFromElementTest extends BaseSqlTestApi {

    private NameSolver nameSolver;

    @Before
    public void setUp() {
        nameSolver = getNameSolver(false);
        nameSolver.register(Employee.class, "Employee", getClassDescriptor(Employee.class)); //$NON-NLS-1$
        nameSolver.register(People.class, "People", getClassDescriptor(People.class)); //$NON-NLS-1$
        nameSolver.register(Zoo_People.class, "Zoo_People", getClassDescriptor(Zoo_People.class)); //$NON-NLS-1$
    }

    @Test
    public void testCrossJoin() {
        final FromElement joinElement = new JoinElement<>(getClassDescriptor(Employee.class), Employee.class,
                nameSolver.register(Employee.class, "Employee_1", getClassDescriptor(Employee.class))); //$NON-NLS-1$

        System.out.println("joinElement.render(): " + joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
        assertEquals(", EMPLOYEE Employee_1_3 ", joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
    }

    @Test
    public void testInnerJoin() {
        final FromElement joinElement = new InnerJoinElement<>(getClassDescriptor(People.class), People.class,
                nameSolver.register(People.class, "People", getClassDescriptor(People.class)), "Employee.id", "People.firstname"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        System.out.println("joinElement.render(): " + joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
        assertEquals("INNER JOIN PEOPLE People_3 ON Employee_0.ID = People_3.FIRSTNAME ", joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
    }

    @Test
    public void testLeftOuterJoin() {
        final FromElement joinElement = new LeftOuterJoinElement<>(getClassDescriptor(People.class), People.class,
                nameSolver.register(People.class, "People", getClassDescriptor(People.class)), "Employee.id", "People.firstname"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        System.out.println("joinElement.render(): " + joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
        assertEquals("LEFT OUTER JOIN PEOPLE People_3 ON Employee_0.ID = People_3.FIRSTNAME ", joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
    }

    @Test
    public void testNaturalJoin() {
        final FromElement joinElement = new NaturalJoinElement<>(getClassDescriptor(Employee.class), Employee.class,
                nameSolver.register(Employee.class, "Employee_1", getClassDescriptor(Employee.class))); //$NON-NLS-1$

        System.out.println("joinElement.render(): " + joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
        assertEquals("NATURAL JOIN EMPLOYEE Employee_1_3 ", joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
    }

    @Test
    public void testRightOuterJoin() {
        final FromElement joinElement = new RightOuterJoinElement<>(getClassDescriptor(People.class), People.class,
                nameSolver.register(People.class, "People", getClassDescriptor(People.class))); //$NON-NLS-1$

        System.out.println("joinElement.render(): " + joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
        assertEquals("RIGHT OUTER JOIN PEOPLE People_3 ", joinElement.renderSqlElement(getH2DDProfile(), nameSolver)); //$NON-NLS-1$
    }

}
