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
package com.jporm.rm.kotlin.connection.datasource

import com.jporm.commons.core.inject.ServiceCatalog
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.util.DBTypeDescription
import com.jporm.rm.kotlin.connection.Transaction
import com.jporm.rm.kotlin.connection.TransactionProvider
import com.jporm.sql.dialect.DBProfile

import javax.sql.DataSource

/**

 * @author Francesco Cina
 * *
 * *         21/mag/2011
 */
class DataSourceTransactionProvider @JvmOverloads constructor(private val dataSource: DataSource, private var dbType: DBProfile = DBTypeDescription.build(dataSource).dbType.dbProfile) : TransactionProvider {
    private var connectionProvider: DataSourceConnectionProvider = DataSourceConnectionProvider(dataSource, dbType)

    override fun newTransaction(serviceCatalog: ServiceCatalog, sqlCache: SqlCache, sqlFactory: SqlFactory): Transaction {
        return DataSourceTransaction(serviceCatalog, dbType, sqlCache, sqlFactory, connectionProvider)
    }

    /**
     * @return the connectionProvider
     */
    override fun connectionProvider(): DataSourceConnectionProvider {
        return connectionProvider
    }

    override fun dbProfile(): DBProfile {
       return dbType
    }

}
