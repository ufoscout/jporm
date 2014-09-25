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
package com.jporm.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.jporm.JPO;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section05.AutoIdInteger;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class AutoIdTest extends BaseTestAllDB {

	public AutoIdTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testAutoId() {
		final JPO jpOrm = getJPOrm();

		jpOrm.register(AutoId.class);

		final Session conn = jpOrm.session();
		AutoId autoId = conn.doInTransaction((_session) -> {
			// CREATE
			AutoId autoId2 = new AutoId();
			autoId2.setValue("value for test " + new Date().getTime() ); //$NON-NLS-1$
			return conn.save(autoId2);
		});

		System.out.println("autoId id: " + autoId.getId()); //$NON-NLS-1$
		assertTrue( autoId.getId() > 0 );

		AutoId autoIdLoad1 = conn.doInTransaction((_session) -> {
			// LOAD
			AutoId autoIdLoad2 = conn.find(AutoId.class, autoId.getId() ).get();
			assertNotNull(autoIdLoad2);
			assertEquals( autoId.getId(), autoIdLoad2.getId() );
			assertEquals( autoId.getValue(), autoIdLoad2.getValue() );

			//UPDATE
			autoIdLoad2.setValue("new Value " + new Date().getTime() ); //$NON-NLS-1$
			return conn.update(autoIdLoad2);
		});

		// LOAD
		final AutoId autoIdLoad2 = conn.find(AutoId.class, autoId.getId() ).get();
		assertNotNull(autoIdLoad2);
		assertEquals( autoIdLoad1.getId(), autoIdLoad2.getId() );
		assertEquals( autoIdLoad1.getValue(), autoIdLoad2.getValue() );

		conn.doInTransactionVoid((_session) -> {
			//DELETE
			conn.delete(autoIdLoad2);
		});

		final AutoId autoIdLoad3 = conn.find(AutoId.class, autoId.getId() ).get();
		assertNull(autoIdLoad3);

	}

	@Test
	public void testAutoIdInteger() {
		final JPO jpOrm = getJPOrm();

		jpOrm.register(AutoIdInteger.class);

		// CREATE
		final Session conn = jpOrm.session();
		AutoIdInteger autoId = conn.doInTransaction((_session) -> {
			AutoIdInteger autoId1 = new AutoIdInteger();
			autoId1.setValue("value for test " + new Date().getTime() ); //$NON-NLS-1$
			return conn.save(autoId1);
		});

		System.out.println("autoId id: " + autoId.getId()); //$NON-NLS-1$
		assertTrue( autoId.getId() > 0 );

		// LOAD
		AutoIdInteger autoIdLoad1 = conn.find(AutoIdInteger.class, autoId.getId() ).get();
		assertNotNull(autoIdLoad1);
		assertEquals( autoId.getId(), autoIdLoad1.getId() );
		assertEquals( autoId.getValue(), autoIdLoad1.getValue() );

		//UPDATE
		AutoIdInteger autoIdLoad2 = conn.doInTransaction((_session) -> {
			autoIdLoad1.setValue("new Value " + new Date().getTime() ); //$NON-NLS-1$
			return conn.update(autoIdLoad1);
		});

		// LOAD
		final AutoIdInteger autoIdLoad3 = conn.find(AutoIdInteger.class, autoId.getId() ).get();
		assertNotNull(autoIdLoad3);
		assertEquals( autoIdLoad2.getId(), autoIdLoad3.getId() );
		assertEquals( autoIdLoad2.getValue(), autoIdLoad3.getValue() );

		//DELETE
		conn.doInTransactionVoid((_session) -> {
			conn.delete(autoIdLoad3);
		});

		final AutoIdInteger autoIdLoad4 = conn.find(AutoIdInteger.class, autoId.getId() ).get();
		assertNull(autoIdLoad4);

	}

}
