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
import com.jporm.transaction.Transaction;

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

		AutoId autoId = new AutoId();
		autoId.setValue("value for test " + new Date().getTime() ); //$NON-NLS-1$

		// CREATE
		final Session conn = jpOrm.session();
		Transaction tx = conn.transaction();
		autoId = conn.save(autoId);
		tx.commit();


		System.out.println("autoId id: " + autoId.getId()); //$NON-NLS-1$
		assertTrue( autoId.getId() > 0 );

		// LOAD
		AutoId autoIdLoad1 = conn.find(AutoId.class, autoId.getId() ).get();
		assertNotNull(autoIdLoad1);
		assertEquals( autoId.getId(), autoIdLoad1.getId() );
		assertEquals( autoId.getValue(), autoIdLoad1.getValue() );

		//UPDATE
		tx = conn.transaction();
		autoIdLoad1.setValue("new Value " + new Date().getTime() ); //$NON-NLS-1$
		autoIdLoad1 = conn.update(autoIdLoad1);
		tx.commit();

		// LOAD
		final AutoId autoIdLoad2 = conn.find(AutoId.class, autoId.getId() ).get();
		assertNotNull(autoIdLoad2);
		assertEquals( autoIdLoad1.getId(), autoIdLoad2.getId() );
		assertEquals( autoIdLoad1.getValue(), autoIdLoad2.getValue() );

		//DELETE
		tx = conn.transaction();
		conn.delete(autoIdLoad2);
		tx.commit();
		final AutoId autoIdLoad3 = conn.find(AutoId.class, autoId.getId() ).get();
		assertNull(autoIdLoad3);

	}

	@Test
	public void testAutoIdInteger() {
		final JPO jpOrm = getJPOrm();

		jpOrm.register(AutoIdInteger.class);

		AutoIdInteger autoId = new AutoIdInteger();
		autoId.setValue("value for test " + new Date().getTime() ); //$NON-NLS-1$

		// CREATE
		final Session conn = jpOrm.session();
		Transaction tx = conn.transaction();
		autoId = conn.save(autoId);
		tx.commit();


		System.out.println("autoId id: " + autoId.getId()); //$NON-NLS-1$
		assertTrue( autoId.getId() > 0 );

		// LOAD
		AutoIdInteger autoIdLoad1 = conn.find(AutoIdInteger.class, autoId.getId() ).get();
		assertNotNull(autoIdLoad1);
		assertEquals( autoId.getId(), autoIdLoad1.getId() );
		assertEquals( autoId.getValue(), autoIdLoad1.getValue() );

		//UPDATE
		tx = conn.transaction();
		autoIdLoad1.setValue("new Value " + new Date().getTime() ); //$NON-NLS-1$
		autoIdLoad1 = conn.update(autoIdLoad1);
		tx.commit();

		// LOAD
		final AutoIdInteger autoIdLoad2 = conn.find(AutoIdInteger.class, autoId.getId() ).get();
		assertNotNull(autoIdLoad2);
		assertEquals( autoIdLoad1.getId(), autoIdLoad2.getId() );
		assertEquals( autoIdLoad1.getValue(), autoIdLoad2.getValue() );

		//DELETE
		tx = conn.transaction();
		conn.delete(autoIdLoad2);
		tx.commit();
		final AutoIdInteger autoIdLoad3 = conn.find(AutoIdInteger.class, autoId.getId() ).get();
		assertNull(autoIdLoad3);

	}

}
