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
package com.jporm.test.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.jporm.JPO;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.exception.OrmOptimisticLockException;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section06.DataVersionInteger;
import com.jporm.test.domain.section06.DataVersionLong;
import com.jporm.test.domain.section06.DataVersionSqlDate;
import com.jporm.transaction.Transaction;

/**
 * 
 * @author cinafr
 *
 */
public class VersionTest extends BaseTestAllDB {

	public VersionTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private Session session;

	@Before
	public void setUp() {
		final JPO jpOrm = getJPOrm();

		jpOrm.register(DataVersionInteger.class);
		jpOrm.register(DataVersionLong.class);

		session = jpOrm.session();
	}

	@Test
	public void testLongNewRecordVersion() {

		DataVersionLong dataVersion = new DataVersionLong();
		dataVersion.setData("dataVersion1"); //$NON-NLS-1$

		Transaction tx = session.transaction();
		dataVersion = session.save(dataVersion);
		final long currentVersion = dataVersion.getVersion();
		assertEquals(0l, currentVersion);
		tx.commit();

		tx = session.transaction();
		dataVersion = session.update(dataVersion);
		assertEquals(currentVersion+1 , dataVersion.getVersion());
		tx.commit();

		tx = session.transaction();
		dataVersion = session.update(dataVersion);
		assertEquals(currentVersion+2 , dataVersion.getVersion());
		tx.commit();
	}

	@Test
	public void testLongNewRecordVersionWithCustomVersionNumber() {

		DataVersionLong dataVersion = new DataVersionLong();
		dataVersion.setData("dataVersion1"); //$NON-NLS-1$
		dataVersion.setVersion(1000);

		Transaction tx = session.transaction();
		dataVersion = session.save(dataVersion);
		final long currentVersion = dataVersion.getVersion();
		assertEquals(0l, currentVersion);
		tx.commit();

		tx = session.transaction();
		dataVersion = session.update(dataVersion);
		assertEquals(currentVersion+1 , dataVersion.getVersion());
		tx.commit();

		tx = session.transaction();
		dataVersion = session.update(dataVersion);
		assertEquals(currentVersion+2 , dataVersion.getVersion());
		tx.commit();
	}

	@Test
	public void testLongWrongVersionNumber() {

		DataVersionLong dataVersion = new DataVersionLong();
		dataVersion.setData("dataVersion1"); //$NON-NLS-1$
		dataVersion.setVersion(1000);

		Transaction tx = session.transaction();
		dataVersion = session.save(dataVersion);
		final long currentVersion = dataVersion.getVersion();
		assertEquals(0l, currentVersion);
		tx.commit();

		tx = session.transaction();
		dataVersion = session.update(dataVersion);
		assertEquals(currentVersion+1 , dataVersion.getVersion());
		tx.commit();

		tx = session.transaction();
		boolean wrongVersion = false;
		try {
			dataVersion.setVersion(1000);
			dataVersion = session.update(dataVersion);
		} catch (final OrmOptimisticLockException e) {
			e.printStackTrace();
			wrongVersion = true;
		}
		assertTrue(wrongVersion);
		tx.commit();
	}

	@Test
	public void testSqlDateNewRecordVersion() {
		try {
			getJPOrm().register(DataVersionSqlDate.class);
			fail("A OrmConfigurationException should be thrwon before!!"); //$NON-NLS-1$
		} catch (OrmConfigurationException e) {
			// ok
		}
	}

	@Test
	public void testIntegerNewRecordVersion() {

		DataVersionInteger dataVersion = new DataVersionInteger();
		dataVersion.setData("dataVersion1"); //$NON-NLS-1$
		assertNull( dataVersion.getVersion() );

		Transaction tx = session.transaction();
		dataVersion = session.save(dataVersion);
		final Integer currentVersion = dataVersion.getVersion();
		assertEquals( Integer.valueOf(0), currentVersion);
		tx.commit();

		tx = session.transaction();
		dataVersion = session.update(dataVersion);
		assertEquals(Integer.valueOf(currentVersion+1) , dataVersion.getVersion());
		tx.commit();

		tx = session.transaction();
		boolean wrongVersion = false;
		try {
			dataVersion.setVersion(1000);
			dataVersion = session.update(dataVersion);
		} catch (final OrmOptimisticLockException e) {
			e.printStackTrace();
			wrongVersion = true;
		}
		assertTrue(wrongVersion);
		tx.commit();
	}

}
