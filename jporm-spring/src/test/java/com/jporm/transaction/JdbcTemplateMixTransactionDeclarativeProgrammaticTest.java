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
package com.jporm.transaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.BaseTestJdbcTemplate;
import com.jporm.JPO;
import com.jporm.query.find.FindQuery;
import com.jporm.session.Session;
import com.jporm.transaction.Transaction;
import com.jporm.transactional.ITransactionalCode;
import com.jporm.transactional.ITransactionalExecutor;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class JdbcTemplateMixTransactionDeclarativeProgrammaticTest extends BaseTestJdbcTemplate {

	public static int NAME_COUNT = 0;
	private ITransactionalExecutor txExecutor;
	JPO jpOrm;
	private final Random random = new Random();
	private final int repeat = 25; //to test if connections are released

	@Before
	public void setUp() {
		jpOrm = getJPO();
		jpOrm.register(People.class);
		txExecutor = getH2TransactionalExecutor();
	}

	@Test
	public void testJdbcTemplateTransaction1() throws Exception {
		for (int i=0; i<repeat; i++) {
			final String name1 = newFirstname();
			final String name2 = newFirstname();
			final Transaction tx = jpOrm.session().transaction();
			create( name1 );
			tx.commit();
			assertTrue( checkExists(name1) );
			assertFalse( checkExists(name2) );

			txExecutor.exec(new ITransactionalCode() {
				@Override
				public void exec() throws Exception {
					create(name2);
				}
			});
			assertTrue( checkExists(name2) );
		}
	}

	@Test
	public void testJdbcTemplateTransaction2() {
		for (int i=0; i<repeat; i++) {
			final String name1 = newFirstname();
			final String name2 = newFirstname();
			final Transaction tx = jpOrm.session().transaction();
			create( name1 );
			tx.commit();
			assertTrue( checkExists(name1) );
			assertFalse( checkExists(name2) );

			try {
				txExecutor.exec(new ITransactionalCode() {
					@Override
					public void exec() {
						create(name2);
						throw new RuntimeException();
					}
				});
			}
			catch (final Exception e) {
				e.printStackTrace();
			}

			assertFalse( checkExists(name2) );
		}
	}

	@Test
	public void testJdbcTemplateTransaction3() throws Exception {
		for (int i=0; i<repeat; i++) {
			final String name1 = newFirstname();
			final String name2 = newFirstname();

			assertFalse( checkExists(name1) );
			assertFalse( checkExists(name2) );

			txExecutor.exec(new ITransactionalCode() {
				@Override
				public void exec() throws Exception {
					create(name2);
					final Transaction tx = jpOrm.session().transaction();
					create( name1 );
					tx.commit();
				}
			});
			assertTrue( checkExists(name1) );
			assertTrue( checkExists(name2) );
		}
	}

	@Test
	public void testJdbcTemplateTransaction4() {
		for (int i=0; i<repeat; i++) {
			final String name1 = newFirstname();
			final String name2 = newFirstname();

			assertFalse( checkExists(name1) );
			assertFalse( checkExists(name2) );

			try {
				txExecutor.exec(new ITransactionalCode() {
					@Override
					public void exec() {
						create(name2);
						final Transaction tx = jpOrm.session().transaction();
						create( name1 );
						tx.rollback();
					}
				});
			} catch (final Exception e) {
				e.printStackTrace();
			}

			assertFalse( checkExists(name1) );
			assertFalse( checkExists(name2) );
		}
	}

	@Test
	public void testJdbcTemplateTransaction5() {
		for (int i=0; i<repeat; i++) {
			final String name1 = newFirstname();
			final String name2 = newFirstname();

			assertFalse( checkExists(name1) );
			assertFalse( checkExists(name2) );

			try {
				txExecutor.exec(new ITransactionalCode() {
					@Override
					public void exec() {
						create(name2);
						final Transaction tx = jpOrm.session().transaction();
						create( name1 );
						tx.commit();
						throw new RuntimeException();
					}
				});
			} catch (final Exception e) {
				e.printStackTrace();
			}

			assertFalse( checkExists(name1) );
			assertFalse( checkExists(name2) );
		}
	}

	@Test
	public void testJdbcTemplateTransaction6() {
		for (int i=0; i<repeat; i++) {
			final String name1 = newFirstname();
			final String name2 = newFirstname();

			assertFalse( checkExists(name1) );
			assertFalse( checkExists(name2) );

			final Transaction tx = jpOrm.session().transaction();
			create( name1 );
			try {
				txExecutor.exec(new ITransactionalCode() {
					@Override
					public void exec() {
						create(name2);
						throw new RuntimeException();
					}
				});
			} catch (final Exception e) {
				e.printStackTrace();
			}
			try{
				tx.commit();
			} catch (final Exception e) {
				tx.rollback();
			}

			assertFalse( checkExists(name1) );
			assertFalse( checkExists(name2) );
		}
	}

	long create(final String firstName) {

		//		final long id = new Date().getTime();
		People people = new People();
		//		people.setId( id );
		people.setFirstname( firstName );
		people.setLastname("Wizard"); //$NON-NLS-1$

		// CREATE
		final Session conn = jpOrm.session();
		people = conn.save(people);

		System.out.println("People [" + firstName + "] saved with id: " + people.getId()); //$NON-NLS-1$ //$NON-NLS-2$
		//		assertFalse( id == people.getId() );
		return people.getId();

	}

	private boolean checkExists(final String firstName) {
		final Session conn = jpOrm.session();
		final FindQuery<People> query = conn.findQuery(People.class);
		query.where().eq("firstname", firstName); //$NON-NLS-1$
		return query.getRowCount()>0;
	}

	private String newFirstname() {
		return "hello-" + random.nextInt() + "-" + NAME_COUNT++; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
