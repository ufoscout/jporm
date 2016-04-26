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
package com.jporm.rm.spring.transaction;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import org.junit.Test;

import com.jporm.commons.core.util.GenericWrapper;
import com.jporm.core.domain.People;
import com.jporm.rm.JpoRm;
import com.jporm.rm.session.Session;
import com.jporm.rm.spring.BaseTestJdbcTemplate;
import com.jporm.rm.spring.transactional.ITransactionalCode;
import com.jporm.rm.spring.transactional.ITransactionalExecutor;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class JdbcTemplatePeople2Test extends BaseTestJdbcTemplate {

    class Create implements ITransactionalCode {
        private final JpoRm jpOrm;
        private final GenericWrapper<People> peopleWrapper;

        Create(final JpoRm jpOrm, final GenericWrapper<People> peopleWrapper) {
            this.jpOrm = jpOrm;
            this.peopleWrapper = peopleWrapper;
        }

        @Override
        public void exec() {
            People people = new People();
            people.setFirstname("people"); //$NON-NLS-1$
            people.setLastname("Wizard"); //$NON-NLS-1$

            // CREATE
            final Session conn = jpOrm.session();
            people = conn.save(people);

            System.out.println("People saved with id: " + people.getId()); //$NON-NLS-1$
            peopleWrapper.setValue(people);

        }

    }

    class Delete implements ITransactionalCode {
        private final JpoRm jpOrm;
        private final People people;
        private final boolean throwsException;

        Delete(final JpoRm jpOrm, final People people, final boolean throwsException) {
            this.jpOrm = jpOrm;
            this.people = people;
            this.throwsException = throwsException;

        }

        @Override
        public void exec() throws Exception {
            final Session conn = jpOrm.session();
            conn.delete(people);
            if (throwsException) {
                throw new Exception();
            }
        }
    }

    class Load implements ITransactionalCode {
        private final JpoRm jpOrm;
        private final long id;
        private final GenericWrapper<People> peopleWrapper;

        Load(final JpoRm jpOrm, final long id, final GenericWrapper<People> peopleWrapper) {
            this.jpOrm = jpOrm;
            this.id = id;
            this.peopleWrapper = peopleWrapper;
        }

        @Override
        public void exec() {
            final Session conn = jpOrm.session();
            final Optional<People> peopleLoad1 = conn.findById(People.class, id).fetchOneOptional();
            peopleLoad1.ifPresent(people -> {
                peopleWrapper.setValue(people);
            });

        }
    }

    class Update implements ITransactionalCode {
        private final JpoRm jpOrm;
        private final People people;

        Update(final JpoRm jpOrm, final People people) {
            this.jpOrm = jpOrm;
            this.people = people;

        }

        @Override
        public void exec() {
            people.setFirstname("Wizard name"); //$NON-NLS-1$
            final Session conn = jpOrm.session();
            conn.update(people);
        }
    }

    @Test
    public void testJdbcTemplateTransaction1() throws Exception {
        final JpoRm jpOrm = getJPO();

        final ITransactionalExecutor executor = getH2TransactionalExecutor();

        final GenericWrapper<People> peopleWrapper = new GenericWrapper<People>(null);
        executor.exec(new Create(jpOrm, peopleWrapper));
        final long id = peopleWrapper.getValue().getId();

        GenericWrapper<People> peopleLoadedWrapper = new GenericWrapper<People>(null);
        executor.execReadOnly(new Load(jpOrm, id, peopleLoadedWrapper));
        People loaded = peopleLoadedWrapper.getValue();
        assertNotNull(loaded);

        try {
            executor.exec(new Delete(jpOrm, loaded, true));
        } catch (final Exception e) {
            // do nothings
        }

        peopleLoadedWrapper = new GenericWrapper<People>(null);
        executor.execReadOnly(new Load(jpOrm, id, peopleLoadedWrapper));
        loaded = peopleLoadedWrapper.getValue();
        assertNotNull(loaded);

        executor.exec(new Delete(jpOrm, loaded, false));

        peopleLoadedWrapper = new GenericWrapper<People>(null);
        executor.execReadOnly(new Load(jpOrm, id, peopleLoadedWrapper));
        loaded = peopleLoadedWrapper.getValue();
        assertNull(loaded);

    }
}
