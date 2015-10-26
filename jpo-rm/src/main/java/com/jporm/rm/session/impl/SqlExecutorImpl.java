/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rm.session.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReader;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.commons.core.util.BigDecimalUtil;
import com.jporm.rm.session.Connection;
import com.jporm.rm.session.ConnectionProvider;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;
import com.jporm.types.io.StatementSetter;

/**
 * @author Francesco Cina 02/lug/2011
 */
public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);
	private final ConnectionProvider connectionProvider;
	private final boolean autoCommit;

	/**
	 * @param sqlPerformerStrategy2
	 * @param serviceCatalog
	 */
	public SqlExecutorImpl(final ConnectionProvider connectionProvider, final TypeConverterFactory typeFactory, boolean autoCommit) {
		super(typeFactory);
		this.connectionProvider = connectionProvider;
		this.autoCommit = autoCommit;
	}

	@Override
	public int[] batchUpdate(final Collection<String> sqls) throws JpoException {
		Connection connection = null;
		try {
			connection = connectionProvider.getConnection(autoCommit);
			return connection.batchUpdate(sqls);
		} finally {
			if (connection!=null) {
				connection.close();
			}
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		Connection connection = null;
		try {
			connection = connectionProvider.getConnection(autoCommit);
			return connection.batchUpdate(sql, psc);
		} finally {
			if (connection!=null) {
				connection.close();
			}
		}
	}

	@Override
	public int[] batchUpdate(final String sql, final Collection<Object[]> args) throws JpoException {
		Connection connection = null;
		try {
			connection = connectionProvider.getConnection(autoCommit);
			Collection<StatementSetter> statements = new ArrayList<>();
			args.forEach(array -> statements.add(new PrepareStatementSetterArrayWrapper(array)));
			return connection.batchUpdate(sql, statements);
		} finally {
			if (connection!=null) {
				connection.close();
			}
		}
	}

	@Override
	public void execute(final String sql) throws JpoException {
		Connection connection = null;
		try {
			connection = connectionProvider.getConnection(autoCommit);
			connection.execute(sql);
		} finally {
			if (connection!=null) {
				connection.close();
			}
		}
	}

	@Override
	public <T> T query(final String sql, final Collection<?> args, final ResultSetReader<T> rse) throws JpoException {
		Connection connection = null;
		try {
			connection = connectionProvider.getConnection(autoCommit);
			StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
			return connection.query(sql, pss, rse);
		} finally {
			if (connection!=null) {
				connection.close();
			}
		}
	}

	@Override
	public <T> T query(final String sql, final Object[] args, final ResultSetReader<T> rse) throws JpoException {
		Connection connection = null;
		try {
			connection = connectionProvider.getConnection(autoCommit);
			StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
			return connection.query(sql, pss, rse);
		} finally {
			if (connection!=null) {
				connection.close();
			}
		}
	}

	@Override
	public <T> List<T> query(final String sql, final Collection<?> args, final ResultSetRowReader<T> rsrr)
			throws JpoException {
		return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(rsrr));
	}

	@Override
	public <T> List<T> query(final String sql, final Object[] args, final ResultSetRowReader<T> rsrr)
			throws JpoException {
		return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(rsrr));
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public Double queryForDouble(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public Double queryForDouble(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public Float queryForFloat(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public Float queryForFloat(final String sql, final Object[] args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public Integer queryForInt(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public Integer queryForInt(final String sql, final Object[] args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public Long queryForLong(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public Long queryForLong(final String sql, final Object[] args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public String queryForString(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_STRING);
	}

	@Override
	public String queryForString(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_STRING);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Object[] args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
	}

	@Override
	public <T> T queryForUnique(final String sql, final Collection<?> args, final ResultSetRowReader<T> rsrr)
			throws JpoException {
		return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));
	}

	@Override
	public <T> T queryForUnique(final String sql, final Object[] args, final ResultSetRowReader<T> rsrr)
			throws JpoException, JpoNotUniqueResultException {
		return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));
	}

	@Override
	public int update(final String sql, final Collection<?> args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, pss);
	}

	@Override
	public int update(final String sql, final Collection<?> args, final GeneratedKeyReader generatedKeyReader)
			throws JpoException {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return update(sql, pss, generatedKeyReader);
	}

	@Override
	public int update(final String sql, final Object[] args, final GeneratedKeyReader generatedKeyReader)
			throws JpoException {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, pss, generatedKeyReader);
	}

	@Override
	public int update(final String sql, final StatementSetter psc, final GeneratedKeyReader generatedKeyReader)
			throws JpoException {
		Connection connection = null;
		try {
			connection = connectionProvider.getConnection(autoCommit);
			return connection.update(sql, generatedKeyReader, psc);
		} finally {
			if (connection!=null) {
				connection.close();
			}
		}
	}

	@Override
	public int update(final String sql, final Object[] args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return update(sql, pss);
	}

	@Override
	public int update(final String sql, final StatementSetter psc) throws JpoException {
		return update(sql, psc, GENERATING_KEY_READER_DO_NOTHING);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
