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
package com.jporm.sql.query.update;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.core.domain.Zoo_People;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.H2DBProfile;
import com.jporm.sql.query.clause.Update;
import com.jporm.sql.query.clause.impl.UpdateImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;

/**
 *
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class OrmUpdateTest extends BaseSqlTestApi {

	@Test
	public void testUpdate1() {

		Update update = new UpdateImpl<>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
		update.set().eq("age", "12"); //$NON-NLS-1$ //$NON-NLS-2$
		update.where().eq("id", 1); //$NON-NLS-1$
		System.out.println(update.renderSql());
		final String expectedSql = "UPDATE EMPLOYEE SET AGE = ? WHERE ID = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , update.renderSql());

		final List<Object> values = new ArrayList<Object>();
		update.appendValues(values);

		assertEquals(2, values.size());

		assertEquals( "12" , values.get(0)); //$NON-NLS-1$
		assertEquals( Integer.valueOf(1) , values.get(1));

	}

	@Test
	public void testUpdate2() {

		final Date date = new Date( new java.util.Date().getTime() );
		Update update = new UpdateImpl<>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Zoo_People.class);
		update.set().eq("birthdate", date); //$NON-NLS-1$
		update.set().eq("deathdate", date); //$NON-NLS-1$
		update.where().eq("id", 1); //$NON-NLS-1$
		System.out.println(update.renderSql());
		final String expectedSql = "UPDATE ZOO.PEOPLE SET BIRTHDATE = ? , DEATHDATE = ? WHERE ID = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , update.renderSql());

		final List<Object> values = new ArrayList<Object>();
		update.appendValues(values);

		assertEquals(3, values.size());

		assertEquals( date , values.get(0));
		assertEquals( date , values.get(1));
		assertEquals( Integer.valueOf(1) , values.get(2));

	}

	@Test
	public void testOnlineSqlWriting() {
		// METHOD ONE
		final Date date = new Date( new java.util.Date().getTime() );
		Update update = new UpdateImpl<>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Zoo_People.class);
		update.where().eq("birthdate", date); //$NON-NLS-1$
		update.where().eq("deathdate", date); //$NON-NLS-1$
		update.set().eq("id", 1); //$NON-NLS-1$

		final String methodOneRendering = update.renderSql();


		// SAME QUERY WITH OLD ONLINE WRITING
		Update update2 = new UpdateImpl<>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Zoo_People.class);
		update2.where().eq("birthdate", date).eq("deathdate", date);
		update2.set().eq("id", 1);
		final String oldOnlineMethodWriting = update2.renderSql();

		System.out.println("Method one query    : " + methodOneRendering);
		System.out.println("online writing query: " + oldOnlineMethodWriting);

		assertEquals(methodOneRendering, oldOnlineMethodWriting);

		// SAME QUERY WITH ONLINE WRITING
		Update update3 = new UpdateImpl<>(new H2DBProfile(), getClassDescriptorMap(), new PropertiesFactory(), Zoo_People.class);

		update3.where().eq("birthdate", date).eq("deathdate", date);
		update3.set().eq("id", 1);
		final String onlineMethodWriting = update3.renderSql();

		System.out.println("Method one query    : " + methodOneRendering); //$NON-NLS-1$
		System.out.println("online writing query: " + onlineMethodWriting); //$NON-NLS-1$

		assertEquals(methodOneRendering, onlineMethodWriting);
	}

}
