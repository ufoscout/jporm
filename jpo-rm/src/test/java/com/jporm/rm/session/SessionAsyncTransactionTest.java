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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Feb 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.rm.session;

import static org.junit.Assert.assertEquals;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.jporm.core.domain.AutoId;
import com.jporm.rm.BaseTestApi;
import com.jporm.rm.JPO;


/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SessionAsyncTransactionTest extends BaseTestApi {

	@Test
	public void testAsyncTransactionExecution() throws InterruptedException, ExecutionException {

		JPO jpo = getJPO();
		String value = UUID.randomUUID().toString();

		CompletableFuture<AutoId> futureEmp = jpo.session().txAsync(txSession -> {
			AutoId emp = new AutoId();
			emp.setValue(value);
			return txSession.save(emp);
		})
		.thenCompose(emp -> jpo.session().txAsync(txSession -> {
			return txSession.find(AutoId.class, emp.getId()).fetchUnique();
		}));

		assertEquals( value, futureEmp.get().getValue() );
	}

}
