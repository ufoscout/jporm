/*******************************************************************************
 * Copyright 2016 Francesco Cina'

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


import com.jporm.commons.core.inject.ServiceCatalog
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.transaction.TransactionIsolation
import com.jporm.rm.kotlin.session.Session
import com.jporm.rm.kotlin.session.SqlExecutorImpl
import com.jporm.rm.kotlin.session.SessionImpl
import com.jporm.sql.dialect.DBProfile

abstract class AbstractTransaction(private val serviceCatalog: ServiceCatalog,
                                   /**
                                    * @return the dbProfile
                                    */
                                   val dbProfile: DBProfile, private val sqlCache: SqlCache, private val sqlFactory: SqlFactory) : Transaction {
    /**
     * @return the transactionIsolation
     */
    protected var transactionIsolation: TransactionIsolation
        private set
    /**
     * @return the timeout
     */
    protected var timeout: Int = 0
        private set
    /**
     * @return the readOnly
     */
    protected var isReadOnly = false
        private set

    init {

        val configService = serviceCatalog.configService
        transactionIsolation = configService.defaultTransactionIsolation
        timeout = configService.transactionDefaultTimeoutSeconds

    }

    override fun isolation(isolation: TransactionIsolation): Transaction {
        transactionIsolation = isolation
        return this
    }

    override fun readOnly(readOnly: Boolean): Transaction {
        this.isReadOnly = readOnly
        return this
    }

    override fun timeout(seconds: Int): Transaction {
        timeout = seconds
        return this
    }

    protected fun newSession(connection: Connection): Session {
        val sqlExecutor = SqlExecutorImpl(object : ConnectionProvider<Connection> {
            override fun <T> connection(autoCommit: Boolean, connectionFunction: (Connection) -> T): T {
                return connectionFunction(connection)
            }
        }, serviceCatalog.typeFactory)
        return SessionImpl(serviceCatalog, dbProfile, sqlExecutor, sqlCache, sqlFactory)
    }
}
