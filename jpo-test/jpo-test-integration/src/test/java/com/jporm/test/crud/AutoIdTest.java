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

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.jporm.core.JPO;
import com.jporm.core.session.Session;
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
		final JPO jpOrm = getJPO();

		final Session conn = jpOrm.session();
		AutoId autoId = conn.txNow((_session) -> {
			// CREATE
			AutoId autoId2 = new AutoId();
			autoId2.setValue("value for test " + new Date().getTime() ); //$NON-NLS-1$
			return conn.save(autoId2);
		});

		System.out.println("autoId id: " + autoId.getId()); //$NON-NLS-1$
		assertTrue( autoId.getId() > -1 );

		AutoId autoIdLoad1 = conn.txNow((_session) -> {
			// LOAD
			AutoId autoIdLoad2 = conn.find(AutoId.class, autoId.getId() ).fetchUnique();
			assertNotNull(autoIdLoad2);
			assertEquals( autoId.getId(), autoIdLoad2.getId() );
			assertEquals( autoId.getValue(), autoIdLoad2.getValue() );

			//UPDATE
			autoIdLoad2.setValue("new Value " + new Date().getTime() ); //$NON-NLS-1$
			return conn.update(autoIdLoad2);
		});

		// LOAD
		final AutoId autoIdLoad2 = conn.find(AutoId.class, autoId.getId() ).fetchUnique();
		assertNotNull(autoIdLoad2);
		assertEquals( autoIdLoad1.getId(), autoIdLoad2.getId() );
		assertEquals( autoIdLoad1.getValue(), autoIdLoad2.getValue() );

		conn.txVoidNow((_session) -> {
			//DELETE
			conn.delete(autoIdLoad2);
		});

		assertFalse(conn.find(AutoId.class, autoId.getId() ).fetchOptional().isPresent());

	}

	@Test
	public void testAutoIdInteger() {
		final JPO jpOrm = getJPO();

		// CREATE
		final Session conn = jpOrm.session();
		AutoIdInteger autoId = conn.txNow((_session) -> {
			AutoIdInteger autoId1 = new AutoIdInteger();
			autoId1.setValue("value for test " + new Date().getTime() ); //$NON-NLS-1$
			return conn.save(autoId1);
		});

		System.out.println("autoId id: " + autoId.getId()); //$NON-NLS-1$
		assertTrue( autoId.getId() > 0 );

		// LOAD
		AutoIdInteger autoIdLoad1 = conn.find(AutoIdInteger.class, autoId.getId() ).fetchUnique();
		assertNotNull(autoIdLoad1);
		assertEquals( autoId.getId(), autoIdLoad1.getId() );
		assertEquals( autoId.getValue(), autoIdLoad1.getValue() );

		//UPDATE
		AutoIdInteger autoIdLoad2 = conn.txNow((_session) -> {
			autoIdLoad1.setValue("new Value " + new Date().getTime() ); //$NON-NLS-1$
			return conn.update(autoIdLoad1);
		});

		// LOAD
		final AutoIdInteger autoIdLoad3 = conn.find(AutoIdInteger.class, autoId.getId() ).fetchUnique();
		assertNotNull(autoIdLoad3);
		assertEquals( autoIdLoad2.getId(), autoIdLoad3.getId() );
		assertEquals( autoIdLoad2.getValue(), autoIdLoad3.getValue() );

		//DELETE
		conn.txVoidNow((_session) -> {
			conn.delete(autoIdLoad3);
		});

		assertFalse(conn.find(AutoIdInteger.class, autoId.getId() ).fetchOptional().isPresent());

	}

}
