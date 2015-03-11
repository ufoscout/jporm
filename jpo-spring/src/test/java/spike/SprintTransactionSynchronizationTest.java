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
package spike;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.jporm.BaseTestJdbcTemplate;

/**
 * 
 * @author Francesco Cina'
 *
 * 29 Apr 2011
 */
public class SprintTransactionSynchronizationTest extends BaseTestJdbcTemplate {

	private PlatformTransactionManager platformTransactionManager;

	@Before
	public void setUp() {
		this.platformTransactionManager = getH2PlatformTransactionManager();
	}

	@Test
	public void testSynchronizeTransactionSpring1() {

		final org.springframework.transaction.support.DefaultTransactionDefinition definitionOuter = new org.springframework.transaction.support.DefaultTransactionDefinition();
		final org.springframework.transaction.support.DefaultTransactionDefinition definitionInner = new org.springframework.transaction.support.DefaultTransactionDefinition();
		definitionInner.setPropagationBehavior(org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED);
		final TransactionStatus statusOuter = this.platformTransactionManager.getTransaction(definitionOuter);
		final TransactionStatus statusInner = this.platformTransactionManager.getTransaction(definitionInner);

		assertTrue(statusOuter.isNewTransaction());
		assertFalse(statusInner.isNewTransaction());

		assertFalse(statusOuter.isCompleted());
		assertFalse(statusInner.isCompleted());

		this.platformTransactionManager.commit(statusOuter);
		assertTrue(statusOuter.isCompleted());
		assertFalse(statusInner.isCompleted());

		this.platformTransactionManager.commit(statusInner);
		assertTrue(statusInner.isCompleted());
	}

	@Test
	public void testSynchronizeTransactionSpring2() {

		final org.springframework.transaction.support.DefaultTransactionDefinition definitionOuter = new org.springframework.transaction.support.DefaultTransactionDefinition();
		final org.springframework.transaction.support.DefaultTransactionDefinition definitionInner = new org.springframework.transaction.support.DefaultTransactionDefinition();
		definitionInner.setPropagationBehavior(org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED);
		final TransactionStatus statusOuter = this.platformTransactionManager.getTransaction(definitionOuter);
		final TransactionStatus statusInner = this.platformTransactionManager.getTransaction(definitionInner);

		assertTrue(statusOuter.isNewTransaction());
		assertFalse(statusInner.isNewTransaction());

		assertFalse(statusOuter.isCompleted());
		assertFalse(statusInner.isCompleted());

		this.platformTransactionManager.commit(statusInner);
		assertTrue(statusInner.isCompleted());
		assertFalse(statusOuter.isCompleted());

		this.platformTransactionManager.commit(statusOuter);
		assertTrue(statusOuter.isCompleted());
		assertTrue(statusInner.isCompleted());


	}

	@Test
	public void testSynchronizeTransactionSpring3() {

		final org.springframework.transaction.support.DefaultTransactionDefinition definitionOuter = new org.springframework.transaction.support.DefaultTransactionDefinition();
		final org.springframework.transaction.support.DefaultTransactionDefinition definitionInner = new org.springframework.transaction.support.DefaultTransactionDefinition();
		definitionInner.setPropagationBehavior(org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED);

		final TransactionStatus statusOuter = this.platformTransactionManager.getTransaction(definitionOuter);
		final TransactionStatus statusInner = this.platformTransactionManager.getTransaction(definitionInner);

		assertTrue(statusOuter.isNewTransaction());
		assertFalse(statusInner.isNewTransaction());

		assertFalse(statusOuter.isRollbackOnly());

		System.out.println("pre rollback"); //$NON-NLS-1$
		this.platformTransactionManager.rollback(statusInner);
		System.out.println("post rollback"); //$NON-NLS-1$
		assertFalse(statusOuter.isCompleted());
		assertTrue(statusOuter.isRollbackOnly());
		if (!statusOuter.isRollbackOnly()){
			this.platformTransactionManager.commit(statusOuter);
		} else {
			this.platformTransactionManager.rollback(statusOuter);
		}
	}

}
