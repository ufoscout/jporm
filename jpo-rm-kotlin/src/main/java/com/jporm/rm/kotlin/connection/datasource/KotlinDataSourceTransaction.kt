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
package com.jporm.rm.kotlin.connection.datasource

import com.jporm.commons.core.transaction.TransactionIsolation
import com.jporm.rm.kotlin.connection.Transaction
import com.jporm.rm.kotlin.session.Session
import com.jporm.rm.kotlin.session.SessionImpl
import java.util.function.Function

class KotlinDataSourceTransaction(val tx: com.jporm.rm.connection.Transaction): Transaction {

    override fun <T> execute(callback: (Session) -> T): T {
        return tx.execute(Function { callback(SessionImpl(it)) });
    }

    override fun execute(callback: (Session) -> Unit) {
        execute({
            callback(it)
            null
        })
    }

    override fun isolation(isolation: TransactionIsolation): Transaction {
        tx.isolation(isolation)
        return this
    }

    override fun readOnly(readOnly: Boolean): Transaction {
        tx.readOnly(readOnly)
        return this
    }

    override fun timeout(seconds: Int): Transaction {
        tx.timeout(seconds)
        return this
    }
}
