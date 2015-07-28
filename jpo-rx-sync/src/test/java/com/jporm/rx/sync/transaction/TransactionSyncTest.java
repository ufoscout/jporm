/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.sync.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.jporm.rx.sync.JpoRxSync;
import com.jporm.rx.sync.SyncTestBase;
import com.jporm.test.domain.section08.CommonUser;

public class TransactionSyncTest extends SyncTestBase {

	@Test
	public void transaction_should_be_committed_at_the_end() throws Throwable {
		JpoRxSync jpo = newJpo();

		CommonUser newUser = jpo.transaction().execute(txSession -> {
			CommonUser user = new CommonUser();
			user.setFirstname(UUID.randomUUID().toString());
			user.setLastname(UUID.randomUUID().toString());

			return txSession.save(user);
		});
		assertNotNull(newUser);

		Optional<CommonUser> optionalFoundUser = jpo.session().findById(CommonUser.class, newUser.getId()).fetchOptional();
		assertTrue(optionalFoundUser.isPresent());
		assertEquals(newUser.getFirstname(), optionalFoundUser.get().getFirstname());

	}

	@Test
	public void failing_transaction_should_be_rolledback_at_the_end() throws Throwable {
		JpoRxSync jpo = newJpo();

		AtomicLong firstUserId = new AtomicLong();
		//
		try {
			jpo.transaction().execute(txSession -> {
				CommonUser user = new CommonUser();
				user.setFirstname(UUID.randomUUID().toString());
				user.setLastname(UUID.randomUUID().toString());

				CommonUser firstUser = txSession.save(user);
				assertNotNull(firstUser);
				assertNotNull(firstUser.getId());
				firstUserId.set(firstUser.getId());

				assertTrue(txSession.findById(CommonUser.class, firstUserId.get()).exist());

				// This action should fail because the object does not provide
				// all the mandatory fields
				CommonUser failingUser = new CommonUser();
				return txSession.save(failingUser);
			});
		} catch (Exception e) {
			assertFalse(jpo.session().findById(CommonUser.class, firstUserId.get()).exist());
		}

	}

}
