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
package com.jporm.rm.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.JdbcUtils;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.io.jdbc.JdbcResultSet;
import com.jporm.commons.core.io.jdbc.JdbcStatement;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.rm.connection.Connection;
import com.jporm.sql.dialect.StatementStrategy;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.Statement;

/**
 *
 * @author Francesco Cina
 *
 *         02/lug/2011
 *
 *         ISqlExecutor implementation using JdbcTemplate as backend
 */
public class JdbcTemplateConnection implements Connection {

	private final static String[] EMPTY_STRING_ARRAY = new String[0];
	private final static Logger logger = LoggerFactory.getLogger(JdbcTemplateConnection.class);
	private final StatementStrategy statementStrategy;
	private final JdbcTemplate jdbcTemplate;

	public JdbcTemplateConnection(final JdbcTemplate jdbcTemplate, final StatementStrategy statementStrategy) {
		this.jdbcTemplate = jdbcTemplate;
		this.statementStrategy = statementStrategy;
	}

	@Override
	public int[] batchUpdate(final Collection<String> sqls, Function<String, String> sqlPreProcessor) throws JpoException {
		final String[] stringArray = new String[sqls.size()];
		{
			int count = 0;
			for (final String sql : sqls) {
				final String processedSql = sqlPreProcessor.apply(sql);
				logger.debug("Execute batch update query: [{}]", processedSql);
				stringArray[count++] = processedSql;
			}
		}
		try {
			return jdbcTemplate.batchUpdate(stringArray);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final Collection<Consumer<Statement>> statementSetter) throws JpoException {
		logger.debug("Execute query: [{}]", sql);
		List<Consumer<Statement>> args;
		if (statementSetter instanceof List) {
			args = (List<Consumer<Statement>>) statementSetter;
		} else {
			args = new ArrayList<>(statementSetter);
		}
		try {
			final BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {

				@Override
				public int getBatchSize() {
					return args.size();
				}

				@Override
				public void setValues(final PreparedStatement ps, final int i) throws SQLException {
					args.get(i).accept(new JdbcStatement(ps));
				}
			};
			return jdbcTemplate.batchUpdate(sql, bpss);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final com.jporm.types.io.BatchPreparedStatementSetter psc) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			final BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
				@Override
				public int getBatchSize() {
					return psc.getBatchSize();
				}

				@Override
				public void setValues(final PreparedStatement ps, final int i) throws SQLException {
					psc.set(new JdbcStatement(ps), i);
				}
			};
			return jdbcTemplate.batchUpdate(sql, bpss);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public void execute(final String sql) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			jdbcTemplate.execute(sql);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public <T> T query(final String sql, final Consumer<Statement> pss, final Function<com.jporm.types.io.ResultSet, T> rse) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			return jdbcTemplate.query(sql, new org.springframework.jdbc.core.PreparedStatementSetter() {
				@Override
				public void setValues(final PreparedStatement ps) throws SQLException {
					pss.accept(new JdbcStatement(ps));
				}
			}, new ResultSetReaderWrapper<>(rse));
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public void setReadOnly(final boolean readOnly) {
	}

	@Override
	public void setTimeout(final int timeout) {
	}

	@Override
	public void setTransactionIsolation(final TransactionIsolation isolationLevel) {
	}

	@Override
	public <R> R update(final String sql, final GeneratedKeyReader<R> generatedKeyReader, final Consumer<Statement> pss) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			final String[] generatedColumnNames = generatedKeyReader.generatedColumnNames();
			final org.springframework.jdbc.core.PreparedStatementCreator psc = new org.springframework.jdbc.core.PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final java.sql.Connection con) throws SQLException {
					PreparedStatement ps = null;
					ps = statementStrategy.prepareStatement(con, sql, generatedColumnNames);
					pss.accept(new JdbcStatement(ps));
					return ps;
				}
			};

			return jdbcTemplate.execute(psc, new PreparedStatementCallback<R>() {
				@Override
				public R doInPreparedStatement(final PreparedStatement ps) throws SQLException {
					ResultSet keys = null;
					try {
						final int rows = ps.executeUpdate();
						keys = ps.getGeneratedKeys();
						return generatedKeyReader.read(new JdbcResultSet(keys), rows);
					} finally {
						JdbcUtils.closeResultSet(keys);
					}
				}
			});
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int update(String sql, Consumer<Statement> pss) {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			final org.springframework.jdbc.core.PreparedStatementCreator psc = new org.springframework.jdbc.core.PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final java.sql.Connection con) throws SQLException {
					PreparedStatement ps = null;
					ps = statementStrategy.prepareStatement(con, sql, EMPTY_STRING_ARRAY);
					pss.accept(new JdbcStatement(ps));
					return ps;
				}
			};
			return jdbcTemplate.execute(psc, new PreparedStatementCallback<Integer>() {
				@Override
				public Integer doInPreparedStatement(final PreparedStatement ps) throws SQLException {
					return ps.executeUpdate();
				}
			});
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

}
