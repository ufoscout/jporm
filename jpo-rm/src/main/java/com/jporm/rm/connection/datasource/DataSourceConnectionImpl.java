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
package com.jporm.rm.connection.datasource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.commons.core.exception.sql.JpoSqlException;
import com.jporm.commons.core.io.jdbc.JdbcResultSet;
import com.jporm.commons.core.io.jdbc.JdbcStatement;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.util.SpringBasedSQLStateSQLExceptionTranslator;
import com.jporm.rm.connection.Connection;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;

/**
 *
 * @author Francesco Cina
 *
 *         02/lug/2011
 *
 *         {@link Connection} implementation using java.sql.Connection as
 *         backend.
 */
public class DataSourceConnectionImpl implements DataSourceConnection {

	private final static String[] EMPTY_STRING_ARRAY = new String[0];
	private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceConnectionImpl.class);
	private static long COUNT = 0l;

	private final long connectionNumber = COUNT++;
	private final DBProfile dbType;
	private final java.sql.Connection connection;
	private int timeout = -1;
	private long expireInstant = -1;

	public DataSourceConnectionImpl(final java.sql.Connection connection, final DBProfile dbType) {
		this.connection = connection;
		this.dbType = dbType;
	}

	@Override
	public int[] batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) throws JpoException {
		Statement _statement = null;
		try {
			final Statement statement = connection.createStatement();
			setTimeout(statement);
			_statement = statement;
			sqls.forEach(sql -> {
				try {
					sql = sqlPreProcessor.apply(sql);
					LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql);
					statement.addBatch(sql);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			});
			connection.setReadOnly(false);
			final int[] result = statement.executeBatch();
			return result;
		} catch (final Exception e) {
			throw translateException("batchUpdate", "", e);
		} finally {
			try {
				close(_statement);
			} catch (final Exception e) {
				throw translateException("batchUpdate", "", e);
			}
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			setTimeout(preparedStatement);
			for (int i = 0; i < psc.getBatchSize(); i++) {
				psc.set(new JdbcStatement(preparedStatement), i);
				preparedStatement.addBatch();
			}
			final int[] result = preparedStatement.executeBatch();
			return result;
		} catch (final Exception e) {
			throw translateException("batchUpdate", sql, e);
		} finally {
			try {
				close(preparedStatement);
			} catch (final Exception e) {
				throw translateException("batchUpdate", sql, e);
			}
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final Collection<Consumer<com.jporm.types.io.Statement>> statementSetters) throws JpoException {
		LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql);
		PreparedStatement _preparedStatement = null;
		try {
			final PreparedStatement preparedStatement = connection.prepareStatement(sql);
			_preparedStatement = preparedStatement;
			setTimeout(preparedStatement);
			statementSetters.forEach(statementSetter -> {
				try {
					statementSetter.accept(new JdbcStatement(preparedStatement));
					preparedStatement.addBatch();
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			});
			final int[] result = preparedStatement.executeBatch();
			return result;
		} catch (final Exception e) {
			throw translateException("batchUpdate", sql, e);
		} finally {
			try {
				close(_preparedStatement);
			} catch (final Exception e) {
				throw translateException("batchUpdate", sql, e);
			}
		}
	}

	@Override
	public void execute(final String sql) throws JpoException {
		LOGGER.debug("Connection [{}] - Execute sql: [{}]", connectionNumber, sql);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			setTimeout(preparedStatement);
			preparedStatement.execute();
		} catch (final Exception e) {
			throw translateException("execute", sql, e);
		} finally {
			try {
				close(preparedStatement);
			} catch (final Exception e) {
				throw translateException("execute", sql, e);
			}
		}
	}

	private int getRemainingTimeoutSeconds(final long fromInstantMillis) {
		throwExceptionIfTimedOut(fromInstantMillis);
		final int diff = (int) (expireInstant - fromInstantMillis + 999) / 1000;
		return diff;
	}

	@Override
	public <T> T query(final String sql, final Consumer<com.jporm.types.io.Statement> pss, final Function<com.jporm.types.io.ResultSet, T> rse)
			throws JpoException {
		LOGGER.debug("Connection [{}] - Execute query: [{}]", connectionNumber, sql);
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			setTimeout(preparedStatement);
			pss.accept(new JdbcStatement(preparedStatement));
			resultSet = preparedStatement.executeQuery();
			return rse.apply(new JdbcResultSet(resultSet));
		} catch (final Exception e) {
			throw translateException("query", sql, e);
		} finally {
			try {
				close(resultSet, preparedStatement);
				LOGGER.debug("Connection [{}] - close statements", connectionNumber);
			} catch (final Exception e) {
				throw translateException("query", sql, e);
			}
		}
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
		try {
			LOGGER.debug("Connection [{}] - set readOnly mode to [{}]", connectionNumber, readOnly);
			connection.setReadOnly(readOnly);
		} catch (final SQLException e) {
			throw translateException("setTransactionIsolation", "", e);
		}
	}

	@Override
	public void setTimeout(final int timeout) {
		LOGGER.debug("Connection [{}] - set timeout to [{}]", connectionNumber, timeout);
		this.timeout = timeout;
		expireInstant = System.currentTimeMillis() + timeout * 1000;
	}

	private void setTimeout(final Statement statement) throws SQLException {
		if (timeout >= 0) {
			statement.setQueryTimeout(getRemainingTimeoutSeconds(System.currentTimeMillis()));
		}
	}

	@Override
	public void setTransactionIsolation(final TransactionIsolation isolationLevel) {
		try {
			LOGGER.debug("Connection [{}] - set transaction isolation to [{}]", connectionNumber, isolationLevel);
			connection.setTransactionIsolation(isolationLevel.getTransactionIsolation());
		} catch (final SQLException e) {
			throw translateException("setTransactionIsolation", "", e);
		}

	}

	private void throwExceptionIfTimedOut(final long fromInstantMillis) {
		if (fromInstantMillis >= expireInstant) {
			throw new JpoTransactionTimedOutException("Transaction timed out.");
		}
	}

	public static RuntimeException translateException(final String task, final String sql, final Exception ex) {
		if (ex instanceof JpoException) {
			return (JpoException) ex;
		}
		if (ex instanceof SQLException) {
			return SpringBasedSQLStateSQLExceptionTranslator.doTranslate(task, sql, (SQLException) ex);
		}
		return new JpoSqlException(ex);
	}

	@Override
	public int update(String sql, Consumer<com.jporm.types.io.Statement> pss) {
		LOGGER.debug("Connection [{}] - Execute update query: [{}]", connectionNumber, sql);
		final ResultSet generatedKeyResultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = dbType.getStatementStrategy().prepareStatement(connection, sql, EMPTY_STRING_ARRAY);
			setTimeout(preparedStatement);
			pss.accept(new JdbcStatement(preparedStatement));
			return preparedStatement.executeUpdate();
		} catch (final Exception e) {
			throw translateException("update", sql, e);
		} finally {
			try {
				close(generatedKeyResultSet, preparedStatement);
			} catch (final Exception e) {
				throw translateException("update", sql, e);
			}
		}
	}

	@Override
	public <R> R update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<com.jporm.types.io.Statement> pss)
			throws JpoException {
		LOGGER.debug("Connection [{}] - Execute update query: [{}]", connectionNumber, sql);
		ResultSet generatedKeyResultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			final String[] generatedColumnNames = generatedKeyReader.generatedColumnNames();

			preparedStatement = dbType.getStatementStrategy().prepareStatement(connection, sql, generatedColumnNames);
			setTimeout(preparedStatement);
			pss.accept(new JdbcStatement(preparedStatement));
			final int result = preparedStatement.executeUpdate();
			generatedKeyResultSet = preparedStatement.getGeneratedKeys();
			return generatedKeyReader.read(new JdbcResultSet(generatedKeyResultSet), result);
		} catch (final Exception e) {
			throw translateException("update", sql, e);
		} finally {
			try {
				close(generatedKeyResultSet, preparedStatement);
			} catch (final Exception e) {
				throw translateException("update", sql, e);
			}
		}
	}

	private void close(ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
	}

	private void close(Statement statement) throws SQLException {
		if (statement != null) {
			statement.close();
		}
	}

	private void close(ResultSet rs, Statement statement) throws SQLException {
		try {
			close(rs);
		} finally {
			close(statement);
		}
	}

	@Override
	public void close() {
		try {
			LOGGER.debug("Connection [{}] - close", connectionNumber);
			connection.close();
		} catch (final SQLException e) {
			throw translateException("close", "", e);
		}

	}

	@Override
	public void commit() {
		try {
			LOGGER.debug("Connection [{}] - commit", connectionNumber);
			connection.commit();
		} catch (final SQLException e) {
			throw translateException("commit", "", e);
		}
	}

	@Override
	public void rollback() {
		try {
			LOGGER.debug("Connection [{}] - rollback", connectionNumber);
			connection.rollback();
		} catch (final SQLException e) {
			throw translateException("rollback", "", e);
		}
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		try {
			LOGGER.debug("Connection [{}] - setAutoCommit [{}]", connectionNumber, autoCommit);
			connection.setAutoCommit(autoCommit);
		} catch (final SQLException e) {
			throw translateException("setAutoCommit", "", e);
		}
	}

	@Override
	public boolean isClosed() {
		try {
			return connection.isClosed();
		} catch (final SQLException e) {
			throw translateException("isClosed", "", e);
		}
	}
}
