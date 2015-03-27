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
package com.jporm.core.query.update;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.JPO;
import com.jporm.core.JPOrm;
import com.jporm.core.domain.Employee;
import com.jporm.core.domain.Zoo_People;
import com.jporm.core.query.update.CustomUpdateQuery;
import com.jporm.core.session.Session;
import com.jporm.core.session.SessionProvider;
import com.jporm.core.session.impl.NullSessionProvider;

/**
 *
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class OrmUpdateTest extends BaseTestApi {

	private JPO jpOrm;

	@Before
	public void setUp() {
		final SessionProvider connectionProvider = new NullSessionProvider();
		jpOrm = new JPOrm(connectionProvider);
		jpOrm.config().register(Employee.class);
		jpOrm.config().register(Zoo_People.class);
	}

	@Test
	public void testUpdate1() {

		final Session session =  jpOrm.session();

		final CustomUpdateQuery update = session.updateQuery(Employee.class);
		update.set().eq("age", "12"); //$NON-NLS-1$ //$NON-NLS-2$
		update.where().eq("id", 1); //$NON-NLS-1$
		System.out.println(update.renderSql());
		final String expectedSql = "UPDATE EMPLOYEE SET AGE = ? WHERE ID = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , update.renderSql());

		final List<Object> values = new ArrayList<Object>();
		update.sql().appendValues(values);

		assertEquals(2, values.size());

		assertEquals( "12" , values.get(0)); //$NON-NLS-1$
		assertEquals( Integer.valueOf(1) , values.get(1));

	}

	@Test
	public void testUpdate2() {

		final Session session =  jpOrm.session();

		final Date date = new Date( new java.util.Date().getTime() );
		final CustomUpdateQuery update = session.updateQuery(Zoo_People.class);
		update.set().eq("birthdate", date); //$NON-NLS-1$
		update.set().eq("deathdate", date); //$NON-NLS-1$
		update.where().eq("id", 1); //$NON-NLS-1$
		System.out.println(update.renderSql());
		final String expectedSql = "UPDATE ZOO.PEOPLE SET BIRTHDATE = ? , DEATHDATE = ? WHERE ID = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , update.renderSql());

		final List<Object> values = new ArrayList<Object>();
		update.sql().appendValues(values);

		assertEquals(3, values.size());

		assertEquals( date , values.get(0));
		assertEquals( date , values.get(1));
		assertEquals( Integer.valueOf(1) , values.get(2));

	}

	@Test
	public void testOnlineSqlWriting() {
		final Session nullSession =  new JPOrm(new NullSessionProvider()).session();

		// METHOD ONE
		final Date date = new Date( new java.util.Date().getTime() );
		final CustomUpdateQuery update = nullSession.updateQuery(Zoo_People.class);
		update.where().eq("birthdate", date); //$NON-NLS-1$
		update.where().eq("deathdate", date); //$NON-NLS-1$
		update.set().eq("id", 1); //$NON-NLS-1$

		final String methodOneRendering = update.renderSql();


		// SAME QUERY WITH OLD ONLINE WRITING
		final String oldOnlineMethodWriting = nullSession.updateQuery(Zoo_People.class)
				.where().eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$
				.root().set().eq("id", 1) //$NON-NLS-1$
				.root().renderSql();

		System.out.println("Method one query    : " + methodOneRendering); //$NON-NLS-1$
		System.out.println("online writing query: " + oldOnlineMethodWriting); //$NON-NLS-1$

		assertEquals(methodOneRendering, oldOnlineMethodWriting);

		// SAME QUERY WITH ONLINE WRITING
		final String onlineMethodWriting = nullSession.updateQuery(Zoo_People.class)
				.where().eq("birthdate", date).eq("deathdate", date) //$NON-NLS-1$ //$NON-NLS-2$
				.set().eq("id", 1) //$NON-NLS-1$
				.root().renderSql();

		System.out.println("Method one query    : " + methodOneRendering); //$NON-NLS-1$
		System.out.println("online writing query: " + onlineMethodWriting); //$NON-NLS-1$

		assertEquals(methodOneRendering, onlineMethodWriting);
	}

	@Test
	public void executedShouldBeValid() {

		final Session session =  jpOrm.session();
		final CustomUpdateQuery update = session.updateQuery(Zoo_People.class);
		update.where().eq("id", -1); //$NON-NLS-1$
		update.set().eq("firstname", UUID.randomUUID().toString()); //$NON-NLS-1$

		assertFalse(update.isExecuted());
		update.now();
		assertTrue(update.isExecuted());

	}

}
