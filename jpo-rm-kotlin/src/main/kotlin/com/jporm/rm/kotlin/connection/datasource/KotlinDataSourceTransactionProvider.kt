/*******************************************************************************
 * Copyright 2013 Francesco Cina'

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
package com.jporm.rm.quasar.connection.datasource

import java.util.concurrent.atomic.AtomicInteger

import javax.sql.DataSource

import com.jporm.commons.core.inject.ServiceCatalog
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.util.DBTypeDescription
import com.jporm.rm.connection.Transaction
import com.jporm.rm.connection.TransactionProvider
import com.jporm.rm.connection.datasource.DataSourceTransaction
import com.jporm.sql.dialect.DBProfile
import kotlinx.coroutines.experimental.launch

import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import kotlinx.coroutines.experimental.runBlocking

/**

 * @author Francesco Cina
 * *
 * *         21/mag/2011
 */
class KotlinDataSourceTransactionProvider @JvmOverloads constructor(private val dataSource: DataSource, private val poolSize: Int, private var dbType: DBProfile? = null) : TransactionProvider {

    private val coroutineContext = newFixedThreadPoolContext(nThreads = poolSize, name = "jpo-pool-" + COUNT.getAndIncrement())
    private var dbProfile = dbType ?: DBTypeDescription.build(dataSource).dbType.dbProfile
    private var connectionProvider: KotlinDataSourceConnectionProvider = KotlinDataSourceConnectionProvider(dataSource, dbProfile, connectionExecutor, executor)

    override fun getDBProfile() : DBProfile {
            return dbProfile
        }

    override fun getTransaction(serviceCatalog: ServiceCatalog, sqlCache: SqlCache, sqlFactory: SqlFactory): Transaction {
        return DataSourceTransaction(serviceCatalog, dbProfile, sqlCache, sqlFactory, getConnectionProvider())
    }

    override fun getConnectionProvider(): KotlinDataSourceConnectionProvider {

        runBlocking {


    }
        return connectionProvider!!
    }

    suspend fun test() =
        run(context) {

        }


    companion object {
        private val COUNT = AtomicInteger(0)
    }

}
