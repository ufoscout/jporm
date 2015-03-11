/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.core.session.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.core.exception.JpoException;
import com.jporm.core.exception.JpoNotUniqueResultException;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.ResultSetReader;
import com.jporm.core.query.ResultSetRowReader;
import com.jporm.core.session.BatchPreparedStatementSetter;
import com.jporm.core.session.GeneratedKeyReader;
import com.jporm.core.session.PreparedStatementSetter;
import com.jporm.core.session.SqlExecutor;
import com.jporm.core.session.SqlPerformerStrategy;
import com.jporm.core.session.reader.ArrayResultSetReader;
import com.jporm.core.session.reader.ArrayResultSetReaderUnique;
import com.jporm.core.session.reader.BigDecimalResultSetReader;
import com.jporm.core.session.reader.BigDecimalResultSetReaderUnique;
import com.jporm.core.session.reader.ListResultSetReader;
import com.jporm.core.session.reader.ResultSetRowReaderToResultSetReader;
import com.jporm.core.session.reader.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.core.session.reader.StringResultSetReader;
import com.jporm.core.session.reader.StringResultSetReaderUnique;
import com.jporm.sql.dialect.statement.StatementStrategy;
import com.jporm.types.JpoJdbcStatement;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverterJdbcReady;

/**
 * @author Francesco Cina 02/lug/2011
 */
