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
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.exception.JpoException;
import com.jporm.core.exception.JpoRollbackException;
import com.jporm.sql.dialect.querytemplate.QueryTemplate;

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
	private final List<DataSourceConnectionCaller> connectionCallers = new ArrayList<DataSourceConnectionCaller>();
	private boolean rollbackOnly = false;
	private boolean readOnly = false;
	private boolean valid = true;

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
		if ((connectionCallers.size()==1) && !isReadOnly()) {
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
			LOGGER.debug("Commit called. ConnectionCallers.size() = {}", connectionCallers.size());
			LOGGER.debug("isReadOnly() = {}", isReadOnly());
			LOGGER.debug("isRollbackOnly() = {}", isRollbackOnly());
			LOGGER.debug("isReadOnly() = {}", isReadOnly());
		}

		if ((connectionCallers.size()==1) && !isReadOnly()) {
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
			return connectionWrapper.prepareStatement(sql);
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] generatedColumnNames, final QueryTemplate queryTemplate) throws JpoException {
		try {
			return connectionWrapper.prepareStatement(sql, generatedColumnNames, queryTemplate) ;
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public DataSourceStatement createStatement() throws JpoException {
		try {
			return new DataSourceStatementWrapper(connectionWrapper.createStatement() , this);
		} catch (SQLException e) {
			throw new JpoException(e);
		}
	}

	@Override
	public void addCaller(final DataSourceConnectionCaller connectionCaller) throws JpoException {
		connectionCallers.add(connectionCaller);
	}

	@Override
	public void close(final DataSourceConnectionCaller connectionCaller) throws JpoException {
		connectionCallers.remove(connectionCaller);
		if (connectionCallers.isEmpty()) {
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

		public PreparedStatement prepareStatement(final String sql, final String[] generatedColumnNames, final QueryTemplate queryTemplate) throws SQLException {
			validateConnection();

			return queryTemplate.prepareStatement(connection, sql, generatedColumnNames);
			//            return connection.prepareStatement(sql, generatedColumnNames);
			//            return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			//            return connection.prepareStatement(sql, new int[]{1});

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
}
