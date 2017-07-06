/*******************************************************************************
 * Copyright 2015 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.kotlin.connection

import com.jporm.commons.core.transaction.TransactionIsolation
import com.jporm.rm.kotlin.session.Session

interface Transaction {

    /**
     * Executes the current transaction

     * @return
     */
    fun <T> execute(session: (Session) -> T): T

    /**
     * Executes the current transaction

     * @return
     */
    fun execute(session: (Session) -> Unit)

    /**
     * Set the transaction isolation level for the current transaction.

     * @param isolation
     * *
     * @return
     */
    fun isolation(isolation: TransactionIsolation): Transaction

    /**
     * Whether the transaction is read only. Default is false.

     * @param readOnly
     * *
     * @return
     */
    fun readOnly(readOnly: Boolean): Transaction

    /**
     * Set the transaction timeout in seconds

     * @param seconds
     * *
     * @return
     */
    fun timeout(seconds: Int): Transaction

}
