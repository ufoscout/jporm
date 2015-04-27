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
package com.jporm.test.session;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.jporm.rm.JPO;
import com.jporm.rm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section05.AutoIdInteger;
import com.jporm.test.domain.section06.DataVersionWithoutGenerator;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class SessionSaveOrUpdateTest extends BaseTestAllDB {

	public SessionSaveOrUpdateTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testSaveOrUpdateWithConditionGenerator() {
		final JPO jpOrm =getJPO();

		final Session conn = jpOrm.session();
		conn.txVoidNow((_session) -> {
			AutoId autoId = new AutoId();
			final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
			autoId.setValue(value);

			autoId = conn.saveOrUpdate(autoId);
			final Integer newId = autoId.getId();

			assertNotNull(newId);
			assertEquals(value, conn.find(AutoId.class, newId).fetchUnique().getValue());

			final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
			autoId.setValue(newValue);

			autoId = conn.saveOrUpdate(autoId);

			assertEquals(newId, autoId.getId());
			assertEquals(newValue, conn.find(AutoId.class, newId).fetchUnique().getValue());
		});

	}

	@Test
	public void testSaveOrUpdateWithNotConditionGenerator() {
		final JPO jpOrm =getJPO();
		final Session conn = jpOrm.session();
		conn.txVoidNow((_session) -> {
			AutoIdInteger autoId = new AutoIdInteger();
			final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
			autoId.setValue(value);

			final Integer oldId = autoId.getId();

			autoId = conn.saveOrUpdate(autoId);
			Integer newId = autoId.getId();

			assertNotSame(oldId, newId);
			assertEquals(value, conn.find(AutoId.class, newId).fetchUnique().getValue());

			final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
			autoId.setValue(newValue);

			autoId = conn.saveOrUpdate(autoId);

			assertEquals(newId, autoId.getId());
			assertEquals(newValue, conn.find(AutoId.class, newId).fetchUnique().getValue());
		});

	}

	@Test
	public void testSaveOrUpdateWithoutGenerator() {
		final JPO jpOrm =getJPO();
		final Session conn = jpOrm.session();
		conn.txVoidNow((_session) -> {
			final int id = new Random().nextInt(Integer.MAX_VALUE);
			Employee employee = new Employee();
			employee.setId( id );
			employee.setAge( 44 );
			employee.setEmployeeNumber( "empNumber" + id ); //$NON-NLS-1$
			employee.setName("oldName"); //$NON-NLS-1$
			employee.setSurname("Cina"); //$NON-NLS-1$

			// CREATE
			employee = conn.save(employee);

			assertEquals("oldName", conn.find(Employee.class, id).fetchUnique().getName()); //$NON-NLS-1$

			employee.setName("newName"); //$NON-NLS-1$

			employee = conn.saveOrUpdate(employee);

			assertEquals("newName", conn.find(Employee.class, id).fetchUnique().getName()); //$NON-NLS-1$
		});
	}

	@Test
	public void testSaveOrUpdateObjectWithVersionWithoutGenerator() {
		final JPO jpOrm = getJPO();

		// CREATE
		final Session conn = jpOrm.session();
		conn.txVoidNow((_session) -> {
			conn.deleteQuery(DataVersionWithoutGenerator.class).execute();

			DataVersionWithoutGenerator bean = new DataVersionWithoutGenerator();
			int id = 1000;
			bean.setId(id);

			bean = conn.saveOrUpdate(bean);

			assertEquals(0, conn.find(DataVersionWithoutGenerator.class, id).fetchUnique().getVersion());

			bean = conn.saveOrUpdate(bean);

			assertEquals(1, conn.find(DataVersionWithoutGenerator.class, id).fetchUnique().getVersion());

		});

	}
}
