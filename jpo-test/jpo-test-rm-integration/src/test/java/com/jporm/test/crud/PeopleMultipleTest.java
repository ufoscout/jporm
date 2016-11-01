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
package com.jporm.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.jporm.rm.JpoRm;
import com.jporm.rm.query.find.CustomFindQuery;
import com.jporm.rm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section02.People;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class PeopleMultipleTest extends BaseTestAllDB {

    public PeopleMultipleTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private void compare(final List<People> peoples, final List<People> peoplesLoad) {
        assertEquals(peoples.size(), peoplesLoad.size());
        for (final People people : peoples) {
            System.out.println("check people: " + people.getId()); //$NON-NLS-1$
            boolean found = false;
            for (final People peopleLoad : peoplesLoad) {
                if (peopleLoad.getId() == people.getId()) {
                    found = true;
                    assertEquals(people.getFirstname(), peopleLoad.getFirstname());
                    assertEquals(people.getLastname(), peopleLoad.getLastname());
                }
            }
            assertTrue(found);
        }
    }

    private People createPeople(final String firstname) {
        final long id = new Random().nextInt(Integer.MAX_VALUE);
        final People people = new People();
        people.setId(id);
        people.setFirstname(firstname);
        people.setLastname("lastname of " + firstname); //$NON-NLS-1$
        return people;
    }

    @Test
    public void testCrudPeopleMultiple() {
        final JpoRm jpOrm = getJPO();

        // CREATE
        final Session conn = jpOrm.session();

        List<People> peoplesSave = jpOrm.tx().execute((_session) -> {
            List<People> peoples_ = new ArrayList<People>();
            peoples_.add(createPeople("1")); //$NON-NLS-1$
            peoples_.add(createPeople("2")); //$NON-NLS-1$
            peoples_.add(createPeople("3")); //$NON-NLS-1$
            peoples_.add(createPeople("4")); //$NON-NLS-1$
            return conn.save(peoples_);
        });

        for (final People people : peoplesSave) {
            System.out.println("People [" + people.getFirstname() + "]saved with id: " + people.getId()); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // LOAD
        final CustomFindQuery<People> peopleQuery1 = conn.find(People.class);
        assertNotNull(peopleQuery1);
        final Collection<Long> values = new ArrayList<Long>();
        for (final People people : peoplesSave) {
            values.add(people.getId());
        }
        peopleQuery1.where().in("id", values); //$NON-NLS-1$
        final List<People> peopleLoad1 = peopleQuery1.fetchAll();

        compare(peoplesSave, peopleLoad1);

        List<People> peoplesUpdate = jpOrm.tx().execute((_session) -> {
            // UPDATE
            for (final People people : peoplesSave) {
                people.setFirstname(people.getFirstname() + "-updated-" + new Date().getTime()); //$NON-NLS-1$
            }
            return conn.update(peoplesSave);
        });

        // LOAD
        final CustomFindQuery<People> peopleQuery2 = conn.find(People.class);
        assertNotNull(peopleQuery2);
        peopleQuery2.where().in("id", values); //$NON-NLS-1$
        final List<People> peopleLoad2 = peopleQuery2.fetchAll();

        compare(peoplesUpdate, peopleLoad2);

        // DELETE
        jpOrm.tx().execute((_session) -> {
            conn.delete(peopleLoad2);
        });

        // LOAD
        final CustomFindQuery<People> peopleQuery3 = conn.find(People.class);
        assertNotNull(peopleQuery3);
        peopleQuery3.where().in("id", values); //$NON-NLS-1$
        final List<People> peopleLoad3 = peopleQuery3.fetchAll();
        assertTrue(peopleLoad3.isEmpty());

    }

}
