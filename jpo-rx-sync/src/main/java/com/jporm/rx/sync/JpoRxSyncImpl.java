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
package com.jporm.rx.sync;

import com.jporm.rx.JpoRx;
import com.jporm.rx.sync.session.SessionSync;
import com.jporm.rx.sync.session.impl.SessionSyncImpl;
import com.jporm.rx.sync.transaction.TransactionSync;
import com.jporm.rx.sync.transaction.TransactionSyncImpl;

/**
 *
 * @author Francesco Cina'
 *
 * 26/ago/2011
 */
public class JpoRxSyncImpl implements JpoRxSync {

	private JpoRx jpoRx;

	/**
	 * Create a new instance of JpoRxSync.
	 *
	 * @param jpoRx
	 */
	public JpoRxSyncImpl(final JpoRx jpoRx) {
		this.jpoRx = jpoRx;
	}

	@Override
	public final SessionSync session() {
		return new SessionSyncImpl(jpoRx.session());
	}

	@Override
	public TransactionSync transaction() {
		return new TransactionSyncImpl(jpoRx.transaction());
	}

}
