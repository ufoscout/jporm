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
package com.jporm.rm.kotlin

import com.jporm.commons.core.inject.ServiceCatalog
import com.jporm.commons.core.query.SqlFactory
import com.jporm.commons.core.query.cache.SqlCache
import com.jporm.commons.core.query.cache.SqlCacheImpl
import com.jporm.rm.kotlin.connection.Transaction
import com.jporm.rm.kotlin.connection.TransactionProvider
import com.jporm.rm.kotlin.session.Session
import com.jporm.rm.kotlin.session.SessionImpl
import com.jporm.rm.kotlin.session.SqlExecutorImpl
import com.jporm.types.TypeConverter
import com.jporm.types.builder.TypeConverterBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.function.Function

/**

 * @author Francesco Cina'
 * *
 * *         26/ago/2011
 */
class JpoRmImpl
/**
 * Create a new instance of JPOrm.

 * @param sessionProvider
 */
(val transactionProvider: TransactionProvider, val serviceCatalog: ServiceCatalog) : JpoRm {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val instanceCount: Int?
    private val sqlFactory: SqlFactory
    private val sqlCache: SqlCache
    private var session: Session? = null

    init {
        instanceCount = JPORM_INSTANCES_COUNT.getAndIncrement()
        logger.info("Building new instance of JPO (instance [{}])", instanceCount)
        sqlFactory = SqlFactory(serviceCatalog.classToolMap, serviceCatalog.propertiesFactory, transactionProvider.dbProfile().sqlRender)
        sqlCache = SqlCacheImpl(sqlFactory, serviceCatalog.classToolMap, transactionProvider.dbProfile())
    }

    /**
     * Register a new [TypeConverter]. If a [TypeConverter] wraps a
     * Class that is already mapped, the last registered [TypeConverter]
     * will be used.

     * @param typeConverter
     * *
     * @throws OrmConfigurationException
     */
    override fun register(typeWrapper: TypeConverter<*, *>) {
        serviceCatalog.typeFactory.addTypeConverter(typeWrapper)
    }

    /**
     * Register a new [TypeConverterBuilder]. If a [TypeConverter]
     * wraps a Class that is already mapped, the last registered
     * [TypeConverter] will be used.

     * @param typeConverterBuilder
     * *
     * @throws OrmConfigurationException
     */
    override fun register(typeWrapperBuilder: TypeConverterBuilder<*, *>) {
        serviceCatalog.typeFactory.addTypeConverter(typeWrapperBuilder)
    }

    override fun tx(): Transaction {
        return transactionProvider.newTransaction(serviceCatalog, sqlCache, sqlFactory)
    }

    override fun tx(session: (Session) -> Unit) {
        tx().execute(session)
    }

    override fun <T> tx(session: (Session) -> T): T {
        return tx().execute(session)
    }

    override fun session(): Session {
        if (session == null) {
            synchronized(this) {
                val sqlExecutor = SqlExecutorImpl(transactionProvider.connectionProvider(), serviceCatalog.typeFactory)
                val tempSession = SessionImpl(serviceCatalog, transactionProvider.dbProfile(), sqlExecutor, sqlCache, sqlFactory)
                session = tempSession
            }
        }
        return session!!
    }

    companion object {

        private val JPORM_INSTANCES_COUNT = AtomicInteger()
    }

}
