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
package com.jporm.rm.kotlin.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.core.domain.TempTable;
import com.jporm.rm.kotlin.BaseTestApi;
import com.jporm.rm.kotlin.JpoRm;
import com.jporm.rm.kotlin.query.find.CustomFindQuery;
import com.jporm.rm.kotlin.session.ScriptExecutor;

/**
 *
 * @author Francesco Cina
 *
 *         02/lug/2011
 */
public class ScriptExecutorTest extends BaseTestApi {

    private String filename;

    private void executeScript(final JpoRm jpOrm) throws Exception {

        jpOrm.tx().execute(session -> {
            final ScriptExecutor scriptExecutor = session.scriptExecutor();

            try (InputStream scriptStream = new FileInputStream(filename)) {
                scriptExecutor.execute(scriptStream);
            } catch (JpoException | IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        });

    }

    @Before
    public void setUp() {

        filename = getTestInputBasePath() + "/StreamParserTest_1.sql"; //$NON-NLS-1$
        assertTrue(new File(filename).exists());
    }

    @Test
    public void testScript() throws Exception {
        JpoRm jpo = getJPO();
        executeScript(jpo);
        verifyData(jpo);
    }

    private void verifyData(final JpoRm jpOrm) {

        jpOrm.tx(session -> {
            final CustomFindQuery<TempTable> query = session.find(TempTable.class, "TempTable"); //$NON-NLS-1$
            query.orderBy().asc("TempTable.id"); //$NON-NLS-1$
            final List<TempTable> result = query.fetchAll();

            getLogger().info("result.size() = " + result.size()); //$NON-NLS-1$

            for (int i = 0; i < result.size(); i++) {
                final TempTable temp = result.get(i);
                getLogger().info("Found element id: " + temp.getId() + " - name: " + temp.getName()); //$NON-NLS-1$ //$NON-NLS-2$
            }

            final List<String> expectedResult = new ArrayList<>();
            expectedResult.add("one"); //$NON-NLS-1$
            expectedResult.add("two"); //$NON-NLS-1$
            expectedResult.add("three"); //$NON-NLS-1$
            expectedResult.add("four;"); //$NON-NLS-1$
            expectedResult.add("f'ive;"); //$NON-NLS-1$
            expectedResult.add("s'ix;"); //$NON-NLS-1$
            expectedResult.add("seven';{--ix;"); //$NON-NLS-1$
            expectedResult.add("height';{--ix;"); //$NON-NLS-1$
            expectedResult.add("ni';ne';{--ix;"); //$NON-NLS-1$
            expectedResult.add("ten';{--ix;"); //$NON-NLS-1$
            expectedResult.add("e'le;{--ven;"); //$NON-NLS-1$

            assertEquals(expectedResult.size(), result.size());

            for (int i = 0; i < result.size(); i++) {
                final TempTable temp = result.get(i);
                getLogger().info("check element id: " + temp.getId() + " - name: " + temp.getName()); //$NON-NLS-1$ //$NON-NLS-2$
                assertEquals(expectedResult.get(i), temp.getName());
            };
        });

    }

}
