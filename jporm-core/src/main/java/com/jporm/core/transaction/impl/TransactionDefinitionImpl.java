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
package com.jporm.core.transaction.impl;

import com.jporm.core.transaction.TransactionDefinition;
import com.jporm.core.transaction.TransactionIsolation;
import com.jporm.core.transaction.TransactionPropagation;

/**
 * Definition of a new Transaction.
 * Default values are:
 *
 * Transaction propagation: REQUIRED
 * Isolation level: the default of the actual jdbc driver
 * Timeout: the default of the actual jdbc driver
 * ReadOnly: false
 *
 * @author cinafr
 *
 */
public class TransactionDefinitionImpl implements TransactionDefinition {

	private TransactionPropagation propagation = PROPAGATION_DEFAULT;
	private TransactionIsolation isolationLevel = ISOLATION_DEFAULT;
	private int timeout = TIMEOUT_DEFAULT;
	private boolean readOnly = READ_ONLY_DEFAULT;

	@Override
	public TransactionPropagation getPropagation() {
		return propagation;
	}

	@Override
	public TransactionIsolation getIsolationLevel() {
		return isolationLevel;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(final int seconds) {
		timeout = seconds;
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public TransactionDefinition timeout(int seconds) {
		timeout = seconds;
		return this;
	}

	@Override
	public TransactionDefinition readOnly(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	@Override
	public TransactionDefinition propagation(TransactionPropagation propagation) {
		this.propagation = propagation;
		return this;
	}

	@Override
	public TransactionDefinition isolation(TransactionIsolation isolation) {
		isolationLevel = isolation;
		return this;
	}


}
