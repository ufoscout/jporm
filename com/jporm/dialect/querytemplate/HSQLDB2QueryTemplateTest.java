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
package com.jporm.dialect.querytemplate;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.UUID;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.dialect.querytemplate.HSQLDB2QueryTemplate;
import com.jporm.dialect.querytemplate.QueryTemplate;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 16, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class HSQLDB2QueryTemplateTest extends BaseTestApi {

    private QueryTemplate queryTemplate = new HSQLDB2QueryTemplate();

    @Test
    public void testInsertQuerySequence() {
        assertEquals( "NEXT VALUE FOR sequence" , queryTemplate.insertQuerySequence("sequence") );
    }

    @Test
    public void testPaginateNegativeParameters() {
        int firstRow = -1;
        int maxRows = -1;
        assertEquals("sql", queryTemplate.paginateSQL("sql", firstRow, maxRows));
    }

    @Test
    public void testPaginateMaxRows() {
        int firstRow = -1;
        int maxRows = new Random().nextInt(1000) + 1;
        String sql = UUID.randomUUID().toString();
        String expectedSql = sql + "LIMIT " + maxRows + " ";
        assertEquals(expectedSql, queryTemplate.paginateSQL(sql, firstRow, maxRows));
    }

    @Test
    public void testPaginateFirstRow() {
        int firstRow = new Random().nextInt(1000);
        int maxRows = 0;
        String sql = UUID.randomUUID().toString();
        String expectedSql = sql + "OFFSET " + firstRow + " ROWS ";
        assertEquals(expectedSql, queryTemplate.paginateSQL(sql, firstRow, maxRows));
    }

    @Test
    public void testPaginateBetween() {
        int firstRow = new Random().nextInt(1000);
        int maxRows = new Random().nextInt(1000) + 1;
        String sql = UUID.randomUUID().toString();
        String expectedSql = sql + "LIMIT " + maxRows + " OFFSET " + firstRow + " ";
        assertEquals(expectedSql, queryTemplate.paginateSQL(sql, firstRow, maxRows));
    }

}
