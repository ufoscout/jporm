/**
 * *****************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *****************************************************************************
 */
package com.jporm.test.lob;

import java.util.Date;

import org.junit.Test;

import com.jporm.sql.dialect.DBType;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.Blobclob_String;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class BlobClob_String_Test extends BaseTestAllDB {

    public BlobClob_String_Test(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testCrudBlobclob() {

        if (DBType.POSTGRESQL.equals(getTestData().getDBType())) {
            getLogger().info("Skip Test. Postgresql doesn't support this kind of data");
            return;
        }

        long id = new Date().getTime();
        final String text1 = "BINARY STRING TEST 1 " + id; //$NON-NLS-1$
        final String text2 = "BINARY STRING TEST 2 " + id; //$NON-NLS-1$

        transaction(session -> {

            Blobclob_String blobclob_ = new Blobclob_String();
            blobclob_.setBlobField(text1.getBytes());
            blobclob_.setClobField(text2);
            return session.save(blobclob_).thenCompose(blobclob -> {
                getLogger().info("Blobclob saved with id: " + blobclob.getId()); //$NON-NLS-1$
                threadAssertFalse(id == blobclob.getId());
                long newId = blobclob.getId();

                return session.findById(Blobclob_String.class, newId).fetchOne().thenCompose(blobclobLoad1 -> {

                    // threadAssertNotNull(blobclobLoad1);
                    threadAssertEquals(blobclob.getId(), blobclobLoad1.getId());

                    final String retrieved1 = new String(blobclobLoad1.getBlobField());
                    System.out.println("Retrieved1 String " + retrieved1); //$NON-NLS-1$
                    threadAssertEquals(text1, retrieved1);

                    final String retrieved2 = blobclobLoad1.getClobField();
                    System.out.println("Retrieved2 String " + retrieved2); //$NON-NLS-1$
                    threadAssertEquals(text2, retrieved2);

                    return session.delete(blobclobLoad1);
                });
            });
        });

    }

}
