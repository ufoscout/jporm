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
package com.jporm.sql.query.clause;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.core.domain.People;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.H2DBProfile;
import com.jporm.sql.query.clause.Insert;
import com.jporm.sql.query.clause.impl.InsertImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;

public class InsertTest  extends BaseSqlTestApi {

	@Test
	public void testSaveQuerySintax() {

		final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);
		save.values()
		.eq("id", "idValue")
		.eq("employeeNumber", "employeeNumberValue")
		.eq("name", null);

		System.out.println(save.renderSql(new H2DBProfile()));
		final String expectedSql = "INSERT INTO EMPLOYEE (ID, EMPLOYEE_NUMBER, NAME) VALUES (?, ?, ?) ";
		assertEquals(expectedSql , save.renderSql(new H2DBProfile()));

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(3, values.size());

		assertEquals( "idValue" , values.get(0)); //$NON-NLS-1$
		assertEquals( "employeeNumberValue" , values.get(1));
		assertNull( values.get(2));

	}

	@Test
	public void testSaveQuerySintaxWithGenerators() {

		final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class);

		save.values()
		.eq("firstname", "firstnameValue");

		System.out.println(save.renderSql(new H2DBProfile()));
		final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (SEQ_PEOPLE.nextval, ?) ";
		assertEquals(expectedSql , save.renderSql(new H2DBProfile()));

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(1, values.size());

		assertEquals( "firstnameValue" , values.get(0)); //$NON-NLS-1$

	}

	@Test
	public void testSaveQuerySintaxWithGeneratorsOverride() {

		final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class);

		save.values()
		.eq("id", "idValue")
		.eq("firstname", "firstnameValue");

		System.out.println(save.renderSql(new H2DBProfile()));
		final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (SEQ_PEOPLE.nextval, ?) ";
		assertEquals(expectedSql , save.renderSql(new H2DBProfile()));

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(1, values.size());

		assertEquals( "firstnameValue" , values.get(0)); //$NON-NLS-1$

	}

	@Test
	public void testSaveQuerySintaxWithoutGenerators() {

		final Insert save = new InsertImpl<>(getClassDescriptorMap(), new PropertiesFactory(), People.class);

		save.useGenerators(false);

		save.values()
		.eq("id", "idValue")
		.eq("firstname", "firstnameValue");

		System.out.println(save.renderSql(new H2DBProfile()));
		final String expectedSql = "INSERT INTO PEOPLE (ID, FIRSTNAME) VALUES (?, ?) ";
		assertEquals(expectedSql , save.renderSql(new H2DBProfile()));

		final List<Object> values = new ArrayList<Object>();
		save.appendValues(values);

		assertEquals(2, values.size());

		assertEquals( "idValue" , values.get(0));
		assertEquals( "firstnameValue" , values.get(1));
	}

}
