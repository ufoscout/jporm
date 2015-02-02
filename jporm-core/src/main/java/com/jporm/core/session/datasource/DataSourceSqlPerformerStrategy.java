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
package com.jporm.core.session.datasource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.exception.JpoException;
import com.jporm.core.exception.sql.JpoSqlException;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.session.BatchPreparedStatementSetter;
import com.jporm.core.session.GeneratedKeyReader;
import com.jporm.core.session.PreparedStatementSetter;
import com.jporm.core.session.SqlPerformerStrategy;
import com.jporm.core.session.datasource.exception.SpringBasedSQLStateSQLExceptionTranslator;
import com.jporm.sql.dialect.statement.StatementStrategy;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 *
 * {@link SqlPerformerStrategy} implementation using java.sql.Connection as backend.
 */
public class DataSourceSqlPerformerStrategy implements SqlPerformerStrategy, DataSourceConnectionCaller {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final DataSourceSessionProvider dataSourceSessionProvider;

	public DataSourceSqlPerformerStrategy(final DataSourceSessionProvider dataSourceSessionProvider) {
		this.dataSourceSessionProvider = dataSourceSessionProvider;
	}

	@Override
	public void execute(final String sql) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		PreparedStatement preparedStatement = null;
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false, this);
		try {
			preparedStatement = conn.prepareStatement( sql );
//			preparedStatement.setQueryTimeout(timeout);
			preparedStatement.execute();
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw translateException("execute", sql, e); //$NON-NLS-1$
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("execute", sql, e); //$NON-NLS-1$
			} finally {
				conn.close(this);
			}
		}
	}

	@Override
	public <T> T query(final String sql, final PreparedStatementSetter pss, final ResultSetReader<T> rse) 	throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(true, this);
		try {
			preparedStatement = conn.prepareStatement( sql );
			pss.set(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			return rse.read(resultSet);
		} catch (Exception e) {
			throw translateException("query", sql, e); //$NON-NLS-1$
		} finally {
			try {
				if ((resultSet!=null) && !resultSet.isClosed()) {
					resultSet.close();
				}
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("query", sql, e); //$NON-NLS-1$
			} finally {
				conn.close(this);
			}
		}
	}

	@Override
	public int update(final String sql, final PreparedStatementSetter pss) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		DataSourceConnectionImpl conn = dataSourceSessionProvider.getConnection(false, this);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement( sql );
//			preparedStatement.setQueryTimeout(timeout);
			pss.set(preparedStatement);
			int result = preparedStatement.executeUpdate();
			conn.commit();
			return result;
		} catch (Exception e) {
			conn.rollback();
			throw translateException("update", sql, e); //$NON-NLS-1$
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("update", sql, e); //$NON-NLS-1$
			} finally {
				conn.close(this);
			}
		}
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyExtractor, final StatementStrategy statementStrategy, final PreparedStatementSetter pss) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		DataSourceConnectionImpl conn = dataSourceSessionProvider.getConnection(false, this);
		ResultSet generatedKeyResultSet = null;
		PreparedStatement preparedStatement = null;
		int result = 0;
		try {
			preparedStatement = conn.prepareStatement( sql , generatedKeyExtractor.generatedColumnNames(), statementStrategy);
//			preparedStatement.setQueryTimeout(timeout);
			pss.set(preparedStatement);
			result = preparedStatement.executeUpdate();
			generatedKeyResultSet = preparedStatement.getGeneratedKeys();
			generatedKeyExtractor.read(generatedKeyResultSet);
			conn.commit();
			return result;
		} catch (Exception e) {
			conn.rollback();
			throw translateException("update", sql, e); //$NON-NLS-1$
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
				if ((generatedKeyResultSet!=null) && !generatedKeyResultSet.isClosed()) {
					generatedKeyResultSet.close();
				}
			} catch (Exception e) {
				throw translateException("update", sql, e); //$NON-NLS-1$
			} finally {
				conn.close(this);
			}
		}
	}

	@Override
	public int[] batchUpdate(final Stream<String> sqls) throws JpoException {
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false, this);
		DataSourceStatement _statement = null;
		try {
			DataSourceStatement statement = conn.createStatement();
			_statement = statement;
//			statement.setQueryTimeout(timeout);
			sqls.forEach(sql -> statement.addBatch(sql));
			int[] result = statement.executeBatch();
			conn.commit();
			return result;
		} catch (Exception e) {
			conn.rollback();
			throw translateException("batchUpdate", "", e); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			try {
				if (_statement!=null) {
					_statement.close();
				}
			} catch (Exception e) {
				throw translateException("batchUpdate", "", e); //$NON-NLS-1$ //$NON-NLS-2$
			} finally {
				conn.close(this);
			}
		}
	}


	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> args) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false, this);
		PreparedStatement _preparedStatement = null;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement( sql );
			_preparedStatement = preparedStatement;
//			preparedStatement.setQueryTimeout(timeout);
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
			conn.commit();
			return result;
		} catch (Exception e) {
			conn.rollback();
			throw translateException("batchUpdate", sql, e); //$NON-NLS-1$
		} finally {
			try {
				if (_preparedStatement!=null) {
					_preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("batchUpdate", sql, e); //$NON-NLS-1$
			} finally {
				conn.close(this);
			}
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		DataSourceConnection conn = dataSourceSessionProvider.getConnection(false, this);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement( sql );
//			preparedStatement.setQueryTimeout(timeout);
			for (int i=0; i<psc.getBatchSize(); i++) {
				psc.set(preparedStatement, i);
				preparedStatement.addBatch();
			}
			int[] result = preparedStatement.executeBatch();
			conn.commit();
			return result;
		} catch (Exception e) {
			conn.rollback();
			throw translateException("batchUpdate", sql, e); //$NON-NLS-1$
		} finally {
			try {
				if (preparedStatement!=null) {
					preparedStatement.close();
				}
			} catch (Exception e) {
				throw translateException("batchUpdate", sql, e); //$NON-NLS-1$
			} finally {
				conn.close(this);
			}
		}
	}

	private JpoException translateException(final String task, final String sql, final Exception ex) {
		if (ex instanceof JpoException) {
			return (JpoException) ex;
		}
		if (ex instanceof SQLException) {
			return SpringBasedSQLStateSQLExceptionTranslator.doTranslate(task, sql, (SQLException) ex);
		}
		return new JpoSqlException(ex);
	}

}
