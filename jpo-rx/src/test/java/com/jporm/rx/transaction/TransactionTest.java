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
package com.jporm.rx.transaction;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.jporm.rx.BaseTestApi;
import com.jporm.rx.JpoRx;
import com.jporm.test.domain.section08.CommonUser;

public class TransactionTest extends BaseTestApi {

	@Test
	public void transaction_should_be_committed_at_the_end() throws Throwable {
		JpoRx jpo = newJpo();

		jpo.transaction().execute(txSession -> {
			CommonUser user = new CommonUser();
			user.setFirstname(UUID.randomUUID().toString());
			user.setLastname(UUID.randomUUID().toString());

			return txSession.save(user);
		})
		.handle((user, ex) -> {
			getLogger().info("Exception is: {}", ex);
			threadAssertNotNull(user);

			jpo.session().findById(CommonUser.class, user.getId()).fetchOptional()
			.thenApply(optionalFoundUser -> {
				threadAssertTrue(optionalFoundUser.isPresent());
				threadAssertEquals(user.getFirstname(), optionalFoundUser.get().getFirstname());
				resume();
				return null;
			});
			return null;
		});
		await(2500, 1);
	}


	@Test
	public void failing_transaction_should_be_rolledback_at_the_end() throws Throwable {
		JpoRx jpo = newJpo();

		AtomicLong firstUserId = new AtomicLong();

		jpo.transaction().execute(txSession -> {
			CommonUser user = new CommonUser();
			user.setFirstname(UUID.randomUUID().toString());
			user.setLastname(UUID.randomUUID().toString());

			CompletableFuture<CommonUser> saved = txSession.save(user)
					.thenCompose(firstUser -> {
						threadAssertNotNull(firstUser);
						threadAssertNotNull(firstUser.getId());
						firstUserId.set(firstUser.getId());
						//This action should fail because the object does not provide all the mandatory fields
						CommonUser failingUser = new CommonUser();
						return txSession.save(failingUser);
					});
			return saved;
		})
		.handle((user, ex) -> {
			getLogger().info("Exception is: {}", ex);
			threadAssertNotNull(ex);

			jpo.session().findById(CommonUser.class, firstUserId.get()).fetchOptional()
			.thenApply(optionalFoundUser -> {
				threadAssertFalse(optionalFoundUser.isPresent());
				resume();
				return null;
			});
			return null;
		});
		await(2500, 1);
	}

}
