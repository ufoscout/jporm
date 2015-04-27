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
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.sql.JpoSqlException;
import com.jporm.commons.core.io.jdbc.JdbcResultSet;
import com.jporm.commons.core.io.jdbc.JdbcStatement;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.commons.core.util.SpringBasedSQLStateSQLExceptionTranslator;
import com.jporm.rm.session.Session;
import com.jporm.rm.session.SqlPerformerStrategy;
import com.jporm.rm.transaction.TransactionCallback;
import com.jporm.sql.dialect.statement.StatementStrategy;
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
 * {@link SqlPerformerStrategy} implementation using java.sql.Connection as backend.
 */
public class DataSourceSqlPerformerStrategy implements SqlPerformerStrategy {

	private final static Logger logger = LoggerFactory.getLogger(DataSourceSqlPerformerStrategy.class);
	private final DataSourceConnectionProvider dataSourceSessionProvider;
	private final StatementStrategy statementStrategy;

	public DataSourceSqlPerformerStrategy(final DataSourceConnectionProvider dataSourceSessionProvider, StatementStrategy statementStrategy) {
		this.dataSourceSessionProvider = dataSourceSessionProvider;
		this.statementStrategy = statementStrategy;
	}

	@Override
	public void execute(final String sql) throws JpoException {
		logger.debug("Execute query: [{}]", sql);
		PreparedStatement preparedStatement = null;
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false);
		try {
			preparedStatement = conn.prepareStatement( sql );
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
			} finally {
				conn.close();
			}
		}
	}

	@Override
	public <T> T query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse) 	throws JpoException {
		logger.debug("Execute query: [{}]", sql);
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(true);
		try {
			preparedStatement = conn.prepareStatement( sql );
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
			} finally {
				conn.close();
			}
		}
	}

	@Override
	public int update(final String sql, final StatementSetter pss) throws JpoException {
		logger.debug("Execute query: [{}]", sql);
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement( sql );
			pss.set(new JdbcStatement(preparedStatement));
			int result = preparedStatement.executeUpdate();
			return result;
		} catch (Exception e) {
			throw translateException("update", sql, e);
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("update", sql, e);
			} finally {
				conn.close();
			}
		}
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyExtractor, final StatementSetter pss) throws JpoException {
		logger.debug("Execute query: [{}]", sql);
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false);
		ResultSet generatedKeyResultSet = null;
		PreparedStatement preparedStatement = null;
		int result = 0;
		try {
			preparedStatement = conn.prepareStatement( sql , generatedKeyExtractor.generatedColumnNames(), statementStrategy);
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
			} finally {
				conn.close();
			}
		}
	}

	@Override
	public int[] batchUpdate(final Stream<String> sqls) throws JpoException {
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false);
		Statement _statement = null;
		try {
			Statement statement = conn.createStatement();
			_statement = statement;
			sqls.forEach(sql -> {
				try {
					statement.addBatch(sql);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			conn.setReadOnly(false);
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
			} finally {
				conn.close();
			}
		}
	}


	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> args) throws JpoException {
		logger.debug("Execute query: [{}]", sql);
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false);
		PreparedStatement _preparedStatement = null;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement( sql );
			_preparedStatement = preparedStatement;
			args.forEach(arg -> {
				try {
					int i = 0;
					for (Object value : arg) {
						preparedStatement.setObject(++i, value);
					}
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
			} finally {
				conn.close();
			}
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		logger.debug("Execute query: [{}]", sql);
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement( sql );
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
			} finally {
				conn.close();
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
	public <T> T doInTransaction(final Session session, final TransactionDefinition transactionDefinition, final TransactionCallback<T> transactionCallback) {
		logger.debug("Starting new Transaction"); //$NON-NLS-1$
		T result = null;
		Transaction tx = new DataSourceTransaction(dataSourceSessionProvider, transactionDefinition);
		try {
			result = transactionCallback.doInTransaction(session);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
		return result;
	}

}
