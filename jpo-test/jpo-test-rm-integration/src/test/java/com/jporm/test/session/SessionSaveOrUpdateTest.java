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
package com.jporm.test.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Date;
import java.util.Random;

import org.junit.Test;

import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.sql.dialect.DBType;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.test.domain.section05.AutoId;
import com.jporm.test.domain.section05.AutoIdInteger;
import com.jporm.test.domain.section06.DataVersionWithoutGenerator;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class SessionSaveOrUpdateTest extends BaseTestAllDB {

    public SessionSaveOrUpdateTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    @Test
    public void testSaveOrUpdateObjectWithVersionWithoutGenerator() {
        //SQL Server does not manual insert of automatically generated field
        if (isDBType(DBType.SQLSERVER2008)) {
            return;
        }
        final JpoRm jpOrm = getJPO();

        // CREATE
        final Session conn = jpOrm.session();
        jpOrm.tx().execute((_session) -> {
            conn.delete(DataVersionWithoutGenerator.class).execute();

            DataVersionWithoutGenerator bean = new DataVersionWithoutGenerator();
            int id = 1000;
            bean.setId(id);

            bean = conn.saveOrUpdate(bean);

            assertEquals(0, conn.findById(DataVersionWithoutGenerator.class, id).fetchOneUnique().getVersion());

            bean = conn.saveOrUpdate(bean);

            assertEquals(1, conn.findById(DataVersionWithoutGenerator.class, id).fetchOneUnique().getVersion());

        });

    }

    @Test
    public void testSaveOrUpdateWithConditionGenerator() {
        final JpoRm jpOrm = getJPO();

        final Session conn = jpOrm.session();
        jpOrm.tx().execute((_session) -> {
            AutoId autoId = new AutoId();
            final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
            autoId.setValue(value);

            autoId = conn.saveOrUpdate(autoId);
            final Integer newId = autoId.getId();

            assertNotNull(newId);
            assertEquals(value, conn.findById(AutoId.class, newId).fetchOneUnique().getValue());

            final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
            autoId.setValue(newValue);

            autoId = conn.saveOrUpdate(autoId);

            assertEquals(newId, autoId.getId());
            assertEquals(newValue, conn.findById(AutoId.class, newId).fetchOneUnique().getValue());
        });

    }

    @Test
    public void testSaveOrUpdateWithNotConditionGenerator() {
        final JpoRm jpOrm = getJPO();
        final Session conn = jpOrm.session();
        jpOrm.tx().execute((_session) -> {
            AutoIdInteger autoId = new AutoIdInteger();
            final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
            autoId.setValue(value);

            final Integer oldId = autoId.getId();

            autoId = conn.saveOrUpdate(autoId);
            Integer newId = autoId.getId();

            assertNotSame(oldId, newId);
            assertEquals(value, conn.findById(AutoId.class, newId).fetchOneUnique().getValue());

            final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
            autoId.setValue(newValue);

            autoId = conn.saveOrUpdate(autoId);

            assertEquals(newId, autoId.getId());
            assertEquals(newValue, conn.findById(AutoId.class, newId).fetchOneUnique().getValue());
        });

    }

    @Test
    public void testSaveOrUpdateWithoutGenerator() {
        final JpoRm jpOrm = getJPO();
        final Session conn = jpOrm.session();
        jpOrm.tx().execute((_session) -> {
            final int id = new Random().nextInt(Integer.MAX_VALUE);
            Employee employee = new Employee();
            employee.setId(id);
            employee.setAge(44);
            employee.setEmployeeNumber("empNumber" + id); //$NON-NLS-1$
            employee.setName("oldName"); //$NON-NLS-1$
            employee.setSurname("Cina"); //$NON-NLS-1$

            // CREATE
            employee = conn.save(employee);

            assertEquals("oldName", conn.findById(Employee.class, id).fetchOneUnique().getName()); //$NON-NLS-1$

            employee.setName("newName"); //$NON-NLS-1$

            employee = conn.saveOrUpdate(employee);

            assertEquals("newName", conn.findById(Employee.class, id).fetchOneUnique().getName()); //$NON-NLS-1$
        });
    }
}
