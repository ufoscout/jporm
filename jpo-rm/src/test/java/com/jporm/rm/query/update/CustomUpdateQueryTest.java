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
package com.jporm.rm.query.update;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.jporm.core.domain.Employee;
import com.jporm.core.domain.Zoo_People;
import com.jporm.rm.BaseTestApi;
import com.jporm.rm.JpoRm;
import com.jporm.rm.JpoRmBuilder;
import com.jporm.rm.connection.NullTransactionProvider;

/**
 *
 * @author Francesco Cina
 *
 *         23/giu/2011
 */
public class CustomUpdateQueryTest extends BaseTestApi {

    private JpoRm jpOrm;

    @Before
    public void setUp() {
        jpOrm = JpoRmBuilder.get().build(new NullTransactionProvider());
    }

    @Test
    public void testOnlineSqlWriting() {
        JpoRmBuilder.get().build(new NullTransactionProvider()).tx(nullSession -> {

            // METHOD ONE
            final Date date = new Date(new java.util.Date().getTime());
            final CustomUpdateQuery update = nullSession.update(Zoo_People.class);
            update.where().eq("birthdate", date); //$NON-NLS-1$
            update.where().eq("deathdate", date); //$NON-NLS-1$
            update.set("id", 1); //$NON-NLS-1$

            final String methodOneRendering = update.sqlQuery();

            // SAME QUERY WITH OLD ONLINE WRITING
            final String oldOnlineMethodWriting = nullSession.update(Zoo_People.class).set("id", 1) //$NON-NLS-1$
                    .where().eq("birthdate", date).eq("deathdate", date).sqlQuery();

            getLogger().info("Method one query    : " + methodOneRendering); //$NON-NLS-1$
            getLogger().info("online writing query: " + oldOnlineMethodWriting); //$NON-NLS-1$

            assertEquals(methodOneRendering, oldOnlineMethodWriting);

            // SAME QUERY WITH ONLINE WRITING
            final String onlineMethodWriting = nullSession.update(Zoo_People.class).set("id", 1).where().eq("birthdate", date).eq("deathdate", date).sqlQuery();

            getLogger().info("Method one query    : " + methodOneRendering); //$NON-NLS-1$
            getLogger().info("online writing query: " + onlineMethodWriting); //$NON-NLS-1$

            assertEquals(methodOneRendering, onlineMethodWriting);
        });
    }

    @Test
    public void testUpdate1() {

        jpOrm.tx(session -> {

            final CustomUpdateQuery update = session.update(Employee.class);
            update.set("age", "12"); //$NON-NLS-1$ //$NON-NLS-2$
            update.where().eq("id", 1); //$NON-NLS-1$
            getLogger().info(update.sqlQuery());
            final String expectedSql = "UPDATE EMPLOYEE SET AGE = ? WHERE ID = ? "; //$NON-NLS-1$
            assertEquals(expectedSql, update.sqlQuery());

            final List<Object> values = new ArrayList<>();
            update.sqlValues(values);

            assertEquals(2, values.size());

            assertEquals("12", values.get(0)); //$NON-NLS-1$
            assertEquals(Integer.valueOf(1), values.get(1));
        });

    }

    @Test
    public void testUpdate2() {

        jpOrm.tx(session -> {

            final Date date = new Date(new java.util.Date().getTime());
            final CustomUpdateQuery update = session.update(Zoo_People.class);
            update.set("birthdate", date); //$NON-NLS-1$
            update.set("deathdate", date); //$NON-NLS-1$
            update.where().eq("id", 1); //$NON-NLS-1$
            getLogger().info(update.sqlQuery());
            final String expectedSql = "UPDATE ZOO.PEOPLE SET BIRTHDATE = ? , DEATHDATE = ? WHERE ID = ? "; //$NON-NLS-1$
            assertEquals(expectedSql, update.sqlQuery());

            final List<Object> values = new ArrayList<>();
            update.sqlValues(values);

            assertEquals(3, values.size());

            assertEquals(date, values.get(0));
            assertEquals(date, values.get(1));
            assertEquals(Integer.valueOf(1), values.get(2));
        });
    }

    @Test
    public void testUpdateSetNull() {

        jpOrm.tx(session -> {

            final CustomUpdateQuery update = session.update(Employee.class);
            update.set("employeeNumber", null);
            update.set("employeeNumber", UUID.randomUUID().toString());

            // This is to check that a NullPointerException is not thrown
            // see: https://github.com/ufoscout/jporm/issues/86
            assertTrue(update.execute() >= 0);
        });
    }
}
