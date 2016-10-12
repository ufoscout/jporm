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
package com.jporm.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.jporm.rx.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section05.AutoId;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class AutoIdTest extends BaseTestAllDB {

    public AutoIdTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testAutoId() {

        transaction((Session txSession) -> {
            // CREATE
            AutoId autoId2 = new AutoId();
            autoId2.setValue("value for test " + new Date().getTime()); //$NON-NLS-1$
            getLogger().info("created with value: {}", autoId2.getValue()); //$NON-NLS-1$

            return txSession.save(autoId2).flatMap(autoId -> {

                getLogger().info("autoId id: {}", autoId.getId()); //$NON-NLS-1$
                assertTrue(autoId.getId() > -1);

                // LOAD
                return txSession.findById(AutoId.class, autoId.getId()).fetchOneUnique().flatMap(autoIdLoad1 -> {

                    assertNotNull(autoIdLoad1);
                    assertEquals(autoId.getId(), autoIdLoad1.getId());
                    assertEquals(autoId.getValue(), autoIdLoad1.getValue());

                    // UPDATE
                    autoIdLoad1.setValue("new Value " + new Date().getTime()); //$NON-NLS-1$
                    getLogger().info("updated with value: {}", autoIdLoad1.getValue()); //$NON-NLS-1$
                    return txSession.update(autoIdLoad1).flatMap(updated1 -> {

                        getLogger().info("value after update: {}", autoIdLoad1.getValue()); //$NON-NLS-1$

                        // LOAD
                        return txSession.findById(AutoId.class, updated1.getId()).fetchOneUnique().flatMap(loaded2 -> {
                            getLogger().info("loaded with value: {}", loaded2.getValue()); //$NON-NLS-1$
                            assertNotNull(loaded2);
                            assertEquals(updated1.getId(), loaded2.getId());
                            assertEquals(updated1.getValue(), loaded2.getValue());

                            // DELETE
                            return txSession.delete(updated1).flatMap(deleteResult -> {
                                assertTrue(deleteResult.deleted() > 0);

                                // LOAD
                                return txSession.findById(AutoId.class, updated1.getId()).fetchOneOptional().map(loadedOptional -> {
                                    getLogger().info("Is it present after delete? {} ", loadedOptional.isPresent()); //$NON-NLS-1$
                                    assertFalse(loadedOptional.isPresent());
                                    return loadedOptional;
                                });

                            });

                        });
                    });
                });
            });
        });

    }

}
