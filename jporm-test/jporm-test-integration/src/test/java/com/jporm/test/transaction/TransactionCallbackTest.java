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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.jporm.exception.OrmException;
import com.jporm.session.Session;
import com.jporm.test.BaseTestAllDB;
import com.jporm.test.TestData;
import com.jporm.test.domain.section01.Employee;
import com.jporm.transaction.TransactionCallback;
import com.jporm.transaction.TransactionalSession;

/**
 *
 * @author Francesco Cina
 *
 * 20/mag/2011
 */
public class TransactionCallbackTest extends BaseTestAllDB {

	public TransactionCallbackTest(final String testName, final TestData testData) {
		super(testName, testData);
	}

	private final int repeatTests = 50;
	private Session jpoSession;

	@Before
	public void setUp() {
		jpoSession = getJPOrm().session();
	}

	@Test
	public void testTransactionCommitted() {

		final Random random = new Random();
		final List<Employee> employees = new ArrayList<Employee>();

		for (int i=0 ; i<repeatTests; i++) {
			jpoSession.txNow(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(final TransactionalSession session) {
					final Employee employee = new Employee();
					employee.setId( random.nextInt(Integer.MAX_VALUE) );
					employees.add(employee);
					session.save(employee);
					return null;
				}
			});
		}

		for (Employee employee : employees) {
			assertNotNull( jpoSession.find(Employee.class, employee.getId()).getOptional() );
		}
	}

	@Test
	public void testTransactionRolledback() {

		final Random random = new Random();
		final List<Employee> employees = new ArrayList<Employee>();

		for (int i=0 ; i<repeatTests; i++) {
			try {
				jpoSession.txNow(new TransactionCallback<Void>() {
					@Override
					public Void doInTransaction(final TransactionalSession session) {
						final Employee employee = new Employee();
						employee.setId( random.nextInt(Integer.MAX_VALUE) );
						employees.add(employee);
						session.save(employee);
						throw new RuntimeException("manually thrown exception"); //$NON-NLS-1$
					}
				});
			}
			catch (RuntimeException e) {
				//nothing to do
			}
		}

		for (Employee employee : employees) {
			assertFalse( jpoSession.find(Employee.class, employee.getId()).getOptional().isPresent() );
		}
	}

	@Test
	public void testPartecipatingInExistingTransaction() {

		final Random random = new Random();
		final List<Employee> employees = new ArrayList<Employee>();

		try {
			jpoSession.txVoidNow((_session) -> {
				for (int i=0 ; i<repeatTests; i++) {
					jpoSession.txNow(new TransactionCallback<Void>() {
						@Override
						public Void doInTransaction(final TransactionalSession session) {
							final Employee employee = new Employee();
							employee.setId( random.nextInt(Integer.MAX_VALUE) );
							employees.add(employee);
							session.save(employee);
							return null;
						}
					});
				}
				throw new RuntimeException("Manually causing rollback");
			});
		} catch (RuntimeException e) {
			//do nothing
		}


		for (Employee employee : employees) {
			assertFalse( jpoSession.find(Employee.class, employee.getId()).getOptional().isPresent() );
		}
	}


	@Test
	public void testPartecipatingInExistingTransactionAndRollback() {

		final Random random = new Random();
		final List<Employee> employees = new ArrayList<Employee>();

		try {
			jpoSession.txVoidNow((_session) -> {

				for (int i=0 ; i<repeatTests; i++) {
					jpoSession.txNow(new TransactionCallback<Void>() {
						@Override
						public Void doInTransaction(final TransactionalSession session) {
							final Employee employee = new Employee();
							employee.setId( random.nextInt(Integer.MAX_VALUE) );
							employees.add(employee);
							session.save(employee);
							return null;
						}
					});
				}

				try {
					jpoSession.txNow(new TransactionCallback<Void>() {
						@Override
						public Void doInTransaction(final TransactionalSession session) {
							final Employee employee = new Employee();
							employee.setId( random.nextInt(Integer.MAX_VALUE) );
							employees.add(employee);
							session.save(employee);
							throw new RuntimeException("manually thrown exception"); //$NON-NLS-1$
						}
					});
				}
				catch (RuntimeException e) {
					//nothing to do
				}


			});
			fail();
		} catch (OrmException e) {
			assertTrue(e.getMessage().contains("rollback")); //$NON-NLS-1$
		}

		for (Employee employee : employees) {
			assertFalse( jpoSession.find(Employee.class, employee.getId()).getOptional().isPresent() );
		}
	}
}
