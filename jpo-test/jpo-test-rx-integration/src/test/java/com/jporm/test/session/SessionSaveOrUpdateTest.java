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

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.jporm.rx.session.Session;
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
    public void testSaveOrUpdateObjectWithVersionWithoutGenerator() throws InterruptedException, ExecutionException {
        Session session = getJPO().session();

        DataVersionWithoutGenerator bean = new DataVersionWithoutGenerator();
        int id = 1000;
        bean.setId(id);

        session.delete(bean).get();

        bean = session.saveOrUpdate(bean).get();

        assertEquals(0, session.findById(DataVersionWithoutGenerator.class, id).fetchUnique().get().getVersion());

        bean = session.saveOrUpdate(bean).get();

        assertEquals(1, session.findById(DataVersionWithoutGenerator.class, id).fetchUnique().get().getVersion());

    }

    @Test
    public void testSaveOrUpdateWithConditionGenerator() throws InterruptedException, ExecutionException {
        Session session = getJPO().session();
        AutoId autoId = new AutoId();
        final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
        autoId.setValue(value);

        AutoId savedAutoId = session.saveOrUpdate(autoId).get();
        final Integer newId = savedAutoId.getId();
        assertNotNull(newId);

        AutoId foundAutoId = session.findById(AutoId.class, newId).fetchUnique().get();
        assertEquals(value, foundAutoId.getValue());
        final String newValue = "new value for test " + new Date().getTime();
        foundAutoId.setValue(newValue);

        AutoId updatedAutoId = session.saveOrUpdate(foundAutoId).get();

        assertEquals(newId, updatedAutoId.getId());
        assertEquals(newValue, session.findById(AutoId.class, newId).fetchUnique().get().getValue());

    }

    @Test
    public void testSaveOrUpdateWithNotConditionGenerator() throws InterruptedException, ExecutionException {
        Session session = getJPO().session();
        AutoIdInteger autoId = new AutoIdInteger();
        final String value = "value for test " + new Date().getTime(); //$NON-NLS-1$
        autoId.setValue(value);

        final Integer oldId = autoId.getId();

        autoId = session.saveOrUpdate(autoId).get();
        Integer newId = autoId.getId();

        assertNotSame(oldId, newId);
        assertEquals(value, session.findById(AutoId.class, newId).fetchUnique().get().getValue());

        final String newValue = "new value for test " + new Date().getTime(); //$NON-NLS-1$
        autoId.setValue(newValue);

        autoId = session.saveOrUpdate(autoId).get();

        assertEquals(newId, autoId.getId());
        assertEquals(newValue, session.findById(AutoId.class, newId).fetchUnique().get().getValue());
    }

    @Test
    public void testSaveOrUpdateWithoutGenerator() throws InterruptedException, ExecutionException {
        Session session = getJPO().session();
        final int id = new Random().nextInt(Integer.MAX_VALUE);
        Employee employee = new Employee();
        employee.setId(id);
        employee.setAge(44);
        employee.setEmployeeNumber("empNumber" + id); //$NON-NLS-1$
        employee.setName("oldName"); //$NON-NLS-1$
        employee.setSurname("Cina"); //$NON-NLS-1$

        // CREATE
        employee = session.save(employee).get();

        assertEquals("oldName", session.findById(Employee.class, id).fetchUnique().get().getName()); //$NON-NLS-1$

        employee.setName("newName"); //$NON-NLS-1$

        employee = session.saveOrUpdate(employee).get();

        assertEquals("newName", session.findById(Employee.class, id).fetchUnique().get().getName()); //$NON-NLS-1$
    }
}
