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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoRollbackException;
import com.jporm.commons.core.exception.JpoTransactionTimedOutException;
import com.jporm.sql.dialect.statement.StatementStrategy;

/**
 *
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 9, 2013
 *
 * @author  - Francesco Cina
 * @version $Revision
 */
public class DataSourceConnectionImpl implements DataSourceConnection {
	private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceConnectionImpl.class);

	private final ConnectionWrapper connectionWrapper;
	private int connectionCallers;
	private boolean rollbackOnly = false;
	private boolean readOnly = false;
	private boolean valid = true;
	private long expireInstant = -1;

	public DataSourceConnectionImpl(final DataSource dataSource, final boolean readOnly) {
		connectionWrapper = new ConnectionWrapper(dataSource);
		setReadOnly(readOnly);
	}

	@Override
	public void setTransactionIsolation(final int transactionIsolation) throws JpoException {
		try {
			connectionWrapper.setTransactionIsolation(transactionIsolation);
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public boolean isClosed() throws JpoException {
		try {
			return connectionWrapper.isClosed();
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public void rollback() throws JpoException {
		if ((connectionCallers==1) && !isReadOnly()) {
			try {
				connectionWrapper.rollback();
			} catch (SQLException e) {
				throw new JpoException(e);
			}
		}
	}

	@Override
	public void commit() throws JpoException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Commit called. ConnectionCallers.size() = {}", connectionCallers);
			LOGGER.debug("isReadOnly() = {}", isReadOnly());
			LOGGER.debug("isRollbackOnly() = {}", isRollbackOnly());
			LOGGER.debug("isReadOnly() = {}", isReadOnly());
		}

		if ((connectionCallers==1) && !isReadOnly()) {
			if (isRollbackOnly()) {
				LOGGER.debug("Performing ROLLBACK ");
				rollback();
				throw new JpoRollbackException("Transaction rolled back because it has been marked as rollback-only"); //$NON-NLS-1$
			}
			try {

				LOGGER.debug("Performing COMMIT ");

				connectionWrapper.commit();
			} catch (SQLException e) {
				throw new JpoException(e);
			}
		}
	}

	@Override
	public PreparedStatement prepareStatement(final String sql) throws JpoException {
		try {
			return setTimeout(connectionWrapper.prepareStatement(sql));
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] generatedColumnNames, final StatementStrategy statementStrategy) throws JpoException {
		try {
			return setTimeout(connectionWrapper.prepareStatement(sql, generatedColumnNames, statementStrategy));
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public Statement createStatement() throws JpoException {
		try {
			return setTimeout(connectionWrapper.createStatement());
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public void addCaller() throws JpoException {
		connectionCallers++;
	}

	@Override
	public void close() throws JpoException {
		if (--connectionCallers == 0) {
			try {
				connectionWrapper.close();
				valid = false;
			} catch (SQLException e) {
				throw new JpoException(e);
			}
		}
	}

	@Override
	public void setRollbackOnly() throws JpoException {
		rollbackOnly = true;
	}

	@Override
	public void setReadOnly(final boolean readOnly) throws JpoException {
		this.readOnly = readOnly;
	}

	@Override
	public boolean isValid() throws JpoException {
		return valid;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @return the rollbackOnly
	 */
	@Override
	public boolean isRollbackOnly() {
		return rollbackOnly;
	}

	@Override
	public long getExpireInstant() {
		return expireInstant;
	}

	@Override
	public void setExpireInstant(long expireInstant) {
		this.expireInstant = expireInstant;
	}

	@Override
	public int getRemainingTimeoutSeconds(long fromInstantMillis) {
		throwExceptionifTimedOut(fromInstantMillis);
		int diff = (int) ((expireInstant - fromInstantMillis) + 999)/1000;
		//diff = diff>0 ? diff : 1;
		return diff;
	}

	public void throwExceptionifTimedOut(long fromInstantMillis) {
		if (fromInstantMillis >= expireInstant) {
			throw new JpoTransactionTimedOutException("Transaction timed out.");
		}
	}

	class ConnectionWrapper {

		private java.sql.Connection connection;
		private final DataSource dataSource;
		private Savepoint savepoint;

		ConnectionWrapper(final DataSource dataSource) {
			this.dataSource = dataSource;
		}

		public void setTransactionIsolation(final int transactionIsolation) throws SQLException {
			validateConnection();
			connection.setTransactionIsolation(transactionIsolation);
		}

		public void rollback() throws SQLException {
			if (connection!=null) {
				if (savepoint==null) {
					connection.rollback();
				} else {
					connection.rollback(savepoint);
				}

			}
		}

		public void rollback(final Savepoint rollbackToSavepoint) throws SQLException {
			if (connection!=null) {
				connection.rollback(rollbackToSavepoint);
			}
		}

		public void commit() throws SQLException {
			if (connection!=null) {
				connection.commit();
			}
		}

		public Statement createStatement() throws SQLException {
			validateConnection();
			return connection.createStatement();
		}

		public PreparedStatement prepareStatement(final String sql) throws SQLException {
			validateConnection();
			return connection.prepareStatement(sql);
		}

		public PreparedStatement prepareStatement(final String sql, final String[] generatedColumnNames, final StatementStrategy statementStrategy) throws SQLException {
			validateConnection();

			return statementStrategy.prepareStatement(connection, sql, generatedColumnNames);

		}

		public void close() throws SQLException {
			if (connection!=null) {
				connection.close();
			}
		}

		public boolean isClosed() throws SQLException {
			if (connection!=null) {
				return connection.isClosed();
			}
			return true;
		}

		private void validateConnection() throws SQLException {
			if (!isValid()) {
				throw new JpoException("Not possible to open a new java.sql.Connection"); //$NON-NLS-1$
			}
			if (isClosed()) {
				Connection sqlConn = dataSource.getConnection();
				if (!isReadOnly()) {
					savepoint = sqlConn.setSavepoint();
				}
				connection = sqlConn;
			}
		}

	}

	private <T extends Statement> T setTimeout(T statement) throws SQLException {
		if (expireInstant>0) {
			statement.setQueryTimeout(getRemainingTimeoutSeconds(System.currentTimeMillis()));
		}
		return statement;
	}
}
