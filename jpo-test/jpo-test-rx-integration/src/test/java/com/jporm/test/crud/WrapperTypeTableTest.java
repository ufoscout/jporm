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
package com.jporm.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.sql.dialect.DBType;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section07.WrapperTypeTable;

/**
 *
 * @author Francesco Cina'
 *
 *         Apr 17, 2012
 */
public class WrapperTypeTableTest extends BaseTestAllDB {

    public WrapperTypeTableTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testCrudWithWrapperType() {
        // Mysql timestamp doesn't store millis
        if (getTestData().getDBType().equals(DBType.MYSQL)) {
            return;
        }

        transaction((Session session) -> {

            LocalDate endDate = LocalDate.now();
            LocalDateTime startDate = LocalDateTime.now();
            final Date now = new Date();

            WrapperTypeTable wrapper = new WrapperTypeTable();
            wrapper.setNow(now);
            wrapper.setEndDate(endDate);
            wrapper.setStartDate(startDate);

            assertEquals(-1l, wrapper.getId().longValue());

            return session.save(wrapper).flatMap(wrapper1 -> {

                System.out.println("wrapper1 id: " + wrapper1.getId()); //$NON-NLS-1$
                assertTrue(wrapper1.getId() >= Long.valueOf(0));

                return session.findById(WrapperTypeTable.class, wrapper1.getId()).fetchOneUnique().flatMap(wrapperLoad1 -> {

                    assertEquals(wrapper1.getId(), wrapperLoad1.getId());
                    assertNull(wrapperLoad1.getValid());
                    assertTrue(now.equals(wrapperLoad1.getNow()));
                    assertEquals(startDate, wrapperLoad1.getStartDate());
                    assertEquals(endDate, wrapperLoad1.getEndDate());

                    // UPDATE
                    LocalDate newEndDate = LocalDate.now();
                    LocalDateTime newStartDate = LocalDateTime.now();
                    final boolean valid = true;

                    wrapperLoad1.setEndDate(newEndDate);
                    wrapperLoad1.setStartDate(newStartDate);
                    wrapperLoad1.setValid(valid);
                    return session.update(wrapperLoad1).flatMap(uploaded -> {
                        return session.findById(WrapperTypeTable.class, wrapperLoad1.getId()).fetchOneUnique().flatMap(wrapperLoad2 -> {
                            assertNotNull(wrapperLoad2);
                            assertEquals(wrapperLoad1.getId(), wrapperLoad2.getId());
                            assertEquals(valid, wrapperLoad2.getValid());
                            assertEquals(newStartDate, wrapperLoad2.getStartDate());
                            assertEquals(newEndDate, wrapperLoad2.getEndDate());
                            assertTrue(now.equals(wrapperLoad2.getNow()));
                            return session.delete(wrapperLoad2);
                        });
                    });

                });

            });

        });

    }

    @Test
    public void testQueryWithWrapperType() {
        // Mysql timestamp doesn't store millis
        if (getTestData().getDBType().equals(DBType.MYSQL)) {
            return;
        }
        transaction((Session session) -> {

            LocalDate endDate = LocalDate.now();
            LocalDateTime startDate = LocalDateTime.now();
            final Date now = new Date();

            WrapperTypeTable wrapper = new WrapperTypeTable();
            wrapper.setNow(now);
            wrapper.setEndDate(endDate);
            wrapper.setStartDate(startDate);

            assertEquals(-1l, wrapper.getId().longValue());

            return session.save(wrapper).flatMap(wrapper1 -> {

                return session.find(WrapperTypeTable.class).where().eq("startDate", startDate).eq("now", now).eq("endDate", endDate).fetchOneUnique()
                        .flatMap(wrapperLoad1 -> {

                    assertNotNull(wrapperLoad1);
                    assertEquals(wrapper1.getId(), wrapperLoad1.getId());
                    assertNull(wrapperLoad1.getValid());
                    assertTrue(now.equals(wrapperLoad1.getNow()));
                    assertEquals(startDate, wrapperLoad1.getStartDate());
                    assertEquals(endDate, wrapperLoad1.getEndDate());

                    return session.delete(wrapperLoad1);
                });

            });

        });

    }

}
