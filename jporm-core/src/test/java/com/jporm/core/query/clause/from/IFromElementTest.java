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
package com.jporm.core.query.clause.from;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.JPOrm;
import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.core.domain.Zoo_People;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.clause.impl.from.FromElement;
import com.jporm.core.query.clause.impl.from.InnerJoinElement;
import com.jporm.core.query.clause.impl.from.JoinElement;
import com.jporm.core.query.clause.impl.from.LeftOuterJoinElement;
import com.jporm.core.query.clause.impl.from.NaturalJoinElement;
import com.jporm.core.query.clause.impl.from.RightOuterJoinElement;
import com.jporm.core.query.namesolver.NameSolver;
import com.jporm.core.query.namesolver.impl.NameSolverImpl;
import com.jporm.core.session.impl.NullSessionProvider;

/**
 *
 * @author Francesco Cina
 *
 * 27/giu/2011
 */
public class IFromElementTest extends BaseTestApi {

	private NameSolver nameSolver;
	private ServiceCatalog serviceCatalog;

	@Before
	public void setUp() {
		final JPOrm jpOrm = new JPOrm(new NullSessionProvider());
		jpOrm.register(Employee.class );
		jpOrm.register(People.class);
		jpOrm.register(Zoo_People.class);

		serviceCatalog = jpOrm.getServiceCatalog();
		nameSolver = new NameSolverImpl( serviceCatalog, false );
		nameSolver.register(Employee.class, "Employee"); //$NON-NLS-1$
		nameSolver.register(People.class, "People"); //$NON-NLS-1$
		nameSolver.register(Zoo_People.class, "Zoo_People"); //$NON-NLS-1$
	}

	@Test
	public void testCrossJoin() {
		final FromElement joinElement = new JoinElement(serviceCatalog, Employee.class, nameSolver.register(Employee.class, "Employee_1")); //$NON-NLS-1$

		System.out.println( "joinElement.render(): " + joinElement.renderSqlElement(nameSolver) ); //$NON-NLS-1$
		assertEquals(", EMPLOYEE Employee_1_3 ", joinElement.renderSqlElement(nameSolver)); //$NON-NLS-1$
	}

	@Test
	public void testNaturalJoin() {
		final FromElement joinElement = new NaturalJoinElement(serviceCatalog, Employee.class, nameSolver.register(Employee.class, "Employee_1")); //$NON-NLS-1$

		System.out.println( "joinElement.render(): " + joinElement.renderSqlElement(nameSolver) ); //$NON-NLS-1$
		assertEquals("NATURAL JOIN EMPLOYEE Employee_1_3 ", joinElement.renderSqlElement(nameSolver)); //$NON-NLS-1$
	}

	@Test
	public void testInnerJoin() {
		final FromElement joinElement = new InnerJoinElement(serviceCatalog, People.class, nameSolver.register(People.class, "People"), "Employee.id", "People.firstname"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		System.out.println( "joinElement.render(): " + joinElement.renderSqlElement(nameSolver) ); //$NON-NLS-1$
		assertEquals("INNER JOIN PEOPLE People_3 ON Employee_0.ID = People_3.FIRSTNAME ", joinElement.renderSqlElement(nameSolver)); //$NON-NLS-1$
	}

	@Test
	public void testLeftOuterJoin() {
		final FromElement joinElement = new LeftOuterJoinElement(serviceCatalog, People.class, nameSolver.register(People.class, "People"), "Employee.id", "People.firstname"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		System.out.println( "joinElement.render(): " + joinElement.renderSqlElement(nameSolver) ); //$NON-NLS-1$
		assertEquals("LEFT OUTER JOIN PEOPLE People_3 ON Employee_0.ID = People_3.FIRSTNAME ", joinElement.renderSqlElement(nameSolver)); //$NON-NLS-1$
	}

	@Test
	public void testRightOuterJoin() {
		final FromElement joinElement = new RightOuterJoinElement(serviceCatalog, People.class, nameSolver.register(People.class, "People")); //$NON-NLS-1$

		System.out.println( "joinElement.render(): " + joinElement.renderSqlElement(nameSolver) ); //$NON-NLS-1$
		assertEquals("RIGHT OUTER JOIN PEOPLE People_3 ", joinElement.renderSqlElement(nameSolver)); //$NON-NLS-1$
	}

}
