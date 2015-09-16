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
package com.jporm.rm.session.datasource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.commons.core.exception.sql.JpoSqlException;
import com.jporm.commons.core.io.jdbc.JdbcResultSet;
import com.jporm.commons.core.io.jdbc.JdbcStatement;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.util.SpringBasedSQLStateSQLExceptionTranslator;
import com.jporm.rm.session.Connection;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 *
 * {@link Connection} implementation using java.sql.Connection as backend.
 */
public class DataSourceConnection implements Connection {

	private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceConnection.class);
	private static long COUNT = 0l;

	private final long connectionNumber = COUNT++;
	private final DBType dbType;
	private final java.sql.Connection connection;
	private int timeout = TransactionDefinition.TIMEOUT_DEFAULT;
	private long expireInstant = -1;

	public DataSourceConnection(final java.sql.Connection connection, DBType dbType) {
		this.connection = connection;
		this.dbType = dbType;
	}

	@Override
	public void execute(final String sql) throws JpoException {
		LOGGER.debug("Connection [{}] - Execute sql: [{}]", connectionNumber, sql);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement( sql );
			setTimeout(preparedStatement);
			preparedStatement.execute();
		} catch (Exception e) {
			throw translateException("execute", sql, e);
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("execute", sql, e);
			}
		}
	}

	@Override
	public <T> T query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) 	throws JpoException {
		LOGGER.debug("Connection [{}] - Execute query: [{}]", connectionNumber, sql);
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement( sql );
			setTimeout(preparedStatement);
			pss.set(new JdbcStatement(preparedStatement));
			resultSet = preparedStatement.executeQuery();
			return rse.read( new JdbcResultSet(resultSet) );
		} catch (Exception e) {
			throw translateException("query", sql, e);
		} finally {
			try {
				if ((resultSet!=null) && !resultSet.isClosed()) {
					resultSet.close();
				}
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("query", sql, e);
			}
		}
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyExtractor, final StatementSetter pss) throws JpoException {
		LOGGER.debug("Connection [{}] - Execute update query: [{}]", connectionNumber, sql);
		ResultSet generatedKeyResultSet = null;
		PreparedStatement preparedStatement = null;
		int result = 0;
		try {
			preparedStatement = dbType.getDBProfile().getStatementStrategy().prepareStatement(connection, sql, generatedKeyExtractor.generatedColumnNames());
			setTimeout(preparedStatement);
			pss.set(new JdbcStatement(preparedStatement));
			result = preparedStatement.executeUpdate();
			generatedKeyResultSet = preparedStatement.getGeneratedKeys();
			generatedKeyExtractor.read(new JdbcResultSet(generatedKeyResultSet));
			return result;
		} catch (Exception e) {
			throw translateException("update", sql, e);
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
				if ((generatedKeyResultSet!=null) && !generatedKeyResultSet.isClosed()) {
					generatedKeyResultSet.close();
				}
			} catch (Exception e) {
				throw translateException("update", sql, e);
			}
		}
	}

	@Override
	public int[] batchUpdate(final Collection<String> sqls) throws JpoException {
		Statement _statement = null;
		try {
			Statement statement = connection.createStatement();
			setTimeout(statement);
			_statement = statement;
			sqls.forEach(sql -> {
				try {
					LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql);
					statement.addBatch(sql);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			connection.setReadOnly(false);
			int[] result = statement.executeBatch();
			return result;
		} catch (Exception e) {
			throw translateException("batchUpdate", "", e);
		} finally {
			try {
				if (_statement!=null) {
					_statement.close();
				}
			} catch (Exception e) {
				throw translateException("batchUpdate", "", e);
			}
		}
	}


	@Override
	public int[] batchUpdate(final String sql, final Collection<StatementSetter> statementSetters) throws JpoException {
		LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql);
		PreparedStatement _preparedStatement = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement( sql );
			_preparedStatement = preparedStatement;
			setTimeout(preparedStatement);
			statementSetters.forEach(statementSetter -> {
				try {
					statementSetter.set(new JdbcStatement(preparedStatement));
					preparedStatement.addBatch();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			int[] result = preparedStatement.executeBatch();
			return result;
		} catch (Exception e) {
			throw translateException("batchUpdate", sql, e);
		} finally {
			try {
				if (_preparedStatement!=null) {
					_preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("batchUpdate", sql, e);
			}
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		LOGGER.debug("Connection [{}] - Execute batch update query: [{}]", connectionNumber, sql);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement( sql );
			setTimeout(preparedStatement);
			for (int i=0; i<psc.getBatchSize(); i++) {
				psc.set(new JdbcStatement(preparedStatement), i);
				preparedStatement.addBatch();
			}
			int[] result = preparedStatement.executeBatch();
			return result;
		} catch (Exception e) {
			throw translateException("batchUpdate", sql, e);
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("batchUpdate", sql, e);
			}
		}
	}

	private RuntimeException translateException(final String task, final String sql, final Exception ex) {
		if (ex instanceof JpoException) {
			return (JpoException) ex;
		}
		if (ex instanceof SQLException) {
			return SpringBasedSQLStateSQLExceptionTranslator.doTranslate(task, sql, (SQLException) ex);
		}
		return new JpoSqlException(ex);
	}

	@Override
	public void commit() {
		try {
			LOGGER.debug("Connection [{}] - commit", connectionNumber);
			connection.commit();
		} catch (SQLException e) {
			throw translateException("commit", "", e);
		}
	}

	@Override
	public void rollback() {
		try {
			LOGGER.debug("Connection [{}] - rollback", connectionNumber);
			connection.rollback();
		} catch (SQLException e) {
			throw translateException("rollback", "", e);
		}
	}

	@Override
	public void close() {
		try {
			LOGGER.debug("Connection [{}] - close", connectionNumber);
			connection.close();
		} catch (SQLException e) {
			throw translateException("close", "", e);
		}

	}

	@Override
	public void setTransactionIsolation(TransactionIsolation isolationLevel) {
		try {
			LOGGER.debug("Connection [{}] - set transaction isolation to [{}]", connectionNumber, isolationLevel);
			connection.setTransactionIsolation(isolationLevel.getTransactionIsolation());
		} catch (SQLException e) {
			throw translateException("setTransactionIsolation", "", e);
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
