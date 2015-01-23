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

	private static final long serialVersionUID = 1L;
	private final TransactionPropagation propagation;
	private final TransactionIsolation isolationLevel;
	private int timeout;
	private final boolean readOnly;

	public TransactionDefinitionImpl() {
		this(PROPAGATION_DEFAULT, ISOLATION_DEFAULT, READ_ONLY_DEFAULT, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final TransactionPropagation propagation) {
		this(propagation, ISOLATION_DEFAULT, READ_ONLY_DEFAULT, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final TransactionIsolation isolationLevel) {
		this(PROPAGATION_DEFAULT, isolationLevel, READ_ONLY_DEFAULT, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final boolean readOnly) {
		this(PROPAGATION_DEFAULT, ISOLATION_DEFAULT, readOnly, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final TransactionPropagation propagation, final TransactionIsolation isolationLevel) {
		this(propagation, isolationLevel, READ_ONLY_DEFAULT, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final TransactionIsolation isolationLevel, final boolean readOnly) {
		this(PROPAGATION_DEFAULT, isolationLevel, readOnly, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final TransactionPropagation propagation, final boolean readOnly) {
		this(propagation, ISOLATION_DEFAULT, readOnly, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final TransactionPropagation propagation, final TransactionIsolation isolationLevel,  final boolean readOnly) {
		this(propagation, isolationLevel, readOnly, TIMEOUT_DEFAULT);
	}

	public TransactionDefinitionImpl(final TransactionPropagation propagation, final TransactionIsolation isolationLevel, final boolean readOnly, final int timeoutSeconds) {
		this.propagation = propagation;
		this.isolationLevel = isolationLevel;
		setTimeout(timeoutSeconds);
		this.readOnly = readOnly;
	}

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


}
