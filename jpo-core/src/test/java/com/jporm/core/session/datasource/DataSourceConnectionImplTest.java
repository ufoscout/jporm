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
package com.jporm.core.session.datasource;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.exception.JpoTransactionTimedOutException;

public class DataSourceConnectionImplTest extends BaseTestApi {

	@Test
	public void testTimeoutCalculation() {
		DataSourceConnection conn = new DataSourceConnectionImpl(null, true);
		long expireInstant = System.currentTimeMillis();
		conn.setExpireInstant(expireInstant);

		assertEquals( 3, conn.getRemainingTimeoutSeconds(expireInstant - 2001) );
		assertEquals( 2, conn.getRemainingTimeoutSeconds(expireInstant - 2000) );
		assertEquals( 2, conn.getRemainingTimeoutSeconds(expireInstant - 1500) );
		assertEquals( 1, conn.getRemainingTimeoutSeconds(expireInstant - 1000) );
		assertEquals( 1, conn.getRemainingTimeoutSeconds(expireInstant - 1) );

	}

	@Test(expected=JpoTransactionTimedOutException.class)
	public void testShouldThrowTimeoutExceptionOne() {
		DataSourceConnection conn = new DataSourceConnectionImpl(null, true);
		long expireInstant = System.currentTimeMillis();
		conn.setExpireInstant(expireInstant);
		conn.getRemainingTimeoutSeconds(expireInstant);
	}

	@Test(expected=JpoTransactionTimedOutException.class)
	public void testShouldThrowTimeoutExceptionTwo() {
		DataSourceConnection conn = new DataSourceConnectionImpl(null, true);
		long expireInstant = System.currentTimeMillis();
		conn.setExpireInstant(expireInstant);
		conn.getRemainingTimeoutSeconds(expireInstant + 1);
	}

}
