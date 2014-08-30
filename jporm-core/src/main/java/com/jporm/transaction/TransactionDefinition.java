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

import java.io.Serializable;

/**
 * 
 * @author cinafr
 *
 */
public interface TransactionDefinition extends Serializable {

	/**
	 * Use the default timeout of the underlying transaction system,
	 * or none if timeouts are not supported. 
	 */
	int TIMEOUT_DEFAULT = -1;
	
	TransactionPropagation PROPAGATION_DEFAULT = TransactionPropagation.REQUIRED;
	
	TransactionIsolation ISOLATION_DEFAULT = TransactionIsolation.DEFAULT;
	
	boolean READ_ONLY_DEFAULT = false;


	/**
	 * Return the propagation behavior.
	 */
	TransactionPropagation getPropagation();

	/**
	 * Return the isolation level.
	 */
	TransactionIsolation getIsolationLevel();

	/**
	 * Return the transaction timeout.
	 */
	int getTimeout();

	/**
	 * Return whether to optimize as a read-only transaction.
	 */
	boolean isReadOnly();

}

