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
package com.jporm.rm.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.rm.BaseTestApi;
import com.jporm.rm.session.script.Parser;
import com.jporm.rm.session.script.ParserCallback;
import com.jporm.rm.session.script.StreamParser;

/**
 * 
 * @author Francesco Cina
 *
 *         01/lug/2011
 */
public class StreamParserTest extends BaseTestApi {

    private String filename;

    @Before
    public void setUp() {
        filename = getTestInputBasePath() + "/StreamParserTest_1.sql"; //$NON-NLS-1$
        assertTrue(new File(filename).exists());
    }

    @Test
    public void testParser() throws Exception {

        final List<String> expectedList = new ArrayList<String>();

        expectedList.add("create table STRANGE_TABLE ()"); //$NON-NLS-1$
        expectedList.add("drop table STRANGE_TABLE"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (1, 'one')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (2, 'two')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (3, 'three')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (4, 'four;')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (5, 'f''ive;')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (6, 's''ix;')"); //$NON-NLS-1$
        expectedList.add("insert\ninto TEMP_TABLE (ID, NAME) values (7, 'seven'';{--ix;')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (8, 'height'';{--ix;')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (9, 'ni'';ne'';{--ix;')"); //$NON-NLS-1$
        expectedList.add("insert into TEMP_TABLE (ID, NAME) values (10, 'ten'';{--ix;')"); //$NON-NLS-1$
        expectedList.add(" insert\ninto TEMP_TABLE (ID, NAME) values (11, 'e''le;{--ven;')"); //$NON-NLS-1$

        final FileInputStream fis = new FileInputStream(filename);
        final Parser parser = new StreamParser(fis, true);
        final GenericWrapper<Integer> countWrapper = new GenericWrapper<Integer>(0);
        final ParserCallback parserCallback = new ParserCallback() {
            /**
             * 
             */

            @Override
            public void parseAction(final String text) {
                int count = countWrapper.getValue();
                System.out.println("------- BEGIN -------------"); //$NON-NLS-1$
                System.out.println("Received: " + text); //$NON-NLS-1$
                System.out.println("expected: " + expectedList.get(count)); //$NON-NLS-1$
                assertEquals(expectedList.get(count), text);
                System.out.println("------- END ---------------"); //$NON-NLS-1$
                countWrapper.setValue(++count);
            }
        };

        parser.parse(parserCallback);
    }
}
