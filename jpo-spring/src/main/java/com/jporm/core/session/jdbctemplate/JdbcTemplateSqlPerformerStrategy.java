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
package com.jporm.core.session.jdbctemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.io.jdbc.JdbcResultSet;
import com.jporm.commons.core.io.jdbc.JdbcStatement;
import com.jporm.commons.core.transaction.TransactionDefinition;
import com.jporm.commons.core.transaction.TransactionIsolation;
import com.jporm.commons.core.transaction.TransactionPropagation;
import com.jporm.core.session.Session;
import com.jporm.core.session.SqlPerformerStrategy;
import com.jporm.core.transaction.TransactionCallback;
import com.jporm.sql.dialect.statement.StatementStrategy;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.StatementSetter;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 *
 * ISqlExecutor implementation using JdbcTemplate as backend
 */
public class JdbcTemplateSqlPerformerStrategy implements SqlPerformerStrategy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final StatementStrategy statementStrategy;
	private JdbcTemplate jdbcTemplate;
	private final PlatformTransactionManager platformTransactionManager;

	public JdbcTemplateSqlPerformerStrategy(final JdbcTemplate jdbcTemplate, PlatformTransactionManager platformTransactionManager, StatementStrategy statementStrategy) {
		this.jdbcTemplate = jdbcTemplate;
		this.platformTransactionManager = platformTransactionManager;
		this.statementStrategy = statementStrategy;
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
	public <T> T query(final String sql, final StatementSetter pss, final ResultSetReader<T> rse)	throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			return jdbcTemplate.query(sql, new org.springframework.jdbc.core.PreparedStatementSetter() {
				@Override
				public void setValues(final PreparedStatement ps) throws SQLException {
					pss.set(new JdbcStatement(ps));
				}
			}, new ResultSetReaderWrapper<T>(rse) );
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int update(final String sql, final StatementSetter pss) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			return jdbcTemplate.update(sql, new org.springframework.jdbc.core.PreparedStatementSetter() {
				@Override
				public void setValues(final PreparedStatement ps) throws SQLException {
					pss.set(new JdbcStatement(ps));
				}
			});
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader<?> generatedKeyReader, final StatementSetter pss) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			final org.springframework.jdbc.core.PreparedStatementCreator psc = new org.springframework.jdbc.core.PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					PreparedStatement ps = null;
					ps = statementStrategy.prepareStatement(con, sql, generatedKeyReader.generatedColumnNames());
					pss.set(new JdbcStatement(ps));
					return ps;
				}
			};

			return jdbcTemplate.execute(psc, new PreparedStatementCallback<Integer>() {
				@Override
				public Integer doInPreparedStatement(final PreparedStatement ps) throws SQLException {
					int rows = ps.executeUpdate();
					ResultSet keys = ps.getGeneratedKeys();
					if (keys != null) {
						try {
							generatedKeyReader.read(new JdbcResultSet(keys));
						}
						finally {
							JdbcUtils.closeResultSet(keys);
						}
					}
					return rows;
				}
			});
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int[] batchUpdate(final Stream<String> sqls) throws JpoException {
		String[] stringArray = sqls.toArray(size -> new String[size]);
		try {
			return jdbcTemplate.batchUpdate(stringArray);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> argsStream) throws JpoException {
		logger.debug("Execute query: [{}]", sql); //$NON-NLS-1$
		List<Object[]> args = argsStream.collect(Collectors.toList());
		try {
			final BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {

				@Override
				public void setValues(final PreparedStatement ps, final int i) throws SQLException {
					int count = 0;
					for ( final Object object : args.get(i) ) {
						ps.setObject(++count, object);
					}
				}

				@Override
				public int getBatchSize() {
					return args.size();
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
				public void setValues(final PreparedStatement ps, final int i) throws SQLException {
					psc.set(new JdbcStatement(ps), i);
				}
				@Override
				public int getBatchSize() {
					return psc.getBatchSize();
				}
			};
			return jdbcTemplate.batchUpdate(sql, bpss);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public <T> T doInTransaction(final Session session, final TransactionDefinition transactionDefinition, final TransactionCallback<T> transactionCallback) {

		try {
			DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
			setIsolationLevel(definition , transactionDefinition.getIsolationLevel());
			setPropagation( definition , transactionDefinition.getPropagation() );
			setTimeout( definition , transactionDefinition.getTimeout() );
			definition.setReadOnly( transactionDefinition.isReadOnly() );

			TransactionTemplate tt = new TransactionTemplate(platformTransactionManager, definition);
			return tt.execute(transactionStatus -> transactionCallback.doInTransaction(session));
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}

	}

	private void setTimeout(final DefaultTransactionDefinition definition, final int timeout) {
		if (timeout >= 0) {
			definition.setTimeout(timeout);
		}
	}

	private void setPropagation(final DefaultTransactionDefinition definition, final TransactionPropagation propagation) {
		switch (propagation) {
		case MANDATORY:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY );
			break;
		case NESTED:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED );
			break;
		case NEVER:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_NEVER );
			break;
		case NOT_SUPPORTED:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_NOT_SUPPORTED );
			break;
		case REQUIRED:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED );
			break;
		case REQUIRES_NEW:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW );
			break;
		case SUPPORTS:
			definition.setPropagationBehavior( org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS );
			break;
		default:
			throw new JpoException("Unknown Transaction Propagation: " + propagation); //$NON-NLS-1$
		}

	}

	private void setIsolationLevel(final DefaultTransactionDefinition definition,
			final TransactionIsolation isolationLevel) {
		if (isolationLevel!=TransactionIsolation.DEFAULT) {
			definition.setIsolationLevel(isolationLevel.getTransactionIsolation());
		}

	}

}
