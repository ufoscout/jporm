/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package com.jporm.test.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;

import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.json.TableJson;
import com.jporm.test.domain.section01.Employee;

public class JsonCRUDTest  extends BaseTestAllDB {

	public JsonCRUDTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCRUDWithJson() throws Exception {

		getJPO().tx(session -> {

			final Employee employee = new Employee();
			employee.setId(new Random().nextInt());
			employee.setEmployeeNumber(Optional.of(UUID.randomUUID().toString()));

			final TableJson beanWithJson = new TableJson();
			beanWithJson.setJson(employee);

			final TableJson savedBean = session.save(beanWithJson);

			assertNotNull(savedBean);
			assertTrue(savedBean.getId()>=0);
			assertNotNull(savedBean.getJson());
			assertEquals(employee.getId(), savedBean.getJson().getId());
			assertEquals(employee.getEmployeeNumber(), savedBean.getJson().getEmployeeNumber());
			assertNotSame(employee, savedBean.getJson());

			final TableJson foundBean = session.findByModelId(savedBean).fetchOne();
			assertEquals(savedBean.getId(), foundBean.getId());
			assertNotNull(foundBean.getJson());
			assertEquals(employee.getId(), foundBean.getJson().getId());
			assertEquals(employee.getEmployeeNumber(), foundBean.getJson().getEmployeeNumber());

		});

	}

	@Test
	public void testCRUDWithNullJson() throws Exception {

		getJPO().tx(session -> {

			final TableJson beanWithJson = new TableJson();
			final TableJson savedBean = session.save(beanWithJson);

			assertNotNull(savedBean);
			assertTrue(savedBean.getId()>=0);
			assertNull(savedBean.getJson());

			final TableJson foundBean = session.findByModelId(savedBean).fetchOne();
			assertEquals(savedBean.getId(), foundBean.getId());
			assertNull(foundBean.getJson());

		});
	}

}
