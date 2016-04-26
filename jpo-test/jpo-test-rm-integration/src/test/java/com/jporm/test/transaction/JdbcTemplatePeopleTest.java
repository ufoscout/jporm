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
package com.jporm.test.transaction;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;

import com.jporm.rm.JpoRm;
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
public class JdbcTemplatePeopleTest extends BaseTestAllDB {

    public JdbcTemplatePeopleTest(final String testName, final TestData testData) {
        super(testName, testData);
    }

    private long create(final Session session) {

        final long id = new Date().getTime();
        People people = new People();
        people.setId(id);
        people.setFirstname("people"); //$NON-NLS-1$
        people.setLastname("Wizard"); //$NON-NLS-1$

        // CREATE
        people = session.save(people);

        System.out.println("People saved with id: " + people.getId()); //$NON-NLS-1$
        assertTrue(id == people.getId());
        return people.getId();

    }

    private int delete(final Session session, final People people) {
        // DELETE
        return session.delete(people);
    }

    private People load(final Session session, final long id) {
        // LOAD
        final Optional<People> peopleLoad1 = session.findById(People.class, id).fetchOneOptional();
        if (peopleLoad1.isPresent()) {
            return peopleLoad1.get();
        }
        return null;
    }

    @Test
    public void testJdbcTemplateTransaction1() {
        final JpoRm jpOrm = getJPO();

        final long id = jpOrm.transaction().execute(session -> {
            return create(session);
        });

        final People loaded1 = load(jpOrm.session(), id);
        assertNotNull(loaded1);

        try {
            jpOrm.transaction().execute((Session session) -> {
                delete(session, loaded1);
                throw new RuntimeException();
            });
        } catch (RuntimeException e) {
            // ok!
        }

        final People loaded2 = load(jpOrm.session(), id);
        assertNotNull(loaded2);

        try {
            jpOrm.transaction().execute((Session session) -> {
                delete(session, loaded2);
                throw new RuntimeException();
            });
        } catch (RuntimeException e) {
            // ok!
        }

        final People loaded3 = load(jpOrm.session(), id);
        assertNotNull(loaded3);

        jpOrm.transaction().execute(session -> {
            delete(session, loaded3);
        });

        final People loaded4 = load(jpOrm.session(), id);
        assertNull(loaded4);
    }

}
