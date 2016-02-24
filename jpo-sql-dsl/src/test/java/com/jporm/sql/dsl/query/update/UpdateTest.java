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
package com.jporm.sql.dsl.query.update;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jporm.sql.dsl.BaseSqlTestApi;

/**
 *
 * @author Francesco Cina
 *
 *         23/giu/2011
 */
public class UpdateTest extends BaseSqlTestApi {

    @Test
    public void testOnlineSqlWriting() {
        // METHOD ONE
        final Date date = new Date(new java.util.Date().getTime());
        Update update = dsl().update("ZOO.PEOPLE");
        update
        .set("id", 1)
        .where()
            .eq("birthdate", date)
            .eq("deathdate", date);

        final String methodOneRendering = update.sqlQuery();

        // SAME QUERY WITH OLD ONLINE WRITING
        Update update2 = dsl().update("ZOO.PEOPLE");
        update2.where().eq("birthdate", date).eq("deathdate", date);
        update2.set("id", 1);
        final String oldOnlineMethodWriting = update2.sqlQuery();

        getLogger().info("Method one query    : " + methodOneRendering);
        getLogger().info("online writing query: " + oldOnlineMethodWriting);

        assertEquals(methodOneRendering, oldOnlineMethodWriting);

        // SAME QUERY WITH ONLINE WRITING
        Update update3 = dsl().update("ZOO.PEOPLE");

        update3.where().eq("birthdate", date).eq("deathdate", date);
        update3.set("id", 1);
        final String onlineMethodWriting = update3.sqlQuery();

        getLogger().info("Method one query    : " + methodOneRendering); //$NON-NLS-1$
        getLogger().info("online writing query: " + onlineMethodWriting); //$NON-NLS-1$

        assertEquals(methodOneRendering, onlineMethodWriting);
    }

    @Test
    public void testUpdate1() {

        Update update = dsl().update("EMPLOYEE");
        update.set("age", "12");
        update.where().eq("id", 1);
        final String expectedSql = "UPDATE EMPLOYEE SET AGE = ? WHERE ID = ? "; //$NON-NLS-1$
        assertEquals(expectedSql, update.sqlQuery().toUpperCase());

        final List<Object> values = new ArrayList<Object>();
        update.sqlValues(values);

        assertEquals(2, values.size());

        assertEquals("12", values.get(0)); //$NON-NLS-1$
        assertEquals(Integer.valueOf(1), values.get(1));

    }

    @Test
    public void testUpdate2() {

        final Date date = new Date(new java.util.Date().getTime());
        Update update = dsl().update("ZOO.PEOPLE");
        update.set("birthdate", date); //$NON-NLS-1$
        update.set("deathdate", date); //$NON-NLS-1$
        update.where().eq("id", 1); //$NON-NLS-1$
        final String expectedSql = "UPDATE ZOO.PEOPLE SET BIRTHDATE = ? , DEATHDATE = ? WHERE ID = ? "; //$NON-NLS-1$
        assertEquals(expectedSql, update.sqlQuery().toUpperCase());

        final List<Object> values = new ArrayList<Object>();
        update.sqlValues(values);

        assertEquals(3, values.size());

        assertEquals(date, values.get(0));
        assertEquals(date, values.get(1));
        assertEquals(Integer.valueOf(1), values.get(2));

    }

}
