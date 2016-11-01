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

import org.junit.Test;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoOptimisticLockException;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section06.DataVersionInteger;
import com.jporm.test.domain.section06.DataVersionLong;
import com.jporm.test.domain.section06.DataVersionSqlDate;
import com.jporm.test.domain.section06.DataVersionTimestamp;

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
    public void testIntegerNewRecordVersion() {
        getJPO().tx().execute((session) -> {
            DataVersionInteger dataVersion = new DataVersionInteger();
            dataVersion.setData("dataVersion1"); //$NON-NLS-1$
            assertNull(dataVersion.getVersion());

            dataVersion = session.save(dataVersion);
            final Integer currentVersion = dataVersion.getVersion();
            assertEquals(Integer.valueOf(0), currentVersion);

            dataVersion = session.update(dataVersion);
            assertEquals(Integer.valueOf(currentVersion + 1), dataVersion.getVersion());

            boolean wrongVersion = false;
            try {
                dataVersion.setVersion(1000);
                dataVersion = session.update(dataVersion);
            } catch (final JpoOptimisticLockException e) {
                e.printStackTrace();
                wrongVersion = true;
            }
            assertTrue(wrongVersion);
        });
    }

    @Test
    public void testLongNewRecordVersion() {

        getJPO().tx().execute((session) -> {
            DataVersionLong dataVersion = new DataVersionLong();
            dataVersion.setData("dataVersion1"); //$NON-NLS-1$

            dataVersion = session.save(dataVersion);
            final long currentVersion = dataVersion.getVersion();
            assertEquals(0l, currentVersion);

            dataVersion = session.update(dataVersion);
            assertEquals(currentVersion + 1, dataVersion.getVersion());

            dataVersion = session.update(dataVersion);
            assertEquals(currentVersion + 2, dataVersion.getVersion());
        });

    }

    @Test
    public void testLongNewRecordVersionWithCustomVersionNumber() {

        getJPO().tx().execute((session) -> {
            DataVersionLong dataVersion = new DataVersionLong();
            dataVersion.setData("dataVersion1"); //$NON-NLS-1$
            dataVersion.setVersion(1000);

            dataVersion = session.save(dataVersion);
            final long currentVersion = dataVersion.getVersion();
            assertEquals(0l, currentVersion);

            dataVersion = session.update(dataVersion);
            assertEquals(currentVersion + 1, dataVersion.getVersion());

            dataVersion = session.update(dataVersion);
            assertEquals(currentVersion + 2, dataVersion.getVersion());
        });
    }

    @Test
    public void testLongWrongVersionNumber() {
        getJPO().tx().execute((session) -> {
            DataVersionLong dataVersion = new DataVersionLong();
            dataVersion.setData("dataVersion1"); //$NON-NLS-1$
            dataVersion.setVersion(1000);

            dataVersion = session.save(dataVersion);
            final long currentVersion = dataVersion.getVersion();
            assertEquals(0l, currentVersion);

            dataVersion = session.update(dataVersion);
            assertEquals(currentVersion + 1, dataVersion.getVersion());

            boolean wrongVersion = false;
            try {
                dataVersion.setVersion(1000);
                dataVersion = session.update(dataVersion);
            } catch (final JpoOptimisticLockException e) {
                e.printStackTrace();
                wrongVersion = true;
            }
            assertTrue(wrongVersion);
        });
    }

    @Test(expected = JpoException.class)
    public void testSqlDateNewRecordVersion() {
        getJPO().session().findById(DataVersionSqlDate.class, "");
        fail("A OrmConfigurationException should be thrwon before because the java.sql.Date() type is not a valid type for the @Version annotation"); //$NON-NLS-1$
    }

    @Test(expected = JpoException.class)
    public void testTimestampNewRecordVersion() {
        getJPO().session().findById(DataVersionTimestamp.class, "");
        fail("A OrmConfigurationException should be thrwon before because the java.sql.Date() type is not a valid type for the @Version annotation"); //$NON-NLS-1$
    }

}
