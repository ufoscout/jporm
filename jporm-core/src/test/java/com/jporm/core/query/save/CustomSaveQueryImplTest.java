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
package com.jporm.core.query.save;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.core.query.save.CustomSaveQuery;
import com.jporm.core.session.Session;

public class CustomSaveQueryImplTest extends BaseTestApi {

	@Test
	public void testSaveQuerySintax() {

		final Session session =  getJPO().session();

		final CustomSaveQuery save = session.saveQuery(Employee.class);

		save.values()
		.eq("id", "idValue")
		.eq("employeeNumber", "employeeNumberValue")
		.eq("name", null);

		System.out.println(save.renderSql());
		final String expectedSql = "INSERT INTO EMPLOYEE (ID, EMPLOYEE_NUMBER, NAME) VALUES (?, ?, ?) ";
		assertEquals(expectedSql , save.renderSql());

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(3, values.size());

		assertEquals( "idValue" , values.get(0)); //$NON-NLS-1$
		assertEquals( "employeeNumberValue" , values.get(1));
		assertNull( values.get(2));

	}

	@Test
	public void testSaveQuerySintaxWithGenerators() {

		final Session session =  getJPO().session();

		final CustomSaveQuery save = session.saveQuery(People.class);

		save.values()
		.eq("firstname", "firstnameValue");

		System.out.println(save.renderSql());
		final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (SEQ_PEOPLE.nextval, ?) ";
		assertEquals(expectedSql , save.renderSql());

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(1, values.size());

		assertEquals( "firstnameValue" , values.get(0)); //$NON-NLS-1$

	}

	@Test
	public void testSaveQuerySintaxWithGeneratorsOverride() {

		final Session session =  getJPO().session();

		final CustomSaveQuery save = session.saveQuery(People.class);

		save.values()
		.eq("id", "idValue")
		.eq("firstname", "firstnameValue");

		System.out.println(save.renderSql());
		final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (SEQ_PEOPLE.nextval, ?) ";
		assertEquals(expectedSql , save.renderSql());

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(1, values.size());

		assertEquals( "firstnameValue" , values.get(0)); //$NON-NLS-1$

	}

	@Test
	public void testSaveQuerySintaxWithoutGenerators() {

		final Session session =  getJPO().session();

		final CustomSaveQuery save = session.saveQuery(People.class);
		save.useGenerators(false);

		save.values()
		.eq("id", "idValue")
		.eq("firstname", "firstnameValue");

		System.out.println(save.renderSql());
		final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (?, ?) ";
		assertEquals(expectedSql , save.renderSql());

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(2, values.size());

		assertEquals( "idValue" , values.get(0));
		assertEquals( "firstnameValue" , values.get(1));
	}

}
