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
package com.jporm.session.jdbctemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.JdbcUtils;

import com.jporm.core.exception.JpoException;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.session.GeneratedKeyReader;
import com.jporm.core.session.PreparedStatementSetter;
import com.jporm.core.session.SqlPerformerStrategy;
import com.jporm.sql.dialect.querytemplate.QueryTemplate;

/**
 *
 * @author Francesco Cina
 *
 * 02/lug/2011
 *
 * ISqlExecutor implementation using JdbcTemplate as backend
 */
public class JdbcTemplateSqlPerformerStrategy extends SqlPerformerStrategy {

	private JdbcTemplateSessionProvider sessionProvider;

	public JdbcTemplateSqlPerformerStrategy(final JdbcTemplateSessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	@Override
	public void execute(final String sql, final int timeout) throws JpoException {
		getLogger().debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			getJdbcTemplate().setQueryTimeout(timeout);
			getJdbcTemplate().execute(sql);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public <T> T query(final String sql, final int timeout, final int maxRows, final PreparedStatementSetter pss, final ResultSetReader<T> rse)	throws JpoException {
		getLogger().debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			getJdbcTemplate().setMaxRows(maxRows);
			getJdbcTemplate().setQueryTimeout(timeout);
			return getJdbcTemplate().query(sql, new org.springframework.jdbc.core.PreparedStatementSetter() {
				@Override
				public void setValues(final PreparedStatement ps) throws SQLException {
					pss.set(ps);
				}
			}, new ResultSetReaderWrapper<T>(rse) );
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int update(final String sql, final int timeout, final PreparedStatementSetter pss) throws JpoException {
		getLogger().debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			getJdbcTemplate().setQueryTimeout(timeout);
			return getJdbcTemplate().update(sql, new org.springframework.jdbc.core.PreparedStatementSetter() {
				@Override
				public void setValues(final PreparedStatement ps) throws SQLException {
					pss.set(ps);
				}
			});
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int update(final String sql, final int timeout, final GeneratedKeyReader generatedKeyReader, final QueryTemplate queryTemplate, final PreparedStatementSetter pss) throws JpoException {
		getLogger().debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			final org.springframework.jdbc.core.PreparedStatementCreator psc = new org.springframework.jdbc.core.PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					PreparedStatement ps = null;
					ps = queryTemplate.prepareStatement(con, sql, generatedKeyReader.generatedColumnNames());
					pss.set(ps);
					return ps;
				}
			};

			return getJdbcTemplate().execute(psc, new PreparedStatementCallback<Integer>() {
				@Override
				public Integer doInPreparedStatement(final PreparedStatement ps) throws SQLException {
					int rows = ps.executeUpdate();
					ResultSet keys = ps.getGeneratedKeys();
					if (keys != null) {
						try {
							generatedKeyReader.read(keys);
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
	public int[] batchUpdate(final Stream<String> sqls, final int timeout) throws JpoException {
		String[] stringArray = sqls.toArray(size -> new String[size]);
		try {
			getJdbcTemplate().setQueryTimeout(timeout);
			return getJdbcTemplate().batchUpdate(stringArray);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> argsStream, final int timeout) throws JpoException {
		getLogger().debug("Execute query: [{}]", sql); //$NON-NLS-1$
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
			getJdbcTemplate().setQueryTimeout(timeout);
			return getJdbcTemplate().batchUpdate(sql, bpss);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final com.jporm.core.session.BatchPreparedStatementSetter psc, final int timeout) throws JpoException {
		getLogger().debug("Execute query: [{}]", sql); //$NON-NLS-1$
		try {
			final BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter() {
				@Override
				public void setValues(final PreparedStatement ps, final int i) throws SQLException {
					psc.set(ps, i);
				}
				@Override
				public int getBatchSize() {
					return psc.getBatchSize();
				}
			};
			getJdbcTemplate().setQueryTimeout(timeout);
			return getJdbcTemplate().batchUpdate(sql, bpss);
		} catch (final Exception e) {
			throw JdbcTemplateExceptionTranslator.doTranslate(e);
		}
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return sessionProvider.getJdbcTemplate();
	}

}
