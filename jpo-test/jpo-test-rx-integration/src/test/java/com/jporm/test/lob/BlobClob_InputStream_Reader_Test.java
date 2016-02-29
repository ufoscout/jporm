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
 * ****************************************************************************
 */
package com.jporm.test.lob;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Date;

import org.junit.Test;

import com.jporm.commons.core.util.OrmUtil;
import com.jporm.sql.dialect.DBType;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.Blobclob_Stream;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class BlobClob_InputStream_Reader_Test extends BaseTestAllDB {

    public BlobClob_InputStream_Reader_Test(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testCrudBlobclob() throws IOException {

        if (getTestData().getDBType().equals(DBType.DERBY)) {
            return;
        }
        if (DBType.POSTGRESQL.equals(getTestData().getDBType())) {
            getLogger().info("Skip Test. Postgresql doesn't support this kind of data");
            return;
        }

        transaction(session -> {

            long id = new Date().getTime();

            final String text1 = "BINARY STRING TEST 1 " + id; //$NON-NLS-1$
            final InputStream is1 = OrmUtil.stringToStream(text1, OrmUtil.UTF8);

            final String text2 = "BINARY STRING TEST 2 " + id; //$NON-NLS-1$
            final Reader reader2 = OrmUtil.stringToReader(text2);

            Blobclob_Stream blobclob_ = new Blobclob_Stream();
            blobclob_.setBlobInputStream(is1);
            blobclob_.setClobReader(reader2);

            return session.save(blobclob_).thenCompose(blobclob -> {
                try {
                    reader2.close();
                    is1.close();

                    System.out.println("Blobclob saved with id: " + blobclob.getId()); //$NON-NLS-1$
                    assertFalse(id == blobclob.getId());

                    // LOAD
                    return session.findById(Blobclob_Stream.class, blobclob.getId()).fetch().thenCompose(blobclobLoad1 -> {

                        assertNotNull(blobclobLoad1);
                        assertEquals(blobclob.getId(), blobclobLoad1.getId());

                        final String retrieved1 = OrmUtil.streamToString(blobclobLoad1.getBlobInputStream(), OrmUtil.UTF8, false);
                        System.out.println("Retrieved1 String " + retrieved1); //$NON-NLS-1$
                        assertEquals(text1, retrieved1);

                        final String retrieved2 = OrmUtil.readerToString(blobclobLoad1.getClobReader(), false);
                        System.out.println("Retrieved2 String " + retrieved2); //$NON-NLS-1$
                        assertEquals(text2, retrieved2);

                        return session.delete(blobclobLoad1);
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

    }

}
