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

import com.jporm.commons.core.exception.JpoException
import com.jporm.commons.core.exception.JpoTransactionTimedOutException
import com.jporm.commons.core.exception.sql.JpoSqlException
import com.jporm.commons.core.io.jdbc.JdbcResultSet
import com.jporm.commons.core.io.jdbc.JdbcStatement
import com.jporm.commons.core.transaction.TransactionIsolation
import com.jporm.commons.core.util.SpringBasedSQLStateSQLExceptionTranslator
import com.jporm.rm.kotlin.connection.Connection
import com.jporm.sql.dialect.DBProfile
import com.jporm.types.io.BatchPreparedStatementSetter
import com.jporm.types.io.GeneratedKeyReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.function.Consumer
import java.util.function.Function

/**

 * @author Francesco Cina
 * *
 * *         02/lug/2011
 * *
 * *         [Connection] implementation using java.sql.Connection as
 * *         backend.
 */
class DataSourceConnectionImpl(private val connection: java.sql.Connection, private val dbType: DBProfile) : DataSourceConnection {

    private val connectionNumber = COUNT++
    private var timeout = -1
    private var expireInstant: Long = -1

    @Throws(JpoException::class)
    override fun batchUpdate(sqls: Collection<String>, sqlPreProcessor: (String) -> String): IntArray {
        var _statement: Statement? = null
        try {
            val statement = connection.createStatement()
            setTimeout(statement)
            _statement = statement
            sqls.forEach { sql ->
                try {
                    val processedSql = sqlPreProcessor(sql)
                    LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, processedSql)
                    statement.addBatch(sql)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
            connection.isReadOnly = false
            val result = statement.executeBatch()
            return result
        } catch (e: Exception) {
            throw translateException("batchUpdate", "", e)
        } finally {
            try {
                close(_statement)
            } catch (e: Exception) {
                throw translateException("batchUpdate", "", e)
            }

        }
    }

    @Throws(JpoException::class)
    override fun batchUpdate(sql: String, psc: BatchPreparedStatementSetter): IntArray {
        LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql)
        var preparedStatement: PreparedStatement? = null
        try {
            preparedStatement = connection.prepareStatement(sql)
            setTimeout(preparedStatement)
            for (i in 0..psc.batchSize - 1) {
                psc.set(JdbcStatement(preparedStatement), i)
                preparedStatement!!.addBatch()
            }
            val result = preparedStatement!!.executeBatch()
            return result
        } catch (e: Exception) {
            throw translateException("batchUpdate", sql, e)
        } finally {
            try {
                close(preparedStatement)
            } catch (e: Exception) {
                throw translateException("batchUpdate", sql, e)
            }

        }
    }

