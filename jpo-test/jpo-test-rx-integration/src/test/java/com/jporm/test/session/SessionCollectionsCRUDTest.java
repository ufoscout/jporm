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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.jporm.core.JPO;
import com.jporm.core.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class SessionCollectionsCRUDTest extends BaseTestAllDB {

	public SessionCollectionsCRUDTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testCreateDeleteCollection() {
		final JPO jpOrm =getJPO();

		// CREATE
		final Session conn = jpOrm.session();

		conn.txVoidNow((_session) -> {
			List<AutoId> entries = new ArrayList<>();
			entries.add(new AutoId());
			entries.add(new AutoId());
			entries.add(new AutoId());
			entries.add(new AutoId());
			entries = conn.save(entries);

			entries.forEach(entry -> assertTrue(_session.find(entry).getRowCount()>0));

			assertEquals( entries.size(), _session.delete(entries) );

			entries.forEach(entry -> assertFalse(_session.find(entry).getRowCount()>0));
		});

	}

	@Test
	public void testCreateUpdateCollection() {
		final JPO jpOrm =getJPO();

		// CREATE
		final Session conn = jpOrm.session();

		conn.txVoidNow((_session) -> {
			List<AutoId> entries = new ArrayList<>();
			entries.add(new AutoId());
			entries.add(new AutoId());
			entries.add(new AutoId());
			entries.add(new AutoId());

			String value1 = UUID.randomUUID().toString();
			entries.forEach(entry -> entry.setValue(value1));

			entries = conn.save(entries);
			entries.forEach(entry -> assertEquals(value1, _session.find(entry).getUnique().getValue()));

			String value2 = UUID.randomUUID().toString();
			entries.forEach(entry -> entry.setValue(value2));
			entries = conn.update(entries);

			entries.forEach(entry -> assertEquals(value2, _session.find(entry).getUnique().getValue()));

		});

	}


}
