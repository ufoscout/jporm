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
package com.jporm.sql.query.clause;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.core.domain.Zoo_People;
import com.jporm.sql.BaseSqlTestApi;
import com.jporm.sql.dialect.H2DBProfile;
import com.jporm.sql.query.clause.Delete;
import com.jporm.sql.query.clause.impl.DeleteImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;

/**
 *
 * @author Francesco Cina
 *
 * 23/giu/2011
 */
public class DeleteTest extends BaseSqlTestApi {

	@Test
	public void testUpdate1() {

		Delete delete = new DeleteImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Employee.class);

		delete.where().eq("id", 1); //$NON-NLS-1$
		System.out.println(delete.renderSql(new H2DBProfile()));
		final String expectedSql = "DELETE FROM EMPLOYEE WHERE ID = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , delete.renderSql(new H2DBProfile()));

		final List<Object> values = new ArrayList<Object>();
		delete.appendValues(values);

		assertEquals(1, values.size());

		assertEquals( Integer.valueOf(1) , values.get(0));

	}

	@Test
	public void testUpdate2() {

		Delete delete = new DeleteImpl<>(getClassDescriptorMap(), new PropertiesFactory(), Zoo_People.class);

		final Date date = new Date( new java.util.Date().getTime() );
		delete.where().eq("id", 1);
		delete.where().eq("birthdate", date);
		delete.where().eq("deathdate", date); //$NON-NLS-1$
		System.out.println(delete.renderSql(new H2DBProfile()));
		final String expectedSql = "DELETE FROM ZOO.PEOPLE WHERE ID = ? AND BIRTHDATE = ? AND DEATHDATE = ? "; //$NON-NLS-1$
		assertEquals(expectedSql , delete.renderSql(new H2DBProfile()));

		final List<Object> values = new ArrayList<Object>();
		delete.appendValues(values);

		assertEquals(3, values.size());

		assertEquals( Integer.valueOf(1) , values.get(0));
		assertEquals( date , values.get(1));
		assertEquals( date , values.get(2));

	}

}