    @Throws(JpoException::class)
    override fun batchUpdate(sql: String, statementSetters: Collection<(com.jporm.types.io.Statement) -> Unit>): IntArray {
        LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql)
        var _preparedStatement: PreparedStatement? = null
        try {
            val preparedStatement = connection.prepareStatement(sql)
            _preparedStatement = preparedStatement
            setTimeout(preparedStatement)
            statementSetters.forEach { statementSetter ->
                try {
                    statementSetter(JdbcStatement(preparedStatement))
                    preparedStatement.addBatch()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
            val result = preparedStatement.executeBatch()
            return result
        } catch (e: Exception) {
            throw translateException("batchUpdate", sql, e)
        } finally {
            try {
                close(_preparedStatement)
            } catch (e: Exception) {
                throw translateException("batchUpdate", sql, e)
            }

        }
    }

    @Throws(JpoException::class)
    override fun execute(sql: String) {
        LOGGER.debug("Connection [{}] - Execute sql: [{}]", connectionNumber, sql)
        var preparedStatement: PreparedStatement? = null
        try {
            preparedStatement = connection.prepareStatement(sql)
            setTimeout(preparedStatement)
            preparedStatement!!.execute()
        } catch (e: Exception) {
            throw translateException("execute", sql, e)
        } finally {
            try {
                close(preparedStatement)
            } catch (e: Exception) {
                throw translateException("execute", sql, e)
            }

        }
    }

    private fun getRemainingTimeoutSeconds(fromInstantMillis: Long): Int {
        throwExceptionIfTimedOut(fromInstantMillis)
        val diff = (expireInstant - fromInstantMillis + 999).toInt() / 1000
        return diff
    }

    @Throws(JpoException::class)
    override fun <T> query(sql: String, pss: (com.jporm.types.io.Statement) -> Unit, rse: (com.jporm.types.io.ResultSet) -> T): T {
        LOGGER.debug("Connection [{}] - Execute query: [{}]", connectionNumber, sql)
        var resultSet: ResultSet? = null
        var preparedStatement: PreparedStatement? = null
        try {
            preparedStatement = connection.prepareStatement(sql)
            setTimeout(preparedStatement)
            pss(JdbcStatement(preparedStatement))
            resultSet = preparedStatement!!.executeQuery()
            return rse(JdbcResultSet(resultSet))
        } catch (e: Exception) {
            throw translateException("query", sql, e)
        } finally {
            try {
                close(resultSet, preparedStatement)
                LOGGER.debug("Connection [{}] - close statements", connectionNumber)
            } catch (e: Exception) {
                throw translateException("query", sql, e)
            }

        }
    }

    override fun setReadOnly(readOnly: Boolean) {
        try {
            LOGGER.debug("Connection [{}] - set readOnly mode to [{}]", connectionNumber, readOnly)
            connection.isReadOnly = readOnly
        } catch (e: SQLException) {
            throw translateException("setTransactionIsolation", "", e)
        }

    }

    override fun setTimeout(timeout: Int) {
        LOGGER.debug("Connection [{}] - set timeout to [{}]", connectionNumber, timeout)
        this.timeout = timeout
        expireInstant = System.currentTimeMillis() + timeout * 1000
    }

    @Throws(SQLException::class)
    private fun setTimeout(statement: Statement) {
        if (timeout >= 0) {
            statement.queryTimeout = getRemainingTimeoutSeconds(System.currentTimeMillis())
        }
    }

    override fun setTransactionIsolation(isolationLevel: TransactionIsolation) {
        try {
            LOGGER.debug("Connection [{}] - set transaction isolation to [{}]", connectionNumber, isolationLevel)
            connection.transactionIsolation = isolationLevel.transactionIsolation
        } catch (e: SQLException) {
            throw translateException("setTransactionIsolation", "", e)
        }

    }

    private fun throwExceptionIfTimedOut(fromInstantMillis: Long) {
        if (fromInstantMillis >= expireInstant) {
            throw JpoTransactionTimedOutException("Transaction timed out.")
        }
    }

    override fun update(sql: String, pss: (com.jporm.types.io.Statement) -> Unit): Int {
        LOGGER.debug("Connection [{}] - Execute update query: [{}]", connectionNumber, sql)
        val generatedKeyResultSet: ResultSet? = null
        var preparedStatement: PreparedStatement? = null
        try {
            preparedStatement = dbType.statementStrategy.prepareStatement(connection, sql, EMPTY_STRING_ARRAY)
            setTimeout(preparedStatement)
            pss(JdbcStatement(preparedStatement))
            return preparedStatement!!.executeUpdate()
        } catch (e: Exception) {
            throw translateException("update", sql, e)
        } finally {
            try {
                close(generatedKeyResultSet, preparedStatement)
            } catch (e: Exception) {
                throw translateException("update", sql, e)
            }

        }
    }

    @Throws(JpoException::class)
    override fun <R> update(sql: String, generatedKeyReader: GeneratedKeyReader<R>, pss: (com.jporm.types.io.Statement) -> Unit): R {
        LOGGER.debug("Connection [{}] - Execute update query: [{}]", connectionNumber, sql)
        var generatedKeyResultSet: ResultSet? = null
        var preparedStatement: PreparedStatement? = null
        try {
            val generatedColumnNames = generatedKeyReader.generatedColumnNames()

            preparedStatement = dbType.statementStrategy.prepareStatement(connection, sql, generatedColumnNames)
            setTimeout(preparedStatement)
            pss(JdbcStatement(preparedStatement))
            val result = preparedStatement!!.executeUpdate()
            generatedKeyResultSet = preparedStatement.generatedKeys
            return generatedKeyReader.read(JdbcResultSet(generatedKeyResultSet), result)
        } catch (e: Exception) {
            throw translateException("update", sql, e)
        } finally {
            try {
                close(generatedKeyResultSet, preparedStatement)
            } catch (e: Exception) {
                throw translateException("update", sql, e)
            }

        }
    }

    @Throws(SQLException::class)
    private fun close(rs: ResultSet?) {
        rs?.close()
    }

    @Throws(SQLException::class)
    private fun close(statement: Statement?) {
        statement?.close()
    }

    @Throws(SQLException::class)
    private fun close(rs: ResultSet?, statement: Statement?) {
        try {
            close(rs)
        } finally {
            close(statement)
        }
    }

    override fun close() {
        try {
            LOGGER.debug("Connection [{}] - close", connectionNumber)
            connection.close()
        } catch (e: SQLException) {
            throw translateException("close", "", e)
        }

    }

    override fun commit() {
        try {
            LOGGER.debug("Connection [{}] - commit", connectionNumber)
            connection.commit()
        } catch (e: SQLException) {
            throw translateException("commit", "", e)
        }

    }

    override fun rollback() {
        try {
            LOGGER.debug("Connection [{}] - rollback", connectionNumber)
            connection.rollback()
        } catch (e: SQLException) {
            throw translateException("rollback", "", e)
        }

    }

    override fun setAutoCommit(autoCommit: Boolean) {
        try {
            LOGGER.debug("Connection [{}] - setAutoCommit [{}]", connectionNumber, autoCommit)
            connection.autoCommit = autoCommit
        } catch (e: SQLException) {
            throw translateException("setAutoCommit", "", e)
        }

    }

    override val isClosed: Boolean
        get() {
            try {
                return connection.isClosed
            } catch (e: SQLException) {
                throw translateException("isClosed", "", e)
            }

        }

    companion object {

        private val EMPTY_STRING_ARRAY = arrayOfNulls<String>(0)
        private val LOGGER = LoggerFactory.getLogger(DataSourceConnectionImpl::class.java)
        private var COUNT = 0L

        fun translateException(task: String, sql: String, ex: Exception): RuntimeException {
            if (ex is JpoException) {
                return ex
            }
            if (ex is SQLException) {
                return SpringBasedSQLStateSQLExceptionTranslator.doTranslate(task, sql, ex)
            }
            return JpoSqlException(ex)
        }
    }
}
