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
package com.jporm.rx.core.session.datasource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

import com.jporm.commons.core.async.AsyncTaskExecutor;
import com.jporm.commons.core.io.jdbc.JdbcResultSet;
import com.jporm.commons.core.io.jdbc.JdbcStatement;
import com.jporm.commons.core.util.SpringBasedSQLStateSQLExceptionTranslator;
import com.jporm.rx.core.connection.Connection;
import com.jporm.rx.core.connection.UpdateResult;
import com.jporm.rx.core.connection.UpdateResultImpl;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

public class DatasourceConnection implements Connection {

	private final java.sql.Connection sqlConnection;
	private final AsyncTaskExecutor executor;

	public DatasourceConnection(java.sql.Connection sqlConnection, AsyncTaskExecutor executor) {
		this.sqlConnection = sqlConnection;
		this.executor = executor;
	}

	@Override
	public <T> CompletableFuture<T> query(String sql, final StatementSetter pss, ResultSetReader<T> rse) {
		return executor.execute(() -> {
				java.sql.ResultSet resultSet = null;
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = sqlConnection.prepareStatement( sql );
					pss.set(new JdbcStatement(preparedStatement));
					resultSet = preparedStatement.executeQuery();
					return rse.read(new JdbcResultSet(resultSet));
				} catch (SQLException e) {
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
			java.sql.ResultSet generatedKeyResultSet = null;
			PreparedStatement preparedStatement = null;
			int result = 0;
			try {
				preparedStatement = sqlConnection.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS);
				pss.set(new JdbcStatement(preparedStatement));
				result = preparedStatement.executeUpdate();
				generatedKeyResultSet = preparedStatement.getGeneratedKeys();
				generatedKeyReader.read(new JdbcResultSet(generatedKeyResultSet));
				return new UpdateResultImpl(result);
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("rollback", "", e);
			} finally {
				try {
					if (preparedStatement!=null) {
						preparedStatement.close();
					}
					if ((generatedKeyResultSet!=null) && !generatedKeyResultSet.isClosed()) {
						generatedKeyResultSet.close();
					}
				} catch (SQLException e) {
					throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("rollback", "", e);
				}
			}
		});
	}

	@Override
	public CompletableFuture<Void> close() {
		return executor.execute(() -> {
			try {
				sqlConnection.close();
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("rollback", "", e);
			}
		});
	}

	@Override
	public CompletableFuture<Void> commit() {
		return executor.execute(() -> {
			try {
				sqlConnection.commit();
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("rollback", "", e);
			}
		});
	}

	@Override
	public CompletableFuture<Void> rollback() {
		return executor.execute(() -> {
			try {
				sqlConnection.rollback();
			} catch (SQLException e) {
				throw SpringBasedSQLStateSQLExceptionTranslator.doTranslate("rollback", "", e);
			}
		});
	}

}
