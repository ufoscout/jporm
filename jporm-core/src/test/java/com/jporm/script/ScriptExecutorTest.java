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
package com.jporm.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.JPO;
import com.jporm.JPOrm;
import com.jporm.domain.section01.TempTable;
import com.jporm.query.find.FindQuery;
import com.jporm.script.ScriptExecutor;
import com.jporm.session.Session;
import com.jporm.session.datasource.DataSourceSessionProvider;
import com.jporm.transaction.Transaction;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 */
public class ScriptExecutorTest extends BaseTestApi {

    private String filename;

    @Before
    public void setUp() {

        filename = getTestInputBasePath() + "/StreamParserTest_1.sql"; //$NON-NLS-1$
        assertTrue( new File(filename).exists() );
    }

    @Test
    public void testScript() throws Exception {
        JPO jpo = new JPOrm( new DataSourceSessionProvider( getH2DataSource() ) );
        executeScript( jpo );
        verifyData( jpo );
    }

    private void executeScript(final JPO jpOrm) throws Exception {

        final Session session = jpOrm.session();
        final Transaction tx = session.transaction();
        final ScriptExecutor scriptExecutor = session.scriptExecutor();

        final InputStream scriptStream = new FileInputStream(filename);
        scriptExecutor.execute(scriptStream);

        tx.commit();

    }

    private void verifyData(final JPO jpOrm) {
        jpOrm.register(TempTable.class);

        final Session session = jpOrm.session();
        final FindQuery<TempTable> query = session.findQuery(TempTable.class, "TempTable"); //$NON-NLS-1$
        query.orderBy().asc("TempTable.id"); //$NON-NLS-1$
        final List<TempTable> result = query.getList();

        getLogger().info("result.size() = " + result.size()); //$NON-NLS-1$

        for ( int i=0 ; i<result.size() ; i++) {
            final TempTable temp = result.get(i);
            getLogger().info("Found element id: " + temp.getId() + " - name: " + temp.getName()); //$NON-NLS-1$ //$NON-NLS-2$
        }

        final List<String> expectedResult = new ArrayList<String>();
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

        assertEquals( expectedResult.size(), result.size() );

        for ( int i=0 ; i<result.size() ; i++) {
            final TempTable temp = result.get(i);
            getLogger().info("check element id: " + temp.getId() + " - name: " + temp.getName()); //$NON-NLS-1$ //$NON-NLS-2$
            assertEquals( expectedResult.get(i) , temp.getName());
        }

    }

}
