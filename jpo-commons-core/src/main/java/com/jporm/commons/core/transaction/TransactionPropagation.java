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
package com.jporm.commons.core.transaction;

/**
 * 
 * @author Francesco Cina
 *
 * 13/giu/2011
 */
public enum TransactionPropagation {
	
	/**
	 * Support a current transaction; create a new one if none exists.
	 * This is the default setting.
	 */
	REQUIRED(0),
	
	/**
	 * Support a current transaction; execute non-transactionally if none exists.
	 * Note that the exact behavior depends on the actual orm backend.
	 */
	SUPPORTS(1),
	
	/**
	 * Support a current transaction; throw an exception if no current transaction
	 * exists.
	 */
	MANDATORY(2), 
	
	/**
	 * Create a new transaction, suspending the current transaction if one exists.
	 * Note that the exact behavior depends on the actual orm backend.
	 */
	REQUIRES_NEW(3),
	
	/**
	 * Do not support a current transaction; rather always execute non-transactionally.
	 */
	NOT_SUPPORTED(4),
	
	/**
	 * Do not support a current transaction; throw an exception if a current transaction
	 * exists.
	 */
	NEVER(5),
	
	/**
	 * Execute within a nested transaction if a current transaction exists.
	 * Note that the exact behavior depends on the actual orm backend.
	 */
	NESTED(6);

	 private int propagation;

	 private TransactionPropagation(int propagation) {
		 this.propagation = propagation;
	 }

	 public int getTransactionPropagation() {
	   return propagation;
	 }
}
