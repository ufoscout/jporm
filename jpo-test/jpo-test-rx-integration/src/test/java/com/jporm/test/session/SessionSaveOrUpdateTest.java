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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section05.AutoIdInteger;
import com.jporm.test.domain.section06.DataVersionWithoutGenerator;

import io.reactivex.Single;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class SessionSaveOrUpdateTest extends BaseTestAllDB {

	public SessionSaveOrUpdateTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testSaveOrUpdateObjectWithVersionWithoutGenerator() throws InterruptedException, ExecutionException {
		transaction((Session session) -> {
			DataVersionWithoutGenerator bean = new DataVersionWithoutGenerator();
			final int id = 1000;
			bean.setId(id);

			session.delete(bean).blockingGet();

			bean = session.saveOrUpdate(bean).blockingGet();

			assertEquals(0l, session.findById(DataVersionWithoutGenerator.class, id).fetchOneUnique().blockingGet().getVersion());

			bean = session.saveOrUpdate(bean).blockingGet();

			assertEquals(1l, session.findById(DataVersionWithoutGenerator.class, id).fetchOneUnique().blockingGet().getVersion());
			return Single.just("");
		});

	}

	@Test
	public void testSaveOrUpdateWithConditionGenerator() throws InterruptedException, ExecutionException {
		transaction((Session session) -> {
			final AutoId autoId = new AutoId();
			final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
			autoId.setValue(value);

			final AutoId savedAutoId = session.saveOrUpdate(autoId).blockingGet();
			final Integer newId = savedAutoId.getId();
			assertNotNull(newId);

			final AutoId foundAutoId = session.findById(AutoId.class, newId).fetchOneUnique().blockingGet();
			assertEquals(value, foundAutoId.getValue());
			final String newValue = "new value for test " + new Date().getTime();
			foundAutoId.setValue(newValue);

			final AutoId updatedAutoId = session.saveOrUpdate(foundAutoId).blockingGet();

			assertEquals(newId, updatedAutoId.getId());
			assertEquals(newValue, session.findById(AutoId.class, newId).fetchOneUnique().blockingGet().getValue());
			return Single.just("");
		});

	}

	@Test
	public void testSaveOrUpdateWithNotConditionGenerator() throws InterruptedException, ExecutionException {
		transaction((Session session) -> {
			AutoIdInteger autoId = new AutoIdInteger();
			final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
			autoId.setValue(value);

			final Integer oldId = autoId.getId();

			autoId = session.saveOrUpdate(autoId).blockingGet();
			final Integer newId = autoId.getId();

			assertFalse(newId.equals(oldId));
			assertEquals(value, session.findById(AutoId.class, newId).fetchOneUnique().blockingGet().getValue());

			final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
			autoId.setValue(newValue);

			autoId = session.saveOrUpdate(autoId).blockingGet();

			assertEquals(newId, autoId.getId());
			assertEquals(newValue, session.findById(AutoId.class, newId).fetchOneUnique().blockingGet().getValue());
			return Single.just("");
		});

	}

	@Test
	public void testSaveOrUpdateWithoutGenerator() throws InterruptedException, ExecutionException {
		transaction((Session session) -> {
			final int id = new Random().nextInt(Integer.MAX_VALUE);
			Employee employee = new Employee();
			employee.setId(id);
			employee.setAge(44);
			employee.setEmployeeNumber(Optional.of("empNumber" + id)); //$NON-NLS-1$
			employee.setName("oldName"); //$NON-NLS-1$
			employee.setSurname("Cina"); //$NON-NLS-1$

			// CREATE
			employee = session.save(employee).blockingGet();

			assertEquals("oldName", session.findById(Employee.class, id).fetchOneUnique().blockingGet().getName()); //$NON-NLS-1$

			employee.setName("newName"); //$NON-NLS-1$

			employee = session.saveOrUpdate(employee).blockingGet();

			assertEquals("newName", session.findById(Employee.class, id).fetchOneUnique().blockingGet().getName());
			return Single.just("");
		});

	}
}
