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


import org.junit.Test;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section06.DataVersionInteger;
import com.jporm.test.domain.section06.DataVersionLong;
import com.jporm.test.domain.section06.DataVersionSqlDate;

/**
 *
 * @author cinafr
 *
 */
public class VersionTest extends BaseTestAllDB {

	public VersionTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	@Test
	public void testLongNewRecordVersion() throws Throwable {

		transaction((session) -> {
			DataVersionLong dataVersion = new DataVersionLong();
			dataVersion.setData("dataVersion1"); //$NON-NLS-1$
			return session.save(dataVersion)
			.thenCompose(savedDataVersion -> {
				final long currentVersion = savedDataVersion.getVersion();
				assertEquals(0l, currentVersion);
				return session.update(savedDataVersion)
				.thenCompose(savedDataVersion2 -> {
					assertEquals(currentVersion+1 , savedDataVersion2.getVersion());
					return session.update(savedDataVersion2)
					.thenApply(savedDataVersion3 -> {
						assertEquals(currentVersion+2 , savedDataVersion3.getVersion());
						return savedDataVersion3;
					});
				});
			});
		});

	}

	@Test
	public void testLongNewRecordVersionWithCustomVersionNumber() throws Throwable {

		transaction((session) -> {
			DataVersionLong dataVersion = new DataVersionLong();
			dataVersion.setData("dataVersion1"); //$NON-NLS-1$
			dataVersion.setVersion(1000);
			return session.save(dataVersion)
			.thenCompose(savedDataVersion -> {
				final long currentVersion = savedDataVersion.getVersion();
				assertEquals(0l, currentVersion);
				return session.update(savedDataVersion)
				.thenCompose(savedDataVersion2 -> {
					assertEquals(currentVersion+1 , savedDataVersion2.getVersion());
					return session.update(savedDataVersion2)
					.thenApply(savedDataVersion3 -> {
						assertEquals(currentVersion+2 , savedDataVersion3.getVersion());
						return savedDataVersion3;
					});
				});
			});
		});

	}

	@Test
	public void testLongWrongVersionNumber() throws Throwable {
		transaction(true, (session) -> {
			DataVersionLong dataVersion = new DataVersionLong();
			dataVersion.setData("dataVersion1"); //$NON-NLS-1$
			dataVersion.setVersion(1000);
			return session.save(dataVersion)
			.thenCompose(savedDataVersion -> {
				final long currentVersion = savedDataVersion.getVersion();
				assertEquals(0l, currentVersion);
				return session.update(savedDataVersion)
				.thenCompose(savedDataVersion2 -> {
					assertEquals(currentVersion+1 , savedDataVersion2.getVersion());
					savedDataVersion2.setVersion(1000);
					return session.update(savedDataVersion2);
				});
			});
		});
	}


	@Test(expected=JpoException.class)
	public void testSqlDateNewRecordVersion() {
			getJPO().session().findById(DataVersionSqlDate.class, "");
			fail("A OrmConfigurationException should be thrwon before because the java.sql.Date() type is not a valid type for the @Version annotation"); //$NON-NLS-1$
	}

	@Test
	public void testIntegerNewRecordVersion() {

		transaction(true, (session) -> {
			DataVersionInteger dataVersion = new DataVersionInteger();
			dataVersion.setData("dataVersion1"); //$NON-NLS-1$
			return session.save(dataVersion)
			.thenCompose(savedDataVersion -> {
				final Integer currentVersion = savedDataVersion.getVersion();
				assertEquals(0, currentVersion.intValue());
				return session.update(savedDataVersion)
				.thenCompose(savedDataVersion2 -> {
					assertEquals(currentVersion+1 , savedDataVersion2.getVersion().intValue());
					savedDataVersion2.setVersion(1000);
					return session.update(savedDataVersion2);
				});
			});
		});

	}

}