public class SqlExecutorImpl implements SqlExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SqlExecutorImpl.class);

	public static final ResultSetReader<String> RESULT_SET_READER_STRING_UNIQUE = new StringResultSetReaderUnique();
	public static final ResultSetReader<String> RESULT_SET_READER_STRING = new StringResultSetReader();
	public static final ResultSetReader<BigDecimal> RESULT_SET_READER_BIG_DECIMAL_UNIQUE = new BigDecimalResultSetReaderUnique();
	public static final ResultSetReader<BigDecimal> RESULT_SET_READER_BIG_DECIMAL = new BigDecimalResultSetReader();
	public static final ResultSetReader<Object[]> RESULT_SET_READER_ARRAY_UNIQUE = new ArrayResultSetReaderUnique();
	public static final ResultSetReader<Object[]> RESULT_SET_READER_ARRAY = new ArrayResultSetReader();
	public static final ResultSetReader<List<Object[]>> RESULT_SET_READER_LIST = new ListResultSetReader();

	private final SqlPerformerStrategy sqlPerformerStrategy;
	private final TypeConverterFactory typeFactory;
	private final StatementStrategy statementStrategy;

	/**
	 * @param sqlPerformerStrategy2
	 * @param serviceCatalog
	 */
	public SqlExecutorImpl(final SqlPerformerStrategy sqlPerformerStrategy, final ServiceCatalog serviceCatalog) {
		this.sqlPerformerStrategy = sqlPerformerStrategy;
		typeFactory = serviceCatalog.getTypeFactory();
		statementStrategy = serviceCatalog.getDbProfile().getStatementStrategy();
	}

	@Override
	public int[] batchUpdate(final Stream<String> sqls) throws JpoException {
		return sqlPerformerStrategy.batchUpdate(sqls);
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		return sqlPerformerStrategy.batchUpdate(sql, psc);
	}

	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> args) throws JpoException {
		return sqlPerformerStrategy.batchUpdate(sql, args.map(values -> {
		Object[] unwrappedValues = new Object[values.length];
		for (int i=0; i<values.length; i++) {
			Object object = values[i];
			if (object!=null) {
				TypeConverterJdbcReady<Object, Object> typeWrapper = (TypeConverterJdbcReady<Object, Object>) typeFactory.getTypeConverter(object.getClass());
				unwrappedValues[i] = typeWrapper.toJdbcType(object);
			}
		}
		return unwrappedValues;
	}));
	}

	@Override
	public void execute(final String sql) throws JpoException {
		sqlPerformerStrategy.execute(sql);
	}

	@Override
	public <T> T query(final String sql, final ResultSetReader<T> rse, final Collection<?> args) throws JpoException {
		PreparedStatementSetter pss = new PrepareStatementSetterCollectionWrapper(args, typeFactory);
		return sqlPerformerStrategy.query(sql, pss, rse);
	}

	@Override
	public <T> T query(final String sql, final ResultSetReader<T> rse, final Object... args) throws JpoException {
		PreparedStatementSetter pss = new PrepareStatementSetterArrayWrapper(args, typeFactory);
		return sqlPerformerStrategy.query(sql, pss, rse);
	}

	@Override
	public <T> List<T> query(final String sql, final ResultSetRowReader<T> rsrr, final Collection<?> args)
			throws JpoException {
		return query(sql, new ResultSetRowReaderToResultSetReader<T>(rsrr), args);
	}

	@Override
	public <T> List<T> query(final String sql, final ResultSetRowReader<T> rsrr, final Object... args)
			throws JpoException {
		return query(sql, new ResultSetRowReaderToResultSetReader<T>(rsrr), args);
	}

	@Override
	public Object[] queryForArray(String sql, Collection<?> args) throws JpoException, JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY, args);
	}

	@Override
	public Object[] queryForArray(String sql, Object... args) throws JpoException, JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY, args);
	}

	@Override
	public Optional<Object[]> queryForArrayOptional(final String sql, final Collection<?> args) {
		return Optional.ofNullable(queryForArray(sql, args));
	}

	@Override
	public Optional<Object[]> queryForArrayOptional(final String sql, final Object... args) {
		return Optional.ofNullable(queryForArray(sql, args));
	}

	@Override
	public final Object[] queryForArrayUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY_UNIQUE, values);
	}

	@Override
	public final Object[] queryForArrayUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY_UNIQUE, values);
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public Double queryForDouble(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public Double queryForDouble(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public Float queryForFloat(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public Float queryForFloat(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public Integer queryForInt(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public Integer queryForInt(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public final List<Object[]> queryForList(final String sql, final Collection<?> values) throws JpoException {
		return this.query(sql, RESULT_SET_READER_LIST, values);
	}

	@Override
	public final List<Object[]> queryForList(final String sql, final Object... values) throws JpoException {
		return this.query(sql, RESULT_SET_READER_LIST, values);
	}

	@Override
	public Long queryForLong(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public Long queryForLong(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public String queryForString(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING, args);
	}

	@Override
	public String queryForString(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING, args);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING_UNIQUE, values);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING_UNIQUE, values);
	}

	@Override
	public <T> T queryForUnique(final String sql, final ResultSetRowReader<T> rsrr, final Collection<?> args)
			throws JpoException {
		return query(sql, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr), args);
	}

	@Override
	public <T> T queryForUnique(final String sql, final ResultSetRowReader<T> rsrr, final Object... args)
			throws JpoException, JpoNotUniqueResultException {
		return query(sql, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr), args);
	}

	@Override
	public int update(final String sql, final Collection<?> args) throws JpoException {
		PreparedStatementSetter pss = new PrepareStatementSetterCollectionWrapper(args, typeFactory);
		return sqlPerformerStrategy.update(sql, pss);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final Collection<?> args)
			throws JpoException {
		PreparedStatementSetter pss = new PrepareStatementSetterCollectionWrapper(args, typeFactory);
		return sqlPerformerStrategy.update(sql, generatedKeyReader, statementStrategy, pss);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final Object... args)
			throws JpoException {
		PreparedStatementSetter pss = new PrepareStatementSetterArrayWrapper(args, typeFactory);
		return sqlPerformerStrategy.update(sql, generatedKeyReader, statementStrategy, pss);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final PreparedStatementSetter psc)
			throws JpoException {
		return sqlPerformerStrategy.update(sql, generatedKeyReader, statementStrategy, psc);
	}

	@Override
	public int update(final String sql, final Object... args) throws JpoException {
		PreparedStatementSetter pss = new PrepareStatementSetterArrayWrapper(args, typeFactory);
		return sqlPerformerStrategy.update(sql, pss);
	}

	@Override
	public int update(final String sql, final PreparedStatementSetter psc) throws JpoException {
		return sqlPerformerStrategy.update(sql, psc);
	}

	class PrepareStatementSetterArrayWrapper implements PreparedStatementSetter {
		private final Object[] args;
		private final TypeConverterFactory typeFactory;

		public PrepareStatementSetterArrayWrapper(final Object[] args, final TypeConverterFactory typeFactory) {
			this.args = args;
			this.typeFactory = typeFactory;
		}

		@Override
		public void set(final PreparedStatement ps) throws SQLException {
			if (logger.isDebugEnabled()) {
				logger.debug("Query params: " + Arrays.asList(args)); //$NON-NLS-1$
			}
			int index = 0;
			for (Object object : args) {
				if (object!=null) {
					TypeConverterJdbcReady<Object, Object> typeWrapper = (TypeConverterJdbcReady<Object, Object>) typeFactory.getTypeConverter(object.getClass());
					typeWrapper.getJdbcIO().setValueToPreparedStatement( typeWrapper.toJdbcType(object) , new JpoJdbcStatement(ps) , ++index);
				} else {
					ps.setObject(++index, object);
				}
			}
		}
	}

	class PrepareStatementSetterCollectionWrapper implements PreparedStatementSetter {

		private final Collection<?> args;
		private final TypeConverterFactory typeFactory;

		public PrepareStatementSetterCollectionWrapper(final Collection<?> args, final TypeConverterFactory typeFactory) {
			this.args = args;
			this.typeFactory = typeFactory;
		}

		@Override
		public void set(final PreparedStatement ps) throws SQLException {
			if (logger.isDebugEnabled()) {
				logger.debug("Query params: " + args); //$NON-NLS-1$
			}
			int index = 0;
			for (Object object : args) {
				if (object!=null) {
					TypeConverterJdbcReady<Object, Object> typeWrapper = (TypeConverterJdbcReady<Object, Object>) typeFactory.getTypeConverter(object.getClass());
					typeWrapper.getJdbcIO().setValueToPreparedStatement( typeWrapper.toJdbcType(object) , new JpoJdbcStatement(ps), ++index);
				} else {
					ps.setObject(++index, object);
				}
			}
		}

	}
}
