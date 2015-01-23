/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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

public final class TransactionDefinitionBuilder {

	private TransactionPropagation propagation = TransactionDefinition.PROPAGATION_DEFAULT;
	private TransactionIsolation isolationLevel = TransactionDefinition.ISOLATION_DEFAULT;
	private int timeout = TransactionDefinition.TIMEOUT_DEFAULT;
	private boolean readOnly = TransactionDefinition.READ_ONLY_DEFAULT;

	public TransactionDefinitionBuilder propagation(final TransactionPropagation propagation) {
		this.propagation = propagation;
		return this;
	}

	public TransactionDefinitionBuilder isolationLevel(final TransactionIsolation isolationLevel) {
		this.isolationLevel = isolationLevel;
		return this;
	}

	public TransactionDefinitionBuilder timeout(final int seconds) {
		timeout = seconds;
		return this;
	}

	public TransactionDefinitionBuilder propagation(final boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	public TransactionDefinition build() {
		return new TransactionDefinitionImpl(propagation, isolationLevel, readOnly, timeout);
	}

}
