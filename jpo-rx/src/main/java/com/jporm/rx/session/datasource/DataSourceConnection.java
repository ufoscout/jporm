/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.rx.session.datasource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.commons.core.transaction.TransactionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.io.jdbc.JdbcResultSet;
import com.jporm.commons.core.io.jdbc.JdbcStatement;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.util.SpringBasedSQLStateSQLExceptionTranslator;
import com.jporm.rx.connection.Connection;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.connection.UpdateResultImpl;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class DataSourceConnection implements Connection {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConnection.class);
	private static long COUNT = 0l;

	private final long connectionNumber = COUNT++;
	private final java.sql.Connection sqlConnection;
	private final AsyncTaskExecutor executor;
	private final DBType dbType;
	private int timeout = TransactionDefinition.TIMEOUT_DEFAULT;
	private long expireInstant = -1;

	public DataSourceConnection(java.sql.Connection sqlConnection, DBType dbType, AsyncTaskExecutor executor) {
		this.sqlConnection = sqlConnection;
		this.dbType = dbType;
		this.executor = executor;
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, final StatementSetter pss, ResultSetReader<T> rse) {
		return executor.execute(() -> {
			LOGGER.debug("Connection [{}] - Execute query: [{}]", connectionNumber, sql);
				java.sql.ResultSet resultSet = null;
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = sqlConnection.prepareStatement( sql );
					setTimeout(preparedStatement);
					pss.set(new JdbcStatement(preparedStatement));
					resultSet = preparedStatement.executeQuery();
					return rse.read(new JdbcResultSet(resultSet));
				} catch (SQLException e) {
					LOGGER.error("Exception thrown during query execution", e);
					throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("query", "", e);
				} finally {
					try {
						if ((resultSet!=null) && !resultSet.isClosed()) {
							resultSet.close();
						}
						if (preparedStatement!=null) {
							preparedStatement.close();
						}
					} catch (SQLException e) {
						throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("query", "", e);
					}
				}
		});
	}

	@Override
	public CompletableFuture<UpdateResult> update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter pss) {
		return executor.execute(() -> {
			LOGGER.debug("Connection [{}] - Execute update query: [{}]", connectionNumber, sql);
			java.sql.ResultSet generatedKeyResultSet = null;
			PreparedStatement preparedStatement = null;
			int result = 0;
			try {
				preparedStatement = dbType.getDBProfile().getStatementStrategy().prepareStatement(sqlConnection, sql, generatedKeyReader.generatedColumnNames());
				setTimeout(preparedStatement);
				pss.set(new JdbcStatement(preparedStatement));
				result = preparedStatement.executeUpdate();
				generatedKeyResultSet = preparedStatement.getGeneratedKeys();
				generatedKeyReader.read(new JdbcResultSet(generatedKeyResultSet));
				return new UpdateResultImpl(result);
			} catch (SQLException e) {
				LOGGER.error("Exception thrown during update execution", e);
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("update", "", e);
			} finally {
				try {
					if (preparedStatement!=null) {
						preparedStatement.close();
					}
					if ((generatedKeyResultSet!=null) && !generatedKeyResultSet.isClosed()) {
						generatedKeyResultSet.close();
					}
				} catch (SQLException e) {
					throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("update", "", e);
				}
			}
		});
	}

	@Override
	public CompletableFuture<Void> close() {
		return executor.execute(() -> {
			try {
				LOGGER.debug("Connection [{}] - close", connectionNumber);
				sqlConnection.close();
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("close", "", e);
			}
		});
	}

	@Override
	public CompletableFuture<Void> commit() {
		return executor.execute(() -> {
			try {
				LOGGER.debug("Connection [{}] - commit", connectionNumber);
				sqlConnection.commit();
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("commit", "", e);
			}
		});
	}

	@Override
	public CompletableFuture<Void> rollback() {
		return executor.execute(() -> {
			try {
				LOGGER.debug("Connection [{}] - rollback", connectionNumber);
				sqlConnection.rollback();
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("rollback", "", e);
			}
		});
	}

	@Override
	public void setTransactionIsolation(TransactionIsolation isolation) {
		try {
			LOGGER.debug("Connection [{}] - set transaction isolation to [{}]", connectionNumber, isolation);
			sqlConnection.setTransactionIsolation(isolation.getTransactionIsolation());
		} catch (SQLException e) {
			throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("setTransactionIsolation", "", e);
		}
	}

	@Override
	public void setTimeout(int timeout) {
		LOGGER.debug("Connection [{}] - set timeout to [{}]", connectionNumber, timeout);
		this.timeout = timeout;
		expireInstant = System.currentTimeMillis() + (timeout*1000);
	}

	private int getRemainingTimeoutSeconds(long fromInstantMillis) {
		throwExceptionIfTimedOut(fromInstantMillis);
		int diff = (int) ((expireInstant - fromInstantMillis) + 999)/1000;
		return diff;
	}

	private void throwExceptionIfTimedOut(long fromInstantMillis) {
		if (fromInstantMillis >= expireInstant) {
			throw new JpoTransactionTimedOutException("Transaction timed out.");
		}
	}

	private void setTimeout(Statement statement) throws SQLException {
		if (timeout!=TransactionDefinition.TIMEOUT_DEFAULT) {
			statement.setQueryTimeout(getRemainingTimeoutSeconds(System.currentTimeMillis()));
		}
	}

}
