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
package com.jporm.core.query.delete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.JPO;
import com.jporm.core.JPOBuilder;
import com.jporm.core.domain.Employee;
import com.jporm.core.domain.Zoo_People;
import com.jporm.core.session.Session;
import com.jporm.core.session.SessionProvider;
import com.jporm.core.session.impl.NullSessionProvider;

/**
 *
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class OrmDeleteTest extends BaseTestApi {

	private JPO jpOrm;

	@Before
	public void setUp() {
		final SessionProvider connectionProvider = new NullSessionProvider();
		jpOrm = new JPOBuilder().build(connectionProvider);
	}

	@Test
	public void testUpdate1() {

		final Session session =  jpOrm.session();

		final CustomDeleteQuery<Employee> delete = session.deleteQuery(Employee.class);
		delete.where().eq("id", 1); //$NON-NLS-1$
		System.out.println(delete.renderSql());
		final String expectedSql = "DELETE FROM EMPLOYEE WHERE ID = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , delete.renderSql());

		final List<Object> values = new ArrayList<Object>();
		delete.sql().appendValues(values);

		assertEquals(1, values.size());

		assertEquals( Integer.valueOf(1) , values.get(0));

	}

	@Test
	public void testUpdate2() {

		final Session session =  jpOrm.session();

		final Date date = new Date( new java.util.Date().getTime() );
		final CustomDeleteQuery<Zoo_People> delete = session.deleteQuery(Zoo_People.class);
		delete.where().eq("id", 1).eq("birthdate", date).eq("deathdate", date); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		System.out.println(delete.renderSql());
		final String expectedSql = "DELETE FROM ZOO.PEOPLE WHERE ID = ? AND BIRTHDATE = ? AND DEATHDATE = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , delete.renderSql());

		final List<Object> values = new ArrayList<Object>();
		delete.sql().appendValues(values);

		assertEquals(3, values.size());

		assertEquals( Integer.valueOf(1) , values.get(0));
		assertEquals( date , values.get(1));
		assertEquals( date , values.get(2));

	}

	@Test
	public void testOnlineSqlWriting() {
		final Session nullSession = new JPOBuilder().build(new NullSessionProvider()).session();

		// METHOD ONE
		final Date date = new Date( new java.util.Date().getTime() );
		final CustomDeleteQuery<Zoo_People> delete = nullSession.deleteQuery(Zoo_People.class);
		delete.where().eq("id", 1).eq("birthdate", date).eq("deathdate", date); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		final String methodOneRendering = delete.renderSql();


		// SAME QUERY WITH OLD ONLINE WRITING
		final String oldOnlineMethodWriting = nullSession.deleteQuery(Zoo_People.class)
				.where().eq("id", 1).eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.root().renderSql();

		System.out.println("Method one query        : " + methodOneRendering); //$NON-NLS-1$
		System.out.println("old online writing query: " + oldOnlineMethodWriting); //$NON-NLS-1$

		assertEquals(methodOneRendering, oldOnlineMethodWriting);

		// SAME QUERY WITH ONLINE WRITING
		final String onlineMethodWriting = nullSession.deleteQuery(Zoo_People.class)
				.where().eq("id", 1).eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.root().renderSql();

		System.out.println("Method one query    : " + methodOneRendering); //$NON-NLS-1$
		System.out.println("online writing query: " + onlineMethodWriting); //$NON-NLS-1$

		assertEquals(methodOneRendering, onlineMethodWriting);
	}

	@Test
	public void executedShouldBeValid() {

		final Session session =  jpOrm.session();
		final CustomDeleteQuery<Employee> delete = session.deleteQuery(Employee.class);
		assertFalse(delete.isExecuted());
		delete.now();
		assertTrue(delete.isExecuted());

	}
}
